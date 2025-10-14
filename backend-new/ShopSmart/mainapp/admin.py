from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin

from .models import User, Shop, Product, FavoriteShop, FavoriteProduct, ShopReview, ProductReview


@admin.register(User)
class UserAdmin(BaseUserAdmin):
    fieldsets = (
        (None, {'fields': ('username', 'password')}),
        ('Personal info', {'fields': ('first_name', 'last_name', 'email', 'phone_number', 'full_name', 'profile_image')}),
        ('Location', {'fields': ('current_address', 'latitude', 'longitude', 'location_radius_km')}),
        ('Onboarding', {'fields': ('role', 'onboarding_completed')}),
        ('Permissions', {'fields': ('is_active', 'is_staff', 'is_superuser', 'groups', 'user_permissions')}),
        ('Important dates', {'fields': ('last_login', 'date_joined')}),
    )
    add_fieldsets = (
        (None, {
            'classes': ('wide',),
            'fields': ('username', 'phone_number', 'password1', 'password2'),
        }),
    )
    list_display = ('id', 'username', 'phone_number', 'role', 'onboarding_completed')
    search_fields = ('username', 'phone_number', 'email', 'full_name')
    ordering = ('id',)


@admin.register(Shop)
class ShopAdmin(admin.ModelAdmin):
    list_display = ("id", "shop_id", "owner__full_name", "name", "position", 
                    "is_approved")


@admin.register(Product)
class ProductAdmin(admin.ModelAdmin):
    list_display = ("id", "product_id", "shop", "name", "position", "price", "category")


admin.site.register(FavoriteShop)
admin.site.register(FavoriteProduct)
admin.site.register(ShopReview)
admin.site.register(ProductReview)
