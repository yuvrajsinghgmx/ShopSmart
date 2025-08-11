import logging

from dotenv import load_dotenv
from django.shortcuts import get_object_or_404
from django.contrib.auth import get_user_model
from rest_framework import generics
from rest_framework.permissions import IsAuthenticated

from .models import Product, Shop
from .permissions import IsOwnerOfShop, IsShopOwnerRole
from .serializers import ProductSerializer, ShopSerializer

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
