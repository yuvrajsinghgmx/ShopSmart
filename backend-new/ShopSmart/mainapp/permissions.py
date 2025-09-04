from rest_framework.permissions import BasePermission
from django.contrib.auth import get_user_model
from .models import Shop
from .choices import Role

User = get_user_model()


class IsOwner(BasePermission):
    """
    Custom permission to only allow owners of an object to edit or delete it.
    Assumes the model instance has a 'user' attribute.
    """
    def has_object_permission(self, request, view, obj):
        return obj.user == request.user


class IsOwnerOfShop(BasePermission):
    """
    Permission to check if user is the owner of the shop
    """
    def has_permission(self, request, view):
        if not request.user.is_authenticated:
            return False
        
        shop_pk = view.kwargs.get('shop_pk')
        if shop_pk:
            try:
                shop = Shop.objects.get(pk=shop_pk)
                return shop.owner == request.user
            except Shop.DoesNotExist:
                return False
        return False


class IsShopOwnerRole(BasePermission):
    """
    Permission to check if user has SHOP_OWNER role
    """
    def has_permission(self, request, view):
        return (
            request.user.is_authenticated and 
            request.user.role == Role.SHOP_OWNER 
        )


class IsApprovedShopOwner(BasePermission):
    """
    Permission to check if user owns an approved shop
    """
    def has_permission(self, request, view):
        if not request.user.is_authenticated:
            return False
        
        if request.user.role != Role.SHOP_OWNER:
            return False
            
        # Check if user has at least one approved shop
        return Shop.objects.filter(
            owner=request.user, 
            is_approved=True
        ).exists()


class IsOwnerOfApprovedShop(BasePermission):
    """
    Permission to check if user owns the specific approved shop
    """
    def has_permission(self, request, view):
        if not request.user.is_authenticated:
            return False
        
        shop_pk = view.kwargs.get('shop_pk')
        if shop_pk:
            try:
                shop = Shop.objects.get(pk=shop_pk)
                return (
                    shop.owner == request.user and 
                    shop.is_approved
                )
            except Shop.DoesNotExist:
                return False
        return False


class IsAdmin(BasePermission):
    """
    Permission for admin users only
    """
    def has_permission(self, request, view):
        user = request.user
        return (
            user.is_authenticated and (
                user.is_superuser or
                getattr(user, 'role', None) == Role.ADMIN
            )
        )