# Complete updated urls.py

from django.urls import path
from rest_framework_simplejwt.views import TokenRefreshView

from .firebaseauth_views import FirebaseAuthView, LogoutView
from .views import (
    # Core views
    LoadHomeView, ShopDetailView, ProductDetailView,
    PostShopReviewView, PostProductReviewView,
    
    # Favorites
    FavoriteShopsView, FavoriteProductsView, 
    ToggleFavoriteShopView, ToggleFavoriteProductView,
    
    # Shop Owner views
    RegisterShopView, AddProductView, EditShopView, 
    EditProductView, DeleteProductView,
    
    # Admin views
    AdminShopsListView, AdminPendingShopsView, ApproveShopView,
    

    ProductListCreateView, SetUserRoleView, ShopListCreateView, 
    OnboardingView, ApiRootView
)


urlpatterns = [
    path('', ApiRootView.as_view(), name='api-root'),
    
    # Core endpoints
    path('load/', LoadHomeView.as_view(), name='load-home'),
    path('onboarding/', OnboardingView.as_view(), name='onboarding'),
    
    # Auth endpoints
    path("auth/firebase/", FirebaseAuthView.as_view(), name="firebase-auth"),
    path("auth/refresh/", TokenRefreshView.as_view(), name="token-refresh"),
    path("auth/logout/", LogoutView.as_view(), name="logout"),
    path('profile/set-role/', SetUserRoleView.as_view(), name='set-user-role'),

    # Shop endpoints
    path('shops/', ShopListCreateView.as_view(), name='shop-list-create'),
    path('shops/<int:pk>/', ShopDetailView.as_view(), name='shop-detail'),
    path('shops/<int:shop_pk>/products/', ProductListCreateView.as_view(), name='product-list-create'),
    path('shops/<int:shop_pk>/reviews/', PostShopReviewView.as_view(), name='post-shop-review'),
    path('shops/<int:shop_pk>/toggle-favorite/', ToggleFavoriteShopView.as_view(), name='toggle-favorite-shop'),
    
    # Product endpoints
    path('products/<int:pk>/', ProductDetailView.as_view(), name='product-detail'),
    path('products/<int:product_pk>/reviews/', PostProductReviewView.as_view(), name='post-product-review'),
    path('products/<int:product_pk>/toggle-favorite/', ToggleFavoriteProductView.as_view(), name='toggle-favorite-product'),
    
    # Favorites endpoints
    path('favorites/shops/', FavoriteShopsView.as_view(), name='favorite-shops'),
    path('favorites/products/', FavoriteProductsView.as_view(), name='favorite-products'),
    
    # Shop Owner endpoints
    path('shop-owner/register-shop/', RegisterShopView.as_view(), name='register-shop'),
    path('shop-owner/shops/<int:shop_pk>/add-product/', AddProductView.as_view(), name='add-product'),
    path('shop-owner/shops/<int:pk>/edit/', EditShopView.as_view(), name='edit-shop'),
    path('shop-owner/products/<int:pk>/edit/', EditProductView.as_view(), name='edit-product'),
    path('shop-owner/products/<int:pk>/delete/', DeleteProductView.as_view(), name='delete-product'),
    
    # Admin endpoints
    path('admin/shops/', AdminShopsListView.as_view(), name='admin-shops-list'),
    path('admin/shops/pending/', AdminPendingShopsView.as_view(), name='admin-pending-shops'),
    path('admin/shops/<int:pk>/approve/', ApproveShopView.as_view(), name='approve-shop'),
    
]