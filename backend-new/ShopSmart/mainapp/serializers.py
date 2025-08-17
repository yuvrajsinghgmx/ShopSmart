from rest_framework import serializers
from rest_framework_gis.serializers import GeoFeatureModelSerializer

from .models import Product, Shop, User


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


class UserOnboardingSerializer(serializers.ModelSerializer):
    role = serializers.ChoiceField(choices=User.Role.choices, required=False)
    class Meta:
        model = User
        fields = [
            'id',
            'phone_number',
            'role',
            'full_name',
            'profile_image',
            'current_address',
            'latitude',
            'longitude',
            'location_radius_km',
            'onboarding_completed',
        ]
        read_only_fields = ['id', 'phone_number']

    def validate(self, attrs):
        if attrs.get('onboarding_completed'):
            required_fields = ['full_name']
            for field in required_fields:
                if not attrs.get(field):
                    raise serializers.ValidationError({field: 'This field is required to complete onboarding.'})
        if 'role' in attrs and getattr(self.instance, 'onboarding_completed', False):
            raise serializers.ValidationError({'role': 'Cannot change role after onboarding is completed.'})
        return attrs