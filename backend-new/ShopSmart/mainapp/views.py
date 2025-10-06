import logging
from django.http import JsonResponse
from django.shortcuts import get_object_or_404
from django.contrib.auth import get_user_model
from django.contrib.gis.geos import Point
from django.contrib.gis.measure import Distance
from django.contrib.gis.db.models.functions import Distance as GisDbDistance
from django.utils import timezone
from rest_framework import generics, status
from rest_framework.permissions import IsAuthenticated, AllowAny
from rest_framework.views import APIView
from rest_framework.response import Response
from django.db.models import Avg, Count,F
from rest_framework.filters import SearchFilter, OrderingFilter
from .choices import ShopTypes, ProductTypes, Role
from .permissions import (
    IsOwnerOfShop, IsShopOwnerRole, IsApprovedShopOwner,
    IsOwnerOfApprovedShop, IsAdmin
)
from .serializers import (
    ProductSerializer, ProductDetailSerializer,
    UserProfileSerializer, ShopSerializer, ShopDetailSerializer,
    UserOnboardingSerializer, ShopReviewSerializer, ProductReviewSerializer,
    FavoriteShopSerializer, FavoriteProductSerializer,
    AdminShopListSerializer, AdminShopApprovalSerializer, AdminProductListSerializer,
    ChoicesSerializer, ToggleFavoriteResponseSerializer, ToggleHelpfulResponseSerializer,
    ApiRootResponseSerializer, LoadHomeResponseSerializer,ShopWithProductsSerializer,
    ProductSearchSerializer
)
from .models import Product, Shop, ShopReview, ProductReview, FavoriteShop, FavoriteProduct
from django.contrib.postgres.search import SearchVector, SearchQuery, SearchRank

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

    def list(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        trending_products = queryset.filter(position__in=[2, 3]).annotate(review_count=Count('reviews')).order_by('-review_count')[:10]
        all_products = queryset.exclude(id__in=trending_products.values_list('id', flat=True))

        trending_serializer = self.get_serializer(trending_products, many=True)
        all_serializer = self.get_serializer(all_products, many=True)

        return Response({
            'trendingProducts': trending_serializer.data,
            'allProducts': all_serializer.data
        })


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


CATEGORY_ITEM_LIMIT = 20

class LoadHomeView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = LoadHomeResponseSerializer

    def _get_base_querysets(self, user):
        """Creates base querysets for shops and products filtered by user's location radius."""
        user_location = Point(user.longitude, user.latitude, srid=4326)
        location_radius = Distance(km=user.location_radius_km)

        base_shops_qs = Shop.objects.filter(
            is_approved=True,
            location__distance_lte=(user_location, location_radius)
        ).annotate(
            distance=GisDbDistance('location', user_location),
            avg_rating=Avg('reviews__rating'),
            review_count=Count('reviews')
        )

        base_products_qs = Product.objects.filter(
            shop__in=base_shops_qs
        ).annotate(
            avg_rating=Avg('reviews__rating'),
            review_count=Count('reviews')
        ).select_related('shop')

        return base_shops_qs, base_products_qs, user_location

    def _get_trending_products(self, base_products_qs):
        """
        Gets top 20 trending products based on reviews/position, with randomization.
        """
        return base_products_qs.order_by(
            F('avg_rating').desc(nulls_last=True),
            F('review_count').desc(nulls_last=True),
            '-position',
            '?'  # <-- Ensures variety for items with similar stats
        )[:CATEGORY_ITEM_LIMIT]

    def _get_categorized_products(self, base_products_qs):
        """
        Groups products by type, with weighted random ranking inside each group.
        (This logic was already correct)
        """
        categorized_data = []
        for p_type, p_label in ProductTypes.choices:
            products_in_category = base_products_qs.filter(product_type=p_type)
            ranked_products = products_in_category.order_by('-position', '?')[:CATEGORY_ITEM_LIMIT]

            if ranked_products.exists():
                serialized_products = ProductSerializer(ranked_products, many=True).data
                categorized_data.append({
                    'type': p_label,
                    'items': serialized_products
                })
        return categorized_data

    def _get_nearby_shops(self, base_shops_qs):
        """
        Groups nearby shops by type, prioritizing closest distance, then sponsored, then randomizing order.
        """
        shop_data = []
        for s_type, s_label in ShopTypes.choices:
            shops_in_category = base_shops_qs.filter(shop_type=s_type)

            ranked_shops = shops_in_category.order_by(
                'distance',
                '-position',
                '?'
            )[:CATEGORY_ITEM_LIMIT]

            if ranked_shops.exists():
                serialized_shops = ShopSerializer(ranked_shops, many=True).data
                shop_data.append({
                    'type': s_label,
                    'items': serialized_shops
                })
        return shop_data

    def get(self, request):
            try:
                user = request.user
                if not user.latitude or not user.longitude:
                    return Response({
                        'error': 'Location not set. Please complete onboarding.'
                    }, status=status.HTTP_400_BAD_REQUEST)

                base_shops_qs, base_products_qs, user_location = self._get_base_querysets(user)
                message = 'Homepage loaded successfully.'

                if not base_shops_qs.exists():
                    message = 'No shops found in your radius. Showing results from the wider area.'

                    user_location = Point(user.longitude, user.latitude, srid=4326)

                    all_shops_qs = Shop.objects.filter(is_approved=True).annotate(
                        distance=GisDbDistance('location', user_location),
                        avg_rating=Avg('reviews__rating'),
                        review_count=Count('reviews')
                    ).order_by('distance')

                    nearest_shop_ids = list(all_shops_qs.values_list('id', flat=True)[:50])
                    base_shops_qs = all_shops_qs.filter(id__in=nearest_shop_ids)

                    base_products_qs = Product.objects.filter(
                        shop_id__in=nearest_shop_ids
                    ).annotate(
                        avg_rating=Avg('reviews__rating'),
                        review_count=Count('reviews')
                    ).select_related('shop')

                trending_products = self._get_trending_products(base_products_qs)
                categorized_products = self._get_categorized_products(base_products_qs)
                nearby_shops = self._get_nearby_shops(base_shops_qs)

                response_payload = {
                    'message': message,
                    'user_location': {'latitude': user.latitude, 'longitude': user.longitude, 'radius_km': user.location_radius_km},
                    'trending_products': trending_products,
                    'categorized_products': categorized_products,
                    'nearby_shops': nearby_shops
                }

                serializer = self.serializer_class(response_payload, context={'request': request})
                return Response(serializer.data, status=status.HTTP_200_OK)

            except Exception as e:
                print(f"Error in LoadHomeView: {str(e)}")
                return Response({'error': 'Something went wrong while loading data'}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

class ShopDetailView(generics.RetrieveAPIView):
    """
    Get detailed information about a specific APPROVED shop (for customers).
    """
    queryset = Shop.objects.filter(is_approved=True)
    serializer_class = ShopDetailSerializer
    permission_classes = [IsAuthenticated]
    lookup_field = 'pk'


class ProductDetailView(generics.RetrieveAPIView):
    queryset = Product.objects.select_related('shop')
    serializer_class = ProductDetailSerializer
    permission_classes = [IsAuthenticated]
    lookup_field = 'pk'


class ProductSearchView(generics.ListAPIView):
    serializer_class = ProductSearchSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        query = self.request.query_params.get('q', None)
        if not query:
            return Product.objects.none()

        user_location = Point(self.request.user.longitude, self.request.user.latitude, srid=4326)

        search_vector = SearchVector('name', 'description', 'category')
        search_query = SearchQuery(query,search_type='websearch')

        queryset = Product.objects.annotate(
            distance=GisDbDistance('shop__location', user_location),
            rank=SearchRank(search_vector, search_query)
        ).filter(rank__gte=0.01).order_by('-position', 'distance')

        return queryset


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
    serializer_class = ProductSerializer
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

class ShopOwnerShopDetails(generics.ListAPIView):
    """
    API for shopowner to see the details of all his shops and their products.
    """
    serializer_class = ShopWithProductsSerializer
    permission_classes = [IsAuthenticated]

    def get_queryset(self):
        return Shop.objects.filter(owner=self.request.user)


class AdminShopsListView(generics.ListAPIView):
    """
    Admin view to list all shops
    """
    serializer_class = AdminShopListSerializer
    permission_classes = [IsAuthenticated, IsAdmin]
    filter_backends = [SearchFilter, OrderingFilter]
    search_fields = ['shop_id', 'name', 'category', 'owner__full_name', 'owner__phone_number']
    ordering_fields = ['id', 'name', 'category', 'owner__full_name', 'products_count', 'is_approved', 'created_at']
    ordering = ['-created_at']

    def get_queryset(self):
        return Shop.objects.select_related('owner')


class AdminProductsListView(generics.ListAPIView):
    """
    Admin view to list all products from all shops.
    """
    serializer_class = AdminProductListSerializer
    permission_classes = [IsAuthenticated, IsAdmin]
    filter_backends = [SearchFilter, OrderingFilter]
    search_fields = ['product_id', 'name', 'category', 'shop__name']
    ordering_fields = ['id', 'name', 'price', 'category', 'stock_quantity', 'shop__name', 'created_at']
    ordering = ['-created_at']

    def get_queryset(self):
        return Product.objects.select_related('shop')


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


class AdminShopDetailView(generics.RetrieveAPIView):
    """
    Admin view for retrieving any shop's details.
    """
    queryset = Shop.objects.all()
    serializer_class = ShopDetailSerializer
    permission_classes = [IsAuthenticated, IsAdmin]
    lookup_field = 'pk'


class AdminDeleteShopView(generics.DestroyAPIView):
    """
    Admin view to delete a shop.
    """
    queryset = Shop.objects.all()
    serializer_class = ShopSerializer
    permission_classes = [IsAuthenticated, IsAdmin]


class AdminDeleteProductView(generics.DestroyAPIView):
    """
    Admin view to delete a product.
    """
    queryset = Product.objects.all()
    serializer_class = ProductSerializer
    permission_classes = [IsAuthenticated, IsAdmin]


def health_check(request):
    return JsonResponse({'ok': True})