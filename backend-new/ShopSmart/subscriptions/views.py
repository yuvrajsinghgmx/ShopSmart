from rest_framework import viewsets, permissions, generics, status
from rest_framework.response import Response
from django.db import transaction
from django.utils import timezone
from datetime import timedelta
from django.contrib.gis.geos import Point
from django.contrib.gis.db.models.functions import Distance as GisDbDistance
from django.contrib.gis.measure import Distance

from .models import SubscriptionPlan, ActiveSubscription, Banner, ActivityLog
from .serializers import (
    SubscriptionPlanSerializer, SubscriptionPurchaseSerializer, ActiveSubscriptionSerializer,
    BannerSerializer, ActivityLogSerializer
)
from mainapp.permissions import IsAdmin, IsShopOwnerRole
from mainapp.firebase_utils import FirebaseStorageManager
from mainapp.models import Shop
from mainapp.pagination import StandardResultsSetPagination
from .choices import PlanType


class SubscriptionPlanAdminViewSet(viewsets.ModelViewSet):
    """
    Admin ViewSet for managing Subscription Plans.
    """
    queryset = SubscriptionPlan.objects.all().order_by('price')
    serializer_class = SubscriptionPlanSerializer
    permission_classes = [permissions.IsAuthenticated, IsAdmin]


class SubscriptionPlanListView(generics.ListAPIView):
    """
    Provides a list of all active subscription plans for shop owners to view.
    """
    queryset = SubscriptionPlan.objects.filter(is_active=True).order_by('plan_type', 'price')
    serializer_class = SubscriptionPlanSerializer
    permission_classes = [permissions.IsAuthenticated]


class PurchaseSubscriptionView(generics.CreateAPIView):
    """
    Allows a shop owner to purchase a subscription for a shop, product, or banner.
    """
    serializer_class = SubscriptionPurchaseSerializer
    permission_classes = [permissions.IsAuthenticated, IsShopOwnerRole]

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        plan = serializer.validated_data['plan']
        item = serializer.validated_data['item']
        active_subscription = None
        
        with transaction.atomic():
            if plan.plan_type == PlanType.BANNER:
                banner_image = serializer.validated_data['banner_image']
                firebase_manager = FirebaseStorageManager()
                image_url = firebase_manager.upload_image(banner_image, folder='banners')

                if not image_url:
                    return Response({"error": "Failed to upload banner image."}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)

                start_date = timezone.now()
                end_date = start_date + timedelta(days=plan.duration_days)

                banner = Banner.objects.create(shop=item, image_url=image_url, start_date=start_date, 
                                               end_date=end_date, is_active=True)
                
                active_subscription = ActiveSubscription.objects.create(user=request.user, plan=plan, content_object=banner)

            else:
                active_subscription = ActiveSubscription.objects.create(user=request.user, plan=plan, content_object=item)

                if plan.position_level:
                    item.position = plan.position_level
                    item.save(update_fields=['position'])
        
        if active_subscription:
            ActivityLog.objects.create(
                user=request.user,
                action_type='SUBSCRIPTION_PURCHASED',
                details={
                    'plan_name': plan.name,
                    'plan_id': plan.id,
                    'item_type': active_subscription.content_type.model,
                    'item_id': active_subscription.object_id,
                    'item_repr': str(active_subscription.content_object),
                    'user_id': request.user.id,
                    'username': request.user.username,
                }
            )

        response_serializer = ActiveSubscriptionSerializer(active_subscription, context={'request': request})
        return Response(response_serializer.data, status=status.HTTP_201_CREATED)


class BannerListView(generics.ListAPIView):
    """
    Provides a list of active banners from shops near the customer's location.
    """
    serializer_class = BannerSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        user = self.request.user
        if not user.latitude or not user.longitude:
            return Banner.objects.none()

        user_location = Point(user.longitude, user.latitude, srid=4326)
        now = timezone.now()
        radius = Distance(km=user.location_radius_km)

        nearby_shops_qs = Shop.objects.filter(is_approved=True, location__distance_lte=(user_location, radius))

        queryset = Banner.objects.filter(shop__in=nearby_shops_qs, is_active=True, end_date__gt=now)

        if not queryset.exists():
            nearest_shops_with_banners_qs = Shop.objects.filter(
                is_approved=True, banners__is_active=True, banners__end_date__gt=now
                ).distinct().annotate(distance=GisDbDistance('location', user_location)).order_by('distance')[:5]

            queryset = Banner.objects.filter(shop__in=nearest_shops_with_banners_qs, is_active=True, end_date__gt=now)
        
        return queryset


class ActivityLogListView(generics.ListAPIView):
    """
    Admin endpoint to retrieve a paginated list of all activity logs.
    """
    queryset = ActivityLog.objects.select_related('user').all()
    serializer_class = ActivityLogSerializer
    permission_classes = [permissions.IsAuthenticated, IsAdmin]
    pagination_class = StandardResultsSetPagination