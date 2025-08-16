from django.urls import path

from .views import SendOTPView, VerifyOTPView, ProductListCreateView, OnboardingView, ApiRootView

urlpatterns = [
    path('', ApiRootView.as_view(), name='api-root'),
    path('send-otp/', SendOTPView.as_view(), name='send-otp'),
    path('verify-otp/', VerifyOTPView.as_view(), name='verify-otp'),
    path('onboarding/', OnboardingView.as_view(), name='onboarding'),

    path('shops/<int:shop_pk>/products/', ProductListCreateView.as_view(), name='product-list-create'),

]
