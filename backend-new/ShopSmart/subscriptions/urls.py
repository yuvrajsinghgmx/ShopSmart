from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import (
    SubscriptionPlanAdminViewSet, SubscriptionPlanListView, PurchaseSubscriptionView
)

router = DefaultRouter()
router.register(r'admin', SubscriptionPlanAdminViewSet, basename='subscription-admin')

urlpatterns = [
    path('plans/', SubscriptionPlanListView.as_view(), name='subscription-plans-list'),
    path('purchase/', PurchaseSubscriptionView.as_view(), name='subscription-purchase'),
    path('', include(router.urls)),
]
