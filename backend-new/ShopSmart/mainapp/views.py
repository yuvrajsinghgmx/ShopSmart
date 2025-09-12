import logging
from django.http import JsonResponse
from django.shortcuts import get_object_or_404
from django.contrib.auth import get_user_model
from django.contrib.gis.geos import Point
from django.contrib.gis.measure import Distance
from django.contrib.gis.db.models.functions import Distance as GisDbDistance
from django.utils import timezone
from rest_framework import generics, status
from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny

from .choices import ShopTypes, ProductTypes
from .permissions import (
    IsOwnerOfShop, IsShopOwnerRole, IsApprovedShopOwner, 
    IsOwnerOfApprovedShop, IsAdmin
)
from .serializers import (
    ProductSerializer, ProductDetailSerializer, 
    UserProfileSerializer, ShopSerializer, ShopDetailSerializer, 
    UserOnboardingSerializer, ShopReviewSerializer, ProductReviewSerializer,
    FavoriteShopSerializer, FavoriteProductSerializer,
    AdminShopListSerializer, AdminShopApprovalSerializer,
    ChoicesSerializer, ToggleFavoriteResponseSerializer, ToggleHelpfulResponseSerializer,
    ApiRootResponseSerializer, LoadHomeResponseSerializer
)
from .models import Product, Shop, ShopReview, ProductReview, FavoriteShop, FavoriteProduct

User = get_user_model()
logger = logging.getLogger(__name__)


class ShopListCreateView(generics.ListCreateAPIView):
    queryset = Shop.objects.all()
    serializer_class = ShopSerializer
    
    def get_permissions(self):
        if self.request.method == 'POST':
            self.permission_classes = [IsAuthenticated, IsShopOwnerRole]
        else:
            self.permission_classes = [IsAuthenticated]
        return super().get_permissions()

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class ProductListCreateView(generics.ListCreateAPIView):
    serializer_class = ProductSerializer

    def get_permissions(self):
        if self.request.method == 'POST':
            self.permission_classes = [IsAuthenticated, IsOwnerOfShop]
        else:
            self.permission_classes = [IsAuthenticated]
        return super().get_permissions()

    def get_queryset(self):
        shop_pk = self.kwargs['shop_pk']
        return Product.objects.filter(shop__pk=shop_pk)

    def perform_create(self, serializer):
        shop_pk = self.kwargs['shop_pk']
        shop = get_object_or_404(Shop, pk=shop_pk)
        serializer.save(shop=shop)


class OnboardingView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = UserOnboardingSerializer

    def get(self, request):
        serializer = self.get_serializer(request.user)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def put(self, request):
        serializer = self.get_serializer(request.user, data=request.data, partial=True)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    
    def get_serializer(self, *args, **kwargs):
        """Helper to integrate serializer_class with APIView."""
        return self.serializer_class(*args, **kwargs)


class ApiRootView(APIView):
    permission_classes = [AllowAny]
    serializer_class = ApiRootResponseSerializer # FIX: Added serializer_class

    def get(self, request):
        base = request.build_absolute_uri('/')[:-1]
        data = {
            'message': 'ShopSmart API is running',
            'version': '1.0',
            'endpoints': {
                'load_home': f"{base}/api/load/", 'onboarding': f"{base}/api/onboarding/",
                'firebase_auth': f"{base}/api/auth/firebase/", 'token_refresh': f"{base}/api/auth/refresh/", 'logout': f"{base}/api/auth/logout/",
                'shops_list': f"{base}/api/shops/", 'shop_detail': f"{base}/api/shops/{{shop_id}}/", 'shop_products': f"{base}/api/shops/{{shop_id}}/products/",
                'post_shop_review': f"{base}/api/shops/{{shop_id}}/reviews/", 'toggle_favorite_shop': f"{base}/api/shops/{{shop_id}}/toggle-favorite/",
                'product_detail': f"{base}/api/products/{{product_id}}/", 'post_product_review': f"{base}/api/products/{{product_id}}/reviews/", 'toggle_favorite_product': f"{base}/api/products/{{product_id}}/toggle-favorite/",
                'favorite_shops': f"{base}/api/favorites/shops/", 'favorite_products': f"{base}/api/favorites/products/",
                'register_shop': f"{base}/api/shop-owner/register-shop/", 'add_product': f"{base}/api/shop-owner/shops/{{shop_id}}/add-product/",
                'edit_shop': f"{base}/api/shop-owner/shops/{{shop_id}}/edit/", 'edit_product': f"{base}/api/shop-owner/products/{{product_id}}/edit/", 'delete_product': f"{base}/api/shop-owner/products/{{product_id}}/delete/",
                'admin_shops': f"{base}/api/admin/shops/", 'admin_pending_shops': f"{base}/api/admin/shops/pending/", 'approve_shop': f"{base}/api/admin/shops/{{shop_id}}/approve/",
                'django_admin': f"{base}/admin/",
            },
            'documentation': {
                'authentication': 'JWT Bearer Token required for authenticated endpoints', 'image_upload': 'Support for multiple images via Firebase Storage',
                'geolocation': 'Location-based shop discovery within user radius', 'roles': ['CUSTOMER', 'SHOP_OWNER', 'ADMIN']
            }
        }
        return Response(data, status=status.HTTP_200_OK)


