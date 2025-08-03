from rest_framework import permissions
from django.shortcuts import get_object_or_404

from .models import Shop

class IsOwnerOfShop(permissions.BasePermission):
    def has_permission(self, request, view):
        shop_pk = view.kwargs.get('shop_pk')
        shop = get_object_or_404(Shop, pk=shop_pk)
        return shop.owner == request.user
