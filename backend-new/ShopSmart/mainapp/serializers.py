from rest_framework import serializers
from rest_framework_gis.serializers import GeoFeatureModelSerializer

from .models import Product, User


class UserRoleSerializer(serializers.Serializer):
    role = serializers.ChoiceField(choices=User.Role.choices)

    def validate_role(self, value):
        if not value:
            raise serializers.ValidationError("Role cannot be empty.")
        return value


class UserProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = [
            'id',
            'phone_number',
            'first_name',
            'last_name',
            'role',
            'profile_image',
            'location_radius_km'
        ]
        read_only_fields = ['id', 'phone_number']


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