class LoadHomeView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = LoadHomeResponseSerializer # FIX: Added serializer_class
    
    def get(self, request):
        try:
            user = request.user
            if not user.latitude or not user.longitude:
                return Response({'error': 'Location not set. Please complete onboarding.', 'shops': [], 'total_shops': 0}, status=status.HTTP_400_BAD_REQUEST)
            
            user_location = Point(user.longitude, user.latitude, srid=4326)
            base_shops_qs = Shop.objects.filter(is_approved=True).annotate(distance=GisDbDistance('location', user_location)).select_related('owner').prefetch_related('products')
            nearby_shops = base_shops_qs.filter(location__distance_lte=(user_location, Distance(km=user.location_radius_km))).order_by('distance')
            
            shops_to_process = list(nearby_shops)
            if shops_to_process:
                message = f'Found {len(shops_to_process)} shops nearby'
            else:
                shops_to_process = list(base_shops_qs.order_by('distance')[:10])
                message = 'No shops found in your radius. Showing the 10 nearest shops instead.'
            
            shops_data = []
            for shop in shops_to_process:
                shop_serializer = ShopSerializer(shop, context={'request': request})
                products = shop.products.all()
                products_data = ProductSerializer(products, many=True, context={'request': request}).data
                shop_data = shop_serializer.data
                shop_data['products'] = products_data
                shops_data.append(shop_data)
            
            response_data = {
                'message': message,
                'user_location': {'latitude': user.latitude, 'longitude': user.longitude, 'radius_km': user.location_radius_km},
                'shops': shops_data, 'total_shops': len(shops_data)
            }
            return Response(response_data, status=status.HTTP_200_OK)
            
        except Exception as e:
            logger.error(f"Error in LoadHomeView: {str(e)}")
            return Response({'error': 'Something went wrong while loading data'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class ShopDetailView(generics.RetrieveAPIView):
    queryset = Shop.objects.filter(is_approved=True)
    serializer_class = ShopDetailSerializer
    permission_classes = [IsAuthenticated]
    lookup_field = 'pk'


class ProductDetailView(generics.RetrieveAPIView):
    queryset = Product.objects.select_related('shop')
    serializer_class = ProductDetailSerializer
    permission_classes = [IsAuthenticated]
    lookup_field = 'pk'


class ChoicesView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = ChoicesSerializer # FIX: Added serializer_class

    def get(self, request):
        return Response({
            "shop_types": [choice[0] for choice in ShopTypes.choices],
            "product_types": [choice[0] for choice in ProductTypes.choices],
        })


class PostShopReviewView(generics.CreateAPIView):
    serializer_class = ShopReviewSerializer
    permission_classes = [IsAuthenticated]
    
    def get_serializer_context(self):
        context = super().get_serializer_context()
        context['shop_id'] = self.kwargs['shop_pk']
        return context
    
    def perform_create(self, serializer):
        shop = get_object_or_404(Shop, pk=self.kwargs['shop_pk'], is_approved=True)
        serializer.save(user=self.request.user, shop=shop)


class PostProductReviewView(generics.CreateAPIView):
    serializer_class = ProductReviewSerializer
    permission_classes = [IsAuthenticated]
    
    def get_serializer_context(self):
        context = super().get_serializer_context()
        context['product_id'] = self.kwargs['product_pk']
        return context
    
    def perform_create(self, serializer):
        product = get_object_or_404(Product, pk=self.kwargs['product_pk'])
        serializer.save(user=self.request.user, product=product)


class ShopReviewsListView(generics.ListAPIView):
    serializer_class = ShopReviewSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        shop_pk = self.kwargs['shop_pk']
        return ShopReview.objects.filter(shop__pk=shop_pk)


class ProductReviewsListView(generics.ListAPIView):
    serializer_class = ProductReviewSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        product_pk = self.kwargs['product_pk']
        return ProductReview.objects.filter(product__pk=product_pk)


class ToggleShopReviewHelpfulView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = ToggleHelpfulResponseSerializer # FIX: Added serializer_class

    def post(self, request, shop_pk, review_pk):
        review = get_object_or_404(ShopReview, pk=review_pk, shop__pk=shop_pk)
        user = request.user
        if review.user == user:
            return Response({"error": "You cannot mark your own review as helpful."}, status=status.HTTP_400_BAD_REQUEST)

        if review.helpful_by.filter(pk=user.pk).exists():
            review.helpful_by.remove(user)
            message, is_helpful = "Review un-marked as helpful", False
        else:
            review.helpful_by.add(user)
            message, is_helpful = "Review marked as helpful", True

        return Response({'message': message, 'review_id': review.pk, 'is_helpful': is_helpful, 'helpful_count': review.helpful_by.count()}, status=status.HTTP_200_OK)


class ToggleProductReviewHelpfulView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = ToggleHelpfulResponseSerializer # FIX: Added serializer_class

    def post(self, request, product_pk, review_pk):
        review = get_object_or_404(ProductReview, pk=review_pk, product__pk=product_pk)
        user = request.user
        if review.user == user:
            return Response({"error": "You cannot mark your own review as helpful."}, status=status.HTTP_400_BAD_REQUEST)

        if review.helpful_by.filter(pk=user.pk).exists():
            review.helpful_by.remove(user)
            message, is_helpful = "Review un-marked as helpful", False
        else:
            review.helpful_by.add(user)
            message, is_helpful = "Review marked as helpful", True

        return Response({'message': message, 'review_id': review.pk, 'is_helpful': is_helpful, 'helpful_count': review.helpful_by.count()}, status=status.HTTP_200_OK)
    
    
class ToggleFavoriteShopView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = ToggleFavoriteResponseSerializer # FIX: Added serializer_class
    
    def post(self, request, shop_pk):
        try:
            shop = get_object_or_404(Shop, pk=shop_pk, is_approved=True)
            favorite, created = FavoriteShop.objects.get_or_create(user=request.user, shop=shop)
            
            if created:
                message, is_favorite = "Shop added to favorites", True
            else:
                favorite.delete()
                message, is_favorite = "Shop removed from favorites", False
                
            return Response({'message': message, 'is_favorite': is_favorite, 'shop_id': shop.shop_id}, status=status.HTTP_200_OK)
        except Exception as e:
            logger.error(f"Error toggling favorite shop: {str(e)}")
            return Response({'error': 'Something went wrong'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class ToggleFavoriteProductView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = ToggleFavoriteResponseSerializer # FIX: Added serializer_class
    
    def post(self, request, product_pk):
        try:
            product = get_object_or_404(Product, pk=product_pk)
            favorite, created = FavoriteProduct.objects.get_or_create(user=request.user, product=product)
            
            if created:
                message, is_favorite = "Product added to favorites", True
            else:
                favorite.delete()
                message, is_favorite = "Product removed from favorites", False
                
            return Response({'message': message, 'is_favorite': is_favorite, 'product_id': product.product_id}, status=status.HTTP_200_OK)
        except Exception as e:
            logger.error(f"Error toggling favorite product: {str(e)}")
            return Response({'error': 'Something went wrong'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class FavoriteShopsView(generics.ListAPIView):
    serializer_class = FavoriteShopSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return FavoriteShop.objects.filter(user=self.request.user, shop__is_approved=True).select_related('shop')


class FavoriteProductsView(generics.ListAPIView):
    serializer_class = FavoriteProductSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return FavoriteProduct.objects.filter(user=self.request.user, product__shop__is_approved=True).select_related('product', 'product__shop')


class RegisterShopView(generics.CreateAPIView):
    queryset = Shop.objects.all()
    serializer_class = ShopSerializer
    permission_classes = [IsAuthenticated, IsShopOwnerRole]

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class AddProductView(generics.CreateAPIView):
    serializer_class = ProductSerializer
    permission_classes = [IsAuthenticated, IsOwnerOfApprovedShop]

    def perform_create(self, serializer):
        shop_pk = self.kwargs['shop_pk']
        shop = get_object_or_404(Shop, pk=shop_pk, is_approved=True)
        serializer.save(shop=shop)


class EditShopView(generics.UpdateAPIView):
    queryset = Shop.objects.all()
    serializer_class = ShopSerializer
    permission_classes = [IsAuthenticated, IsOwnerOfShop]
    lookup_field = 'pk'

    def perform_update(self, serializer):
        serializer.save(is_approved=False, approval_date=None, rejection_reason='')


class EditProductView(generics.UpdateAPIView):
    serializer_class = ProductSerializer
    permission_classes = [IsAuthenticated]
    lookup_field = 'pk'

    def get_queryset(self):
        return Product.objects.filter(shop__owner=self.request.user)


class DeleteProductView(generics.DestroyAPIView):
    """Delete a product"""
    serializer_class = ProductSerializer # CRITICAL FIX: Added required serializer_class
    permission_classes = [IsAuthenticated]
    lookup_field = 'pk'

    def get_queryset(self):
        return Product.objects.filter(shop__owner=self.request.user)

    def perform_destroy(self, instance):
        if instance.images:
            from .firebase_utils import FirebaseStorageManager
            firebase_manager = FirebaseStorageManager()
            firebase_manager.delete_multiple_images(instance.images)
        super().perform_destroy(instance)


class AdminShopsListView(generics.ListAPIView):
    """
    Admin view to list all shops
    """
    serializer_class = AdminShopListSerializer
    permission_classes = [IsAuthenticated, IsAdmin]
    
    def get_queryset(self):
        return Shop.objects.select_related('owner')


class AdminPendingShopsView(generics.ListAPIView):
    serializer_class = ShopDetailSerializer
    permission_classes = [IsAuthenticated, IsAdmin]
    
    def get_queryset(self):
        return Shop.objects.filter(is_approved=False).select_related('owner')


class ApproveShopView(generics.UpdateAPIView):
    queryset = Shop.objects.all()
    serializer_class = AdminShopApprovalSerializer
    permission_classes = [IsAuthenticated, IsAdmin]
    lookup_field = 'pk'
    
    def put(self, request, *args, **kwargs):
        shop = self.get_object()
        serializer = self.get_serializer(shop, data=request.data, partial=True)
        
        if serializer.is_valid():
            action = serializer.validated_data['approval_action']
            if action == 'approve':
                shop.is_approved = True
                shop.approval_date = timezone.now()
                shop.rejection_reason = ''
                message = f"Shop '{shop.name}' has been approved"
            elif action == 'reject':
                shop.is_approved = False
                shop.approval_date = None
                shop.rejection_reason = serializer.validated_data.get('rejection_reason', '')
                message = f"Shop '{shop.name}' has been rejected"
            
            shop.save()
            return Response({'message': message, 'shop_id': shop.shop_id, 'is_approved': shop.is_approved}, status=status.HTTP_200_OK)
        
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class AdminDeleteShopView(generics.DestroyAPIView):
    """
    Admin view to delete a shop.
    """
    queryset = Shop.objects.all()
    serializer_class = ShopSerializer
    permission_classes = [IsAuthenticated, IsAdmin]


def health_check(request):
    return JsonResponse({'ok': True})