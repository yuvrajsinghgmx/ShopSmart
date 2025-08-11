from rest_framework import serializers

from .models import Product



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