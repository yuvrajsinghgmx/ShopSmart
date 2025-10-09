from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import SubscriptionPlanAdminViewSet

router = DefaultRouter()
router.register(r'', SubscriptionPlanAdminViewSet, basename='subscription')

urlpatterns = [
    path('', include(router.urls)),
]