from rest_framework import viewsets, permissions
from .models import SubscriptionPlan
from .serializers import SubscriptionPlanSerializer
from mainapp.permissions import IsAdmin

class SubscriptionPlanAdminViewSet(viewsets.ModelViewSet):
    """
    Admin ViewSet for managing Subscription Plans.
    """
    queryset = SubscriptionPlan.objects.all().order_by('price')
    serializer_class = SubscriptionPlanSerializer
    permission_classes = [permissions.IsAuthenticated, IsAdmin]
