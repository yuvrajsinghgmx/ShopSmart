from django.urls import path

from .views import ProductListCreateView
from .firebaseauth_views import FirebaseAuthView,LogoutView
from rest_framework_simplejwt.views import TokenRefreshView


urlpatterns = [
    path('shops/<int:shop_pk>/products/', ProductListCreateView.as_view(), name='product-list-create'),

    path("auth/firebase/", FirebaseAuthView.as_view(), name="firebase-auth"),
    path("auth/refresh/", TokenRefreshView.as_view(), name="token-refresh"),
    path("auth/logout/", LogoutView.as_view(), name="logout"),

]
