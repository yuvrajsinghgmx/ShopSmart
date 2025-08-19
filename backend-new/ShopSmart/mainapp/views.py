import logging

from dotenv import load_dotenv
from django.shortcuts import get_object_or_404
from django.contrib.auth import get_user_model
from rest_framework import generics, status
from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.permissions import AllowAny

from .permissions import IsOwnerOfShop, IsShopOwnerRole
from .serializers import ProductSerializer, UserRoleSerializer, UserProfileSerializer, ShopSerializer, UserOnboardingSerializer
from .models import Product, Shop

User = get_user_model()

load_dotenv()

User = get_user_model()
logger = logging.getLogger(__name__)


class SetUserRoleView(APIView):
    permission_classes = [IsAuthenticated]
    
    def post(self, request, *args, **kwargs):
        user = request.user
        
        role_serializer = UserRoleSerializer(data=request.data)
        if role_serializer.is_valid():
            new_role = role_serializer.validated_data['role']
            
            user.role = new_role
            user.save()
            
            profile_serializer = UserProfileSerializer(user)
            return Response(profile_serializer.data, status=status.HTTP_200_OK)
        
        return Response(role_serializer.errors, status=status.HTTP_400_BAD_REQUEST)


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

    def get(self, request):
        base = request.build_absolute_uri('/')[:-1]
        return Response({
            'message': 'ShopSmart API is running',
            'endpoints': {
                'send_otp': f"{base}/api/send-otp/",
                'verify_otp': f"{base}/api/verify-otp/",
                'onboarding': f"{base}/api/onboarding/",
                'admin': f"{base}/admin/",
            }
        }, status=status.HTTP_200_OK)
