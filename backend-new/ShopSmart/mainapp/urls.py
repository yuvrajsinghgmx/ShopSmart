from django.urls import path

master
from .views import SendOTPView, VerifyOTPView, ProductListCreateView, OnboardingView, ApiRootView

urlpatterns = [
    path('', ApiRootView.as_view(), name='api-root'),
    path('send-otp/', SendOTPView.as_view(), name='send-otp'),
    path('verify-otp/', VerifyOTPView.as_view(), name='verify-otp'),
    path('onboarding/', OnboardingView.as_view(), name='onboarding'),
  
from .views import ProductListCreateView, ShopListCreateView
from .firebaseauth_views import FirebaseAuthView,LogoutView
from rest_framework_simplejwt.views import TokenRefreshView


urlpatterns = [
    # Auth
    path("auth/firebase/", FirebaseAuthView.as_view(), name="firebase-auth"),
    path("auth/refresh/", TokenRefreshView.as_view(), name="token-refresh"),
    path("auth/logout/", LogoutView.as_view(), name="logout"),
master

    # Shops
    path('shops/', ShopListCreateView.as_view(), name='shop-list-create'),

    # Products
    path('shops/<int:shop_pk>/products/', ProductListCreateView.as_view(), name='product-list-create'),
    
]