from rest_framework.permissions import BasePermission
from mainapp.choices import Role

class IsShopOwnerRole(BasePermission):
    def has_permission(self, request, view):
        return request.user and request.user.is_authenticated and request.user.role == Role.SHOP_OWNER

class IsDeliveryBoyRole(BasePermission):
    def has_permission(self, request, view):
        return request.user and request.user.is_authenticated and request.user.role == Role.DELIVERY_BOY