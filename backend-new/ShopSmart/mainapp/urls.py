from django.urls import path

from .views import SendOTPView, VerifyOTPView, ProductListCreateView

urlpatterns = [
    path('send-otp/', SendOTPView.as_view(), name='send-otp'),
    path('verify-otp/', VerifyOTPView.as_view(), name='verify-otp'),

    path('shops/<int:shop_pk>/products/', ProductListCreateView.as_view(), name='product-list-create'),

]
