import logging

from dotenv import load_dotenv
from django.shortcuts import get_object_or_404
from django.contrib.auth import get_user_model
from rest_framework import generics, status
from rest_framework.permissions import IsAuthenticated
from rest_framework.views import APIView
from rest_framework.response import Response

from .models import Product, Shop
from .permissions import IsOwnerOfShop
from .serializers import ProductSerializer, UserRoleSerializer, UserProfileSerializer

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
