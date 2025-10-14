from rest_framework import viewsets, permissions, generics, status
from rest_framework.response import Response
from django.db import transaction
from .models import SubscriptionPlan, ActiveSubscription
from .serializers import (
    SubscriptionPlanSerializer, SubscriptionPurchaseSerializer, ActiveSubscriptionSerializer
)
from mainapp.permissions import IsAdmin, IsShopOwnerRole


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
    Allows a shop owner to purchase a subscription for a shop or product.
    """
    serializer_class = SubscriptionPurchaseSerializer
    permission_classes = [permissions.IsAuthenticated, IsShopOwnerRole]

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)

        plan = serializer.validated_data['plan']
        item = serializer.validated_data['item']
        
        with transaction.atomic():
            active_subscription = ActiveSubscription.objects.create(user=request.user, plan=plan, content_object=item)

            if plan.position_level:
                item.position = plan.position_level
                item.save(update_fields=['position'])

        response_serializer = ActiveSubscriptionSerializer(active_subscription, context={'request': request})
        return Response(response_serializer.data, status=status.HTTP_201_CREATED)
    