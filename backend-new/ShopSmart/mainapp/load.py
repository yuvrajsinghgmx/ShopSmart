import asyncio
import random
from django.contrib.gis.geos import Point
from django.contrib.gis.measure import Distance
from django.contrib.gis.db.models.functions import Distance as GisDbDistance
from django.db.models import Avg, Count, F
from django.views.decorators.cache import cache_page
from django.utils.decorators import method_decorator
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from asgiref.sync import sync_to_async
from rest_framework.permissions import IsAuthenticated
from .models import Shop, Product, ShopTypes, ProductTypes
from .serializers import (
    LoadHomeResponseSerializer,
    ProductSerializer,
    ShopSerializer
)
CATEGORY_ITEM_LIMIT = 20


class LoadHomeView(APIView):
    permission_classes = [IsAuthenticated]
    serializer_class = LoadHomeResponseSerializer

    async def _get_base_querysets(self, user):
        user_location = Point(user.longitude, user.latitude, srid=4326)
        radius = Distance(km=user.location_radius_km)

        base_shops_qs = (
            Shop.objects.filter(is_approved=True, location__distance_lte=(user_location, radius))
            .annotate(
                distance=GisDbDistance('location', user_location),
                avg_rating=Avg('reviews__rating'),
                review_count=Count('reviews')
            )
            .only('id', 'name', 'shop_type', 'is_sponsored', 'position', 'location')
        )

        base_products_qs = (
            Product.objects.filter(shop__in=base_shops_qs)
            .select_related('shop')
            .annotate(
                avg_rating=Avg('reviews__rating'),
                review_count=Count('reviews')
            )
            .only('id', 'name', 'product_type', 'price', 'position', 'shop')
        )

        return base_shops_qs, base_products_qs, user_location

    async def _get_trending_products(self, base_products_qs):
        qs = base_products_qs.order_by(
            F('avg_rating').desc(nulls_last=True),
            F('review_count').desc(nulls_last=True),
            '-position'
        )[:CATEGORY_ITEM_LIMIT * 2]
        products = await sync_to_async(list)(qs)
        products = random.sample(products, min(len(products), CATEGORY_ITEM_LIMIT))
        return ProductSerializer(products, many=True).data

    async def _get_categorized_products(self, base_products_qs):
        categorized_data = []

        async def fetch_category(p_type, p_label):
            qs = base_products_qs.filter(product_type=p_type).order_by('-position')[:CATEGORY_ITEM_LIMIT * 2]
            items = await sync_to_async(list)(qs)
            if items:
                random.shuffle(items)
                return {'type': p_label, 'items': ProductSerializer(items[:CATEGORY_ITEM_LIMIT], many=True).data}
            return None

        tasks = [fetch_category(p_type, p_label) for p_type, p_label in ProductTypes.choices]
        results = await asyncio.gather(*tasks)
        categorized_data = [r for r in results if r]
        return categorized_data

    async def _get_nearby_shops(self, base_shops_qs):
        async def fetch_shops(s_type, s_label):
            qs = base_shops_qs.filter(shop_type=s_type).order_by('distance', '-is_sponsored', '-position')[
                :CATEGORY_ITEM_LIMIT * 2
            ]
            shops = await sync_to_async(list)(qs)
            if shops:
                random.shuffle(shops)
                return {'type': s_label, 'items': ShopSerializer(shops[:CATEGORY_ITEM_LIMIT], many=True).data}
            return None

        tasks = [fetch_shops(s_type, s_label) for s_type, s_label in ShopTypes.choices]
        results = await asyncio.gather(*tasks)
        return [r for r in results if r]
    @method_decorator(cache_page(60 * 5))
    async def get(self, request):
        try:
            user = request.user
            if not user.latitude or not user.longitude:
                return Response(
                    {'error': 'Location not set. Please complete onboarding.'},
                    status=status.HTTP_400_BAD_REQUEST
                )

            base_shops_qs, base_products_qs, user_location = await self._get_base_querysets(user)
            message = 'Homepage loaded successfully.'

            has_shops = await sync_to_async(base_shops_qs.exists)()
            if not has_shops:
                message = 'No shops found in your radius. Showing nearest alternatives.'
                user_location = Point(user.longitude, user.latitude, srid=4326)

                all_shops_qs = (
                    Shop.objects.filter(is_approved=True)
                    .annotate(
                        distance=GisDbDistance('location', user_location),
                        avg_rating=Avg('reviews__rating'),
                        review_count=Count('reviews')
                    )
                    .order_by('distance')[:50]
                )
                nearest_shops = await sync_to_async(list)(all_shops_qs)
                base_shops_qs = Shop.objects.filter(id__in=[s.id for s in nearest_shops])
                base_products_qs = (
                    Product.objects.filter(shop__in=base_shops_qs)
                    .select_related('shop')
                    .annotate(
                        avg_rating=Avg('reviews__rating'),
                        review_count=Count('reviews')
                    )
                )
            trending_task = asyncio.create_task(self._get_trending_products(base_products_qs))
            categorized_task = asyncio.create_task(self._get_categorized_products(base_products_qs))
            shops_task = asyncio.create_task(self._get_nearby_shops(base_shops_qs))

            trending_products, categorized_products, nearby_shops = await asyncio.gather(
                trending_task, categorized_task, shops_task
            )

            response_payload = {
                'message': message,
                'user_location': {
                    'latitude': user.latitude,
                    'longitude': user.longitude,
                    'radius_km': user.location_radius_km,
                },
                'trending_products': trending_products,
                'categorized_products': categorized_products,
                'nearby_shops': nearby_shops,
            }

            serializer = self.serializer_class(response_payload, context={'request': request})
            return Response(serializer.data, status=status.HTTP_200_OK)

        except Exception as e:
            print(f"Error in LoadHomeView: {e}")
            return Response({'error': 'Something went wrong while loading data'},
                            status=status.HTTP_500_INTERNAL_SERVER_ERROR)
