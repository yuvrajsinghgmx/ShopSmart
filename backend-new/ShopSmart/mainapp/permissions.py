from django.shortcuts import get_object_or_404
from rest_framework import permissions

from .models import Shop, User


class IsShopOwnerRole(permissions.BasePermission):
    message = "You must be a Shop Owner to perform this action."

    def has_permission(self, request, view):
        return request.user and request.user.is_authenticated and request.user.role == User.Role.SHOP_OWNER


class IsOwnerOfShop(permissions.BasePermission):
    message = "You must be the owner of this shop to perform this action."

    def has_permission(self, request, view):
        shop_pk = view.kwargs.get('shop_pk')
        if not shop_pk:
            return False
        
        shop = get_object_or_404(Shop, pk=shop_pk)
        return shop.owner == request.user
