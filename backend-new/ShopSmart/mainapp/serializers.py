from rest_framework import serializers
from rest_framework_gis.serializers import GeoFeatureModelSerializer

from .models import Product, Shop


class ShopSerializer(GeoFeatureModelSerializer):
    owner = serializers.StringRelatedField(read_only=True)

    class Meta:
        model = Shop
        geo_field = "location"
        fields = [
            'id',
            'owner',
            'name',
            'image',
            'address',
            'location',
            'category',
            'description',
            'created_at',
        ]


class ProductSerializer(serializers.ModelSerializer):
    shop = serializers.PrimaryKeyRelatedField(read_only=True)

    class Meta:
        model = Product
        fields = [
            'id',
            'shop',
            'name',
            'price',
            'description',
            'category',
            'stock_quantity',
            'images',
            'created_at'
        ]