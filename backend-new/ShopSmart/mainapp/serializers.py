from typing import Optional, List, Dict
from rest_framework import serializers
from django.contrib.auth import get_user_model
from django.contrib.gis.geos import Point
from django.conf import settings
from .models import Product, Shop, FavoriteShop, FavoriteProduct, ShopReview, ProductReview
from .firebase_utils import FirebaseStorageManager

User = get_user_model()


class ChoicesSerializer(serializers.Serializer):
    """Describes the output of the ChoicesView."""
    shop_types = serializers.ListField(child=serializers.CharField())
    product_types = serializers.ListField(child=serializers.CharField())


class ToggleFavoriteResponseSerializer(serializers.Serializer):
    """Describes the output of toggle favorite views."""
    message = serializers.CharField()
    is_favorite = serializers.BooleanField()
    shop_id = serializers.UUIDField(required=False)
    product_id = serializers.UUIDField(required=False)


class ToggleHelpfulResponseSerializer(serializers.Serializer):
    """Describes the output of toggle helpful views."""
    message = serializers.CharField()
    review_id = serializers.IntegerField()
    is_helpful = serializers.BooleanField()
    helpful_count = serializers.IntegerField()


class ApiRootResponseSerializer(serializers.Serializer):
    """Describes the output of the main API Root."""
    message = serializers.CharField()
    version = serializers.CharField()
    endpoints = serializers.DictField(child=serializers.CharField())
    documentation = serializers.DictField(child=serializers.CharField())


class LoadHomeResponseSerializer(serializers.Serializer):
    """Describes the complex output of the LoadHomeView."""
    message = serializers.CharField()
    user_location = serializers.DictField(child=serializers.FloatField())
    shops = serializers.ListField(child=serializers.DictField()) # Note: For full detail, you'd nest ShopSerializer here
    total_shops = serializers.IntegerField()

class FirebaseAuthRequestSerializer(serializers.Serializer):
    """Describes the required input for Firebase authentication."""
    id_token = serializers.CharField()


class AuthFavoriteShopSerializer(serializers.Serializer):
    """Simplified shop details for the login response."""
    id = serializers.IntegerField()
    shop_id = serializers.UUIDField()
    name = serializers.CharField()
    images = serializers.ListField(child=serializers.URLField())
    address = serializers.CharField()
    category = serializers.CharField()
    description = serializers.CharField()
    is_approved = serializers.BooleanField()


class AuthFavoriteProductSerializer(serializers.Serializer):
    """Simplified product details for the login response."""
    id = serializers.IntegerField()
    product_id = serializers.UUIDField()
    name = serializers.CharField()
    images = serializers.ListField(child=serializers.URLField())
    price = serializers.DecimalField(max_digits=10, decimal_places=2)
    category = serializers.CharField()
    description = serializers.CharField()
    stock_quantity = serializers.IntegerField()


class AuthUserSerializer(serializers.Serializer):
    """Describes the 'user' object in the successful login response."""
    id = serializers.IntegerField()
    phone_number = serializers.CharField()
    name = serializers.CharField()
    role = serializers.CharField()
    profile_pic = serializers.URLField(allow_null=True)
    is_new_user = serializers.BooleanField()
    favorite_shops = AuthFavoriteShopSerializer(many=True)
    favorite_products = AuthFavoriteProductSerializer(many=True)


class FirebaseAuthResponseSerializer(serializers.Serializer):
    """Describes the entire successful login response."""
    access = serializers.CharField()
    refresh = serializers.CharField()
    user = AuthUserSerializer()



class LogoutRequestSerializer(serializers.Serializer):
    """Describes the required input for logging out."""
    refresh = serializers.CharField()


class LogoutResponseSerializer(serializers.Serializer):
    """Describes the successful logout message."""
    message = serializers.CharField()

    
class UserProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = [
            'id', 'username', 'phone_number', 'email', 'role', 'full_name', 
            'profile_image', 'current_address', 'latitude', 'longitude', 
            'location_radius_km', 'onboarding_completed'
        ]
        read_only_fields = ['id', 'username', 'phone_number', 'role']


class UserOnboardingSerializer(serializers.ModelSerializer):
    profile_image_upload = serializers.ImageField(write_only=True, required=False, allow_null=True)

    class Meta:
        model = User
        fields = [
            'role', 'full_name', 'email', 'profile_image', 'current_address', 
            'latitude', 'longitude', 'location_radius_km', 'onboarding_completed',
            'profile_image_upload'
        ]
        extra_kwargs = {
            'profile_image': {'read_only': True}
        }

    def validate(self, data):
        if self.instance and self.instance.onboarding_completed:
            if 'role' in data and self.instance.role != data['role']:
                raise serializers.ValidationError({"role": "Role cannot be changed after onboarding is complete."})
        return data

    def update(self, instance, validated_data):
        image_file = validated_data.pop('profile_image_upload', None)
        instance = super().update(instance, validated_data)

        if image_file:
            firebase_manager = FirebaseStorageManager()
            if instance.profile_image:
                firebase_manager.delete_image(instance.profile_image)
            image_url = firebase_manager.upload_image(image_file=image_file, folder='profiles')
            if image_url:
                instance.profile_image = image_url
                instance.save(update_fields=['profile_image'])
        
        if not instance.onboarding_completed:
            instance.onboarding_completed = True
            instance.save(update_fields=['onboarding_completed'])

        return instance


class ShopSerializer(serializers.ModelSerializer):
    owner_name = serializers.CharField(source='owner.full_name', read_only=True)
    distance = serializers.SerializerMethodField()
    is_favorite = serializers.SerializerMethodField()
    reviews_count = serializers.SerializerMethodField()
    average_rating = serializers.SerializerMethodField()
    sponsored = serializers.SerializerMethodField()
    
    image_uploads = serializers.ListField(
        child=serializers.ImageField(), write_only=True, required=False, max_length=settings.SHOP_IMAGE_LIMIT
    )
    document_uploads = serializers.ListField(
        child=serializers.ImageField(), write_only=True, required=False, max_length=settings.DOCUMENT_IMAGE_LIMIT
    )
    latitude = serializers.FloatField(write_only=True)
    longitude = serializers.FloatField(write_only=True)

    class Meta:
        model = Shop
        fields = [
            'id', 'shop_id', 'name', 'images', 'address', 'category', 'description', 'is_approved', 
            'owner_name', 'distance', "shop_type", 'position', 'is_favorite', 'reviews_count', 
            'average_rating', 'created_at', 'image_uploads', 'document_uploads', 'latitude', 'longitude','sponsored'
        ]
        read_only_fields = ['id', 'shop_id', 'is_approved', 'created_at']

    def get_distance(self, obj) -> Optional[float]:
        if hasattr(obj, 'distance') and obj.distance is not None:
            return round(obj.distance.km, 2)
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            user = request.user
            if user.latitude and user.longitude and obj.location:
                user_point = Point(user.longitude, user.latitude, srid=4326)
                distance = obj.location.distance(user_point) * 111
                return round(distance, 2)
        return None

    def get_is_favorite(self, obj) -> bool:
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            return FavoriteShop.objects.filter(user=request.user, shop=obj).exists()
        return False

    def get_reviews_count(self, obj) -> int:
        return obj.reviews.count()

    def get_average_rating(self, obj) -> float:
        reviews = obj.reviews.all()
        if reviews:
            total_rating = sum(review.rating for review in reviews)
            return round(total_rating / len(reviews), 1)
        return 0.0

    def get_sponsored(self, obj):
        return obj.position in [2, 3]

    def create(self, validated_data):
        image_uploads = validated_data.pop('image_uploads', [])
        document_uploads = validated_data.pop('document_uploads', [])
        latitude = validated_data.pop('latitude')
        longitude = validated_data.pop('longitude')
        validated_data['location'] = Point(longitude, latitude, srid=4326)
        shop = super().create(validated_data)
        firebase_manager = FirebaseStorageManager()
        if image_uploads:
            shop.images = firebase_manager.upload_multiple_images(image_uploads, 'shops', settings.SHOP_IMAGE_LIMIT)
        if document_uploads:
            shop.document_images = firebase_manager.upload_multiple_images(document_uploads, 'documents', settings.DOCUMENT_IMAGE_LIMIT)
        shop.save()
        return shop


class ShopDetailSerializer(ShopSerializer):
    products_count = serializers.SerializerMethodField()
    recent_reviews = serializers.SerializerMethodField()
    
    class Meta(ShopSerializer.Meta):
        fields = ShopSerializer.Meta.fields + ['products_count', 'recent_reviews']
    
    def get_products_count(self, obj) -> int:
        return obj.products.count()
    
    def get_recent_reviews(self, obj) -> List:
        recent_reviews = obj.reviews.order_by('-created_at')[:5]
        return ShopReviewSerializer(recent_reviews, many=True, context=self.context).data


class ProductSerializer(serializers.ModelSerializer):
    shop_name = serializers.CharField(source='shop.name', read_only=True)
    shop_id = serializers.CharField(source='shop.shop_id', read_only=True)
    is_favorite = serializers.SerializerMethodField()
    reviews_count = serializers.SerializerMethodField()
    average_rating = serializers.SerializerMethodField()
    
    image_uploads = serializers.ListField(  
        child=serializers.ImageField(), write_only=True, required=False, max_length=settings.PRODUCT_IMAGE_LIMIT
    )

    class Meta:
        model = Product
        fields = [
            'id', 'product_id', 'name', 'price', 'description', 'category', "product_type", 'stock_quantity', 
            'images', 'position', 'shop_name', 'shop_id', 'is_favorite', 'reviews_count', 'average_rating', 
            'created_at', 'image_uploads'
        ]
        read_only_fields = ['id', 'product_id', 'created_at']

    def get_is_favorite(self, obj) -> bool:
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            return FavoriteProduct.objects.filter(user=request.user, product=obj).exists()
        return False

    def get_reviews_count(self, obj) -> int:
        return obj.reviews.count()

    def get_average_rating(self, obj) -> float:
        reviews = obj.reviews.all()
        if reviews:
            total_rating = sum(review.rating for review in reviews)
            return round(total_rating / len(reviews), 1)
        return 0.0

    def create(self, validated_data):
        image_uploads = validated_data.pop('image_uploads', [])
        product = super().create(validated_data)
        if image_uploads:
            firebase_manager = FirebaseStorageManager()
            product.images = firebase_manager.upload_multiple_images(image_uploads, 'products', settings.PRODUCT_IMAGE_LIMIT)
            product.save()
        return product

    def update(self, instance, validated_data):
        image_uploads = validated_data.pop('image_uploads', [])
        product = super().update(instance, validated_data)
        if image_uploads:
            firebase_manager = FirebaseStorageManager()
            if product.images:
                firebase_manager.delete_multiple_images(product.images)
            product.images = firebase_manager.upload_multiple_images(image_uploads, 'products', settings.PRODUCT_IMAGE_LIMIT)
            product.save()
        return product


class ProductDetailSerializer(ProductSerializer):
    shop_details = serializers.SerializerMethodField()
    recent_reviews = serializers.SerializerMethodField()
    
    class Meta(ProductSerializer.Meta):
        fields = ProductSerializer.Meta.fields + ['shop_details', 'recent_reviews']
    
    def get_shop_details(self, obj) -> Dict:
        return {
            'id': obj.shop.id,
            'shop_id': obj.shop.shop_id,
            'name': obj.shop.name,
            'address': obj.shop.address,
            'category': obj.shop.category
        }
    
    def get_recent_reviews(self, obj) -> List:
        recent_reviews = obj.reviews.order_by('-created_at')[:5]
        return ProductReviewSerializer(recent_reviews, many=True, context=self.context).data


class ShopReviewSerializer(serializers.ModelSerializer):
    user_name = serializers.CharField(source='user.full_name', read_only=True)
    helpful_count = serializers.SerializerMethodField()
    is_helpful = serializers.SerializerMethodField()
    
    class Meta:
        model = ShopReview
        fields = ['id', 'rating', 'comment', 'user_name', 'created_at', 'helpful_count', 'is_helpful']
        read_only_fields = ['id', 'created_at']

    def get_helpful_count(self, obj) -> int:
        return obj.helpful_by.count()

    def get_is_helpful(self, obj) -> bool:
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            return obj.helpful_by.filter(id=request.user.id).exists()
        return False

    def create(self, validated_data):
        validated_data['user'] = self.context['request'].user
        validated_data['shop_id'] = self.context['shop_id']
        return super().create(validated_data)


class ProductReviewSerializer(serializers.ModelSerializer):
    user_name = serializers.CharField(source='user.full_name', read_only=True)
    helpful_count = serializers.SerializerMethodField()
    is_helpful = serializers.SerializerMethodField()
    
    class Meta:
        model = ProductReview
        fields = ['id', 'rating', 'comment', 'user_name', 'created_at', 'helpful_count', 'is_helpful']
        read_only_fields = ['id', 'created_at']

    def get_helpful_count(self, obj) -> int:
        return obj.helpful_by.count()

    def get_is_helpful(self, obj) -> bool:
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            return obj.helpful_by.filter(id=request.user.id).exists()
        return False

    def create(self, validated_data):
        validated_data['user'] = self.context['request'].user
        validated_data['product_id'] = self.context['product_id']
        return super().create(validated_data)


class FavoriteShopSerializer(serializers.ModelSerializer):
    shop_name = serializers.CharField(source='shop.name', read_only=True)
    shop_images = serializers.JSONField(source='shop.images', read_only=True)
    shop_category = serializers.CharField(source='shop.category', read_only=True)
    shop_address = serializers.CharField(source='shop.address', read_only=True)
    shop_id = serializers.CharField(source='shop.shop_id', read_only=True)
    average_rating = serializers.SerializerMethodField()
    
    class Meta:
        model = FavoriteShop
        fields = ['id', 'shop_id', 'shop_name', 'shop_images', 'shop_category', 'shop_address', 'average_rating', 'added_at']
    
    def get_average_rating(self, obj) -> float:
        reviews = obj.shop.reviews.all()
        if reviews:
            total_rating = sum(review.rating for review in reviews)
            return round(total_rating / len(reviews), 1)
        return 0.0


class FavoriteProductSerializer(serializers.ModelSerializer):
    product_name = serializers.CharField(source='product.name', read_only=True)
    product_price = serializers.DecimalField(source='product.price', max_digits=10, decimal_places=2, read_only=True)
    product_images = serializers.JSONField(source='product.images', read_only=True)
    product_category = serializers.CharField(source='product.category', read_only=True)
    product_id = serializers.CharField(source='product.product_id', read_only=True)
    shop_name = serializers.CharField(source='product.shop.name', read_only=True)
    shop_id = serializers.CharField(source='product.shop.shop_id', read_only=True)
    average_rating = serializers.SerializerMethodField()
    
    class Meta:
        model = FavoriteProduct
        fields = [
            'id', 'product_id', 'product_name', 'product_price', 'product_images', 'product_category', 
            'shop_name', 'shop_id', 'average_rating', 'added_at'
        ]
    
    def get_average_rating(self, obj) -> float:
        reviews = obj.product.reviews.all()
        if reviews:
            total_rating = sum(review.rating for review in reviews)
            return round(total_rating / len(reviews), 1)
        return 0.0

class ShopWithProductsSerializer(ShopDetailSerializer):
    products = serializers.SerializerMethodField()

    class Meta(ShopDetailSerializer.Meta):
        fields = ShopDetailSerializer.Meta.fields + ['products']

    def get_products(self, obj):
        products = obj.products.all()
        return ProductSerializer(products, many=True, context=self.context).data

class CategorizedDataSerializer(serializers.Serializer):
    type = serializers.CharField()
    items = serializers.ListField(child=serializers.DictField()) 

class LoadHomeResponseSerializer(serializers.Serializer):
    message = serializers.CharField()
    user_location = serializers.DictField(child=serializers.FloatField())
    trending_products = ProductSerializer(many=True)
    categorized_products = CategorizedDataSerializer(many=True)
    nearby_shops = CategorizedDataSerializer(many=True)


class AdminShopListSerializer(serializers.ModelSerializer):
    owner_name = serializers.CharField(source='owner.full_name', read_only=True)
    owner_phone = serializers.CharField(source='owner.phone_number', read_only=True)
    products_count = serializers.SerializerMethodField()
    
    class Meta:
        model = Shop
        fields = [
            'id', 'shop_id', 'name', 'category', 'address', 'is_approved',
            'owner_name', 'owner_phone', 'products_count', 'created_at'
        ]
    
    def get_products_count(self, obj) -> int:
        return obj.products.count()


class AdminProductListSerializer(serializers.ModelSerializer):
    """Serializer for admin product list with basic info"""
    shop_name = serializers.CharField(source='shop.name', read_only=True)
    shop_id = serializers.CharField(source='shop.shop_id', read_only=True)
    
    class Meta:
        model = Product
        fields = [
            'id', 'product_id', 'name', 'price', 'category', 
            'stock_quantity', 'shop_name', 'shop_id', 'created_at'
        ]


class AdminShopApprovalSerializer(serializers.ModelSerializer):
    approval_action = serializers.ChoiceField(choices=['approve', 'reject'], write_only=True)
    rejection_reason = serializers.CharField(required=False, allow_blank=True)
    
    class Meta:
        model = Shop
        fields = ['id', 'shop_id', 'is_approved', 'approval_action', 'rejection_reason']
        read_only_fields = ['id', 'shop_id', 'is_approved']


class AdminLoginSerializer(serializers.Serializer):
    """Describes the input for admin email/password login."""
    email = serializers.EmailField()
    password = serializers.CharField(trim_whitespace=False)


class AdminUserSerializer(serializers.Serializer):
    """Describes the 'user' object in the successful admin login response."""
    id = serializers.IntegerField()
    email = serializers.EmailField()
    name = serializers.CharField()
    role = serializers.CharField()


class AdminAuthResponseSerializer(serializers.Serializer):
    """Describes the entire successful admin login response."""
    access = serializers.CharField()
    refresh = serializers.CharField()
    user = AdminUserSerializer()