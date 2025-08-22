from rest_framework import serializers
from django.contrib.auth import get_user_model
from django.contrib.gis.geos import Point
from django.conf import settings
from .models import Product, Shop, FavoriteShop, FavoriteProduct, ShopReview, ProductReview
from .firebase_utils import FirebaseStorageManager

User = get_user_model()


class UserProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = [
            'id', 'username', 'phone_number', 'role', 'full_name', 
            'profile_image', 'current_address', 'latitude', 'longitude', 
            'location_radius_km', 'onboarding_completed'
        ]
        read_only_fields = ['id', 'username', 'phone_number']


class UserOnboardingSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = [
            'role', 'full_name', 'profile_image', 'current_address', 
            'latitude', 'longitude', 'location_radius_km', 'onboarding_completed'
        ]

    def validate(self, data):
        # Ensure role cannot be changed after onboarding is complete.
        if self.instance and self.instance.onboarding_completed:
            if 'role' in data:
                raise serializers.ValidationError({"role": "Role cannot be changed after onboarding is complete."})
        return data


class ShopSerializer(serializers.ModelSerializer):
    owner_name = serializers.CharField(source='owner.full_name', read_only=True)
    distance = serializers.SerializerMethodField()
    is_favorite = serializers.SerializerMethodField()
    reviews_count = serializers.SerializerMethodField()
    average_rating = serializers.SerializerMethodField()
    
    # Handle multiple images
    image_uploads = serializers.ListField(
        child=serializers.ImageField(), 
        write_only=True, 
        required=False,
        max_length=settings.SHOP_IMAGE_LIMIT
    )
    
    # Handle document images for shop verification
    document_uploads = serializers.ListField(
        child=serializers.ImageField(), 
        write_only=True, 
        required=False,
        max_length=settings.DOCUMENT_IMAGE_LIMIT
    )
    
    # Location fields
    latitude = serializers.FloatField(write_only=True)
    longitude = serializers.FloatField(write_only=True)

    class Meta:
        model = Shop
        fields = [
            'id', 'shop_id', 'name', 'images', 'address', 'category', 
            'description', 'is_approved', 'owner_name', 'distance', 
            'is_favorite', 'reviews_count', 'average_rating', 'created_at',
            # Write-only fields
            'image_uploads', 'document_uploads', 'latitude', 'longitude'
        ]
        read_only_fields = ['id', 'shop_id', 'is_approved', 'created_at']

    def get_distance(self, obj):
        """Calculate distance from user's location"""
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            user = request.user
            if user.latitude and user.longitude and obj.location:
                user_point = Point(user.longitude, user.latitude, srid=4326)
                # Distance in kilometers
                distance = obj.location.distance(user_point) * 111  # Rough conversion
                return round(distance, 2)
        return None

    def get_is_favorite(self, obj):
        """Check if shop is favorited by current user"""
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            return FavoriteShop.objects.filter(user=request.user, shop=obj).exists()
        return False

    def get_reviews_count(self, obj):
        return obj.reviews.count()

    def get_average_rating(self, obj):
        reviews = obj.reviews.all()
        if reviews:
            total_rating = sum(review.rating for review in reviews)
            return round(total_rating / len(reviews), 1)
        return 0

    def create(self, validated_data):
        # Handle image uploads
        image_uploads = validated_data.pop('image_uploads', [])
        document_uploads = validated_data.pop('document_uploads', [])
        latitude = validated_data.pop('latitude')
        longitude = validated_data.pop('longitude')
        
        # Create location point
        validated_data['location'] = Point(longitude, latitude, srid=4326)
        
        shop = super().create(validated_data)
        
        # Upload images to Firebase
        firebase_manager = FirebaseStorageManager()
        
        if image_uploads:
            shop.images = firebase_manager.upload_multiple_images(
                image_uploads, 'shops', settings.SHOP_IMAGE_LIMIT
            )
        
        if document_uploads:
            shop.document_images = firebase_manager.upload_multiple_images(
                document_uploads, 'documents', settings.DOCUMENT_IMAGE_LIMIT
            )
        
        shop.save()
        return shop


class ShopDetailSerializer(ShopSerializer):
    """Extended serializer for shop detail view"""
    products_count = serializers.SerializerMethodField()
    recent_reviews = serializers.SerializerMethodField()
    
    class Meta(ShopSerializer.Meta):
        fields = ShopSerializer.Meta.fields + ['products_count', 'recent_reviews']
    
    def get_products_count(self, obj):
        return obj.products.count()
    
    def get_recent_reviews(self, obj):
        recent_reviews = obj.reviews.order_by('-created_at')[:5]
        return ShopReviewSerializer(recent_reviews, many=True).data


class ProductSerializer(serializers.ModelSerializer):
    shop_name = serializers.CharField(source='shop.name', read_only=True)
    shop_id = serializers.CharField(source='shop.shop_id', read_only=True)
    is_favorite = serializers.SerializerMethodField()
    reviews_count = serializers.SerializerMethodField()
    average_rating = serializers.SerializerMethodField()
    
    # Handle multiple images
    image_uploads = serializers.ListField(
        child=serializers.ImageField(), 
        write_only=True, 
        required=False,
        max_length=settings.PRODUCT_IMAGE_LIMIT
    )

    class Meta:
        model = Product
        fields = [
            'id', 'product_id', 'name', 'price', 'description', 'category', 
            'stock_quantity', 'images', 'shop_name', 'shop_id', 'is_favorite',
            'reviews_count', 'average_rating', 'created_at',
            # Write-only fields
            'image_uploads'
        ]
        read_only_fields = ['id', 'product_id', 'created_at']

    def get_is_favorite(self, obj):
        """Check if product is favorited by current user"""
        request = self.context.get('request')
        if request and hasattr(request, 'user') and request.user.is_authenticated:
            return FavoriteProduct.objects.filter(user=request.user, product=obj).exists()
        return False

    def get_reviews_count(self, obj):
        return obj.reviews.count()

    def get_average_rating(self, obj):
        reviews = obj.reviews.all()
        if reviews:
            total_rating = sum(review.rating for review in reviews)
            return round(total_rating / len(reviews), 1)
        return 0

    def create(self, validated_data):
        image_uploads = validated_data.pop('image_uploads', [])
        
        product = super().create(validated_data)
        
        # Upload images to Firebase
        if image_uploads:
            firebase_manager = FirebaseStorageManager()
            product.images = firebase_manager.upload_multiple_images(
                image_uploads, 'products', settings.PRODUCT_IMAGE_LIMIT
            )
            product.save()
        
        return product

    def update(self, instance, validated_data):
        image_uploads = validated_data.pop('image_uploads', [])
        
        product = super().update(instance, validated_data)
        
        # Handle image updates
        if image_uploads:
            firebase_manager = FirebaseStorageManager()
            
            # Delete old images
            if product.images:
                firebase_manager.delete_multiple_images(product.images)
            
            # Upload new images
            product.images = firebase_manager.upload_multiple_images(
                image_uploads, 'products', settings.PRODUCT_IMAGE_LIMIT
            )
            product.save()
        
        return product


class ProductDetailSerializer(ProductSerializer):
    """Extended serializer for product detail view"""
    shop_details = serializers.SerializerMethodField()
    recent_reviews = serializers.SerializerMethodField()
    
    class Meta(ProductSerializer.Meta):
        fields = ProductSerializer.Meta.fields + ['shop_details', 'recent_reviews']
    
    def get_shop_details(self, obj):
        return {
            'id': obj.shop.id,
            'shop_id': obj.shop.shop_id,
            'name': obj.shop.name,
            'address': obj.shop.address,
            'category': obj.shop.category
        }
    
    def get_recent_reviews(self, obj):
        recent_reviews = obj.reviews.order_by('-created_at')[:5]
        return ProductReviewSerializer(recent_reviews, many=True).data


class ShopReviewSerializer(serializers.ModelSerializer):
    user_name = serializers.CharField(source='user.full_name', read_only=True)
    
    class Meta:
        model = ShopReview
        fields = ['id', 'rating', 'comment', 'user_name', 'created_at']
        read_only_fields = ['id', 'created_at']

    def create(self, validated_data):
        validated_data['user'] = self.context['request'].user
        validated_data['shop_id'] = self.context['shop_id']
        return super().create(validated_data)


class ProductReviewSerializer(serializers.ModelSerializer):
    user_name = serializers.CharField(source='user.full_name', read_only=True)
    
    class Meta:
        model = ProductReview
        fields = ['id', 'rating', 'comment', 'user_name', 'created_at']
        read_only_fields = ['id', 'created_at']

    def create(self, validated_data):
        validated_data['user'] = self.context['request'].user
        validated_data['product_id'] = self.context['product_id']
        return super().create(validated_data)


class FavoriteShopSerializer(serializers.ModelSerializer):
    """Serializer for favorite shops"""
    shop_name = serializers.CharField(source='shop.name', read_only=True)
    shop_images = serializers.JSONField(source='shop.images', read_only=True)
    shop_category = serializers.CharField(source='shop.category', read_only=True)
    shop_address = serializers.CharField(source='shop.address', read_only=True)
    shop_id = serializers.CharField(source='shop.shop_id', read_only=True)
    average_rating = serializers.SerializerMethodField()
    
    class Meta:
        model = FavoriteShop
        fields = [
            'id', 'shop_id', 'shop_name', 'shop_images', 'shop_category', 
            'shop_address', 'average_rating', 'added_at'
        ]
    
    def get_average_rating(self, obj):
        reviews = obj.shop.reviews.all()
        if reviews:
            total_rating = sum(review.rating for review in reviews)
            return round(total_rating / len(reviews), 1)
        return 0


class FavoriteProductSerializer(serializers.ModelSerializer):
    """Serializer for favorite products"""
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
            'id', 'product_id', 'product_name', 'product_price', 'product_images',
            'product_category', 'shop_name', 'shop_id', 'average_rating', 'added_at'
        ]
    
    def get_average_rating(self, obj):
        reviews = obj.product.reviews.all()
        if reviews:
            total_rating = sum(review.rating for review in reviews)
            return round(total_rating / len(reviews), 1)
        return 0


# Admin Serializers
class AdminShopListSerializer(serializers.ModelSerializer):
    """Serializer for admin shop list with basic info"""
    owner_name = serializers.CharField(source='owner.full_name', read_only=True)
    owner_phone = serializers.CharField(source='owner.phone_number', read_only=True)
    products_count = serializers.SerializerMethodField()
    
    class Meta:
        model = Shop
        fields = [
            'id', 'shop_id', 'name', 'category', 'address', 'is_approved',
            'owner_name', 'owner_phone', 'products_count', 'created_at'
        ]
    
    def get_products_count(self, obj):
        return obj.products.count()


class AdminShopApprovalSerializer(serializers.ModelSerializer):
    """Serializer for shop approval/rejection"""
    approval_action = serializers.ChoiceField(choices=['approve', 'reject'], write_only=True)
    rejection_reason = serializers.CharField(required=False, allow_blank=True)
    
    class Meta:
        model = Shop
        fields = ['id', 'shop_id', 'is_approved', 'approval_action', 'rejection_reason']
        read_only_fields = ['id', 'shop_id', 'is_approved']