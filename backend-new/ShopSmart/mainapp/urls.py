from django.urls import path
from rest_framework_simplejwt.views import TokenRefreshView

from .firebaseauth_views import FirebaseAuthView, LogoutView
from .admin_auth_views import AdminLoginView
from .views import (
    # Core views
    LoadHomeView, ShopDetailView, ProductDetailView,
    PostShopReviewView, PostProductReviewView, ChoicesView,
    
    # Favorites
    FavoriteShopsView, FavoriteProductsView, 
    ToggleFavoriteShopView, ToggleFavoriteProductView,
    
    # Shop Owner views
    RegisterShopView, AddProductView, EditShopView, 
    EditProductView, DeleteProductView,ShopOwnerShopDetails,
    
    # Admin views
    AdminShopsListView, AdminPendingShopsView, ApproveShopView, AdminDeleteShopView,
    AdminProductsListView, AdminDeleteProductView, AdminShopDetailView, 
    
    ProductListCreateView, ShopListCreateView, 
    OnboardingView, ApiRootView,
    health_check, ShopReviewsListView, ProductReviewsListView,

    # views for helpful toggle
    ToggleShopReviewHelpfulView, ToggleProductReviewHelpfulView,
    ProductSearchView,
)


urlpatterns = [
    path('', ApiRootView.as_view(), name='api-root'),
    
    # Core endpoints
    path('load/', LoadHomeView.as_view(), name='load-home'),
    path('onboarding/', OnboardingView.as_view(), name='onboarding'),
    path("choices/", ChoicesView.as_view(), name="choices"),
    
    # Auth endpoints
    path("auth/firebase/", FirebaseAuthView.as_view(), name="firebase-auth"),
    path("auth/refresh/", TokenRefreshView.as_view(), name="token-refresh"),
    path("auth/logout/", LogoutView.as_view(), name="logout"),

    # Shop endpoints
    path('shops/', ShopListCreateView.as_view(), name='shop-list-create'),
    path('shops/<int:pk>/', ShopDetailView.as_view(), name='shop-detail'),
    path('shops/<int:shop_pk>/products/', ProductListCreateView.as_view(), name='product-list-create'),
    path('shops/<int:shop_pk>/reviews/', PostShopReviewView.as_view(), name='post-shop-review'),
    path('shops/<int:shop_pk>/get/reviews/',ShopReviewsListView.as_view(), name='get-shop-reviews'),
    path('shops/<int:shop_pk>/reviews/<int:review_pk>/toggle-helpful/', ToggleShopReviewHelpfulView.as_view(), name='toggle-shop-review-helpful'),
    path('shops/<int:shop_pk>/toggle-favorite/', ToggleFavoriteShopView.as_view(), name='toggle-favorite-shop'),
    
    # Product endpoints
    path('products/search/', ProductSearchView.as_view(), name='product-search'),
    path('products/<int:pk>/', ProductDetailView.as_view(), name='product-detail'),
    path('products/<int:product_pk>/reviews/', PostProductReviewView.as_view(), name='post-product-review'),
    path('products/<int:product_pk>/get/reviews/',ProductReviewsListView.as_view(), name='get-product-reviews'),
    path('products/<int:product_pk>/reviews/<int:review_pk>/toggle-helpful/', ToggleProductReviewHelpfulView.as_view(), name='toggle-product-review-helpful'),
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
    path('shop-owner/entities/',ShopOwnerShopDetails.as_view(),name ='shop-details'),
    
    # Admin endpoints
    path('admin/login/', AdminLoginView.as_view(), name='admin-login'),
    path('admin/shops/', AdminShopsListView.as_view(), name='admin-shops-list'),
    path('admin/shops/pending/', AdminPendingShopsView.as_view(), name='admin-pending-shops'),
    path('admin/shops/<int:pk>/', AdminShopDetailView.as_view(), name='admin-shop-detail'),
    path('admin/shops/<int:pk>/approve/', ApproveShopView.as_view(), name='approve-shop'),
    path('admin/shops/<int:pk>/delete/', AdminDeleteShopView.as_view(), name='admin-delete-shop'),
    path('admin/products/', AdminProductsListView.as_view(), name='admin-products-list'),
    path('admin/products/<int:pk>/delete/', AdminDeleteProductView.as_view(), name='admin-delete-product'),

    path('health/', health_check, name='health-check'),
    
]