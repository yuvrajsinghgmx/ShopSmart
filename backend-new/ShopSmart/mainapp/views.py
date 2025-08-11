import os
import random
from dotenv import load_dotenv
from django.shortcuts import get_object_or_404
from rest_framework.permissions import AllowAny
from rest_framework import generics
from rest_framework.permissions import IsAuthenticated
import logging
from .models import Product, Shop
from .permissions import IsOwnerOfShop
from .serializers import  ProductSerializer

load_dotenv()


logger = logging.getLogger(__name__)

class ProductListCreateView(generics.ListCreateAPIView):
    serializer_class = ProductSerializer
    permission_classes = [IsAuthenticated, IsOwnerOfShop]

    def get_queryset(self):
        shop_pk = self.kwargs['shop_pk']
        return Product.objects.filter(shop__pk=shop_pk)

    def perform_create(self, serializer):
        shop_pk = self.kwargs['shop_pk']
        shop = get_object_or_404(Shop, pk=shop_pk)
        serializer.save(shop=shop)
