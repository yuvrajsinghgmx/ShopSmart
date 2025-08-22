# Complete updated urls.py

from django.urls import path
from rest_framework_simplejwt.views import TokenRefreshView

from .firebaseauth_views import FirebaseAuthView, LogoutView



urlpatterns = [
    path('', ApiRootView.as_view(), name='api-root'),


    # Shop endpoints
    path('shops/', ShopListCreateView.as_view(), name='shop-list-create'),
    path('shops/<int:pk>/', ShopDetailView.as_view(), name='shop-detail'),
    path('shops/<int:shop_pk>/products/', ProductListCreateView.as_view(), name='product-list-create'),
]