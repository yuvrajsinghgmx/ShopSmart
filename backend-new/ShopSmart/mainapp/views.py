import logging

from dotenv import load_dotenv
from django.shortcuts import get_object_or_404
from django.contrib.auth import get_user_model
from rest_framework import generics, status
from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny
from rest_framework.reverse import reverse

from .permissions import IsOwnerOfShop, IsShopOwnerRole
from .serializers import ProductSerializer, ShopSerializer, UserOnboardingSerializer
from .models import Product, Shop

User = get_user_model()

load_dotenv()

User = get_user_model()
logger = logging.getLogger(__name__)


class ShopListCreateView(generics.ListCreateAPIView):
    queryset = Shop.objects.all()
    serializer_class = ShopSerializer
    
    def get_permissions(self):
        if self.request.method == 'POST':
            self.permission_classes = [IsAuthenticated, IsShopOwnerRole]
        else:
            self.permission_classes = [IsAuthenticated]
        return super().get_permissions()

    def perform_create(self, serializer):
        serializer.save(owner=self.request.user)


class ProductListCreateView(generics.ListCreateAPIView):
    serializer_class = ProductSerializer

    def get_permissions(self):
        if self.request.method == 'POST':
            self.permission_classes = [IsAuthenticated, IsOwnerOfShop]
        else:
            self.permission_classes = [IsAuthenticated]
        return super().get_permissions()

    def get_queryset(self):
        shop_pk = self.kwargs['shop_pk']
        return Product.objects.filter(shop__pk=shop_pk)

    def perform_create(self, serializer):
        shop_pk = self.kwargs['shop_pk']
        shop = get_object_or_404(Shop, pk=shop_pk)
        serializer.save(shop=shop)


class OnboardingView(APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        serializer = UserOnboardingSerializer(request.user)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def put(self, request):
        serializer = UserOnboardingSerializer(request.user, data=request.data, partial=True)
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class ApiRootView(APIView):
    permission_classes = [AllowAny]

    def get(self, request, format=None):
        return Response({
            'message': 'ShopSmart API is running',
            'endpoints': {
                'auth-firebase': reverse('firebase-auth', request=request, format=format),
                'auth-refresh': reverse('token-refresh', request=request, format=format),
                'auth-logout': reverse('logout', request=request, format=format),
                'onboarding': reverse('onboarding', request=request, format=format),
                'shops': reverse('shop-list-create', request=request, format=format),
            }
        }, status=status.HTTP_200_OK)