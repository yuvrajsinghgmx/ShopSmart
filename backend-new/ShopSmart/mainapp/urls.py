from django.urls import path

from rest_framework_simplejwt.views import TokenRefreshView

from .firebaseauth_views import FirebaseAuthView, LogoutView
from .views import ProductListCreateView, SetUserRoleView

urlpatterns = [
    # Auth
    path("auth/firebase/", FirebaseAuthView.as_view(), name="firebase-auth"),
    path("auth/refresh/", TokenRefreshView.as_view(), name="token-refresh"),
    path("auth/logout/", LogoutView.as_view(), name="logout"),

    path('profile/set-role/', SetUserRoleView.as_view(), name='set-user-role'),

    # Products
    path('shops/<int:shop_pk>/products/', ProductListCreateView.as_view(), name='product-list-create'),
    
]
