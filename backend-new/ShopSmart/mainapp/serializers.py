from rest_framework import serializers

from .models import Product


class SendOTPSerializer(serializers.Serializer):
    country_code = serializers.CharField(
        max_length=5,
        default="+91",
        help_text="Country code (e.g., +91)"
    )
    phone = serializers.CharField(
        max_length=15,
        help_text="Phone number to send OTP to"
    )

    def validate(self, data):
        phone = data.get("phone")
        country_code = data.get("country_code")

        if not phone.isdigit():
            raise serializers.ValidationError("Phone number must contain only digits.")
        if not country_code.startswith("+") or not country_code[1:].isdigit():
            raise serializers.ValidationError("Invalid country code.")
        if len(phone) < 10:
            raise serializers.ValidationError("Phone number must be at least 10 digits long.")

        return data


class VerifyOTPSerializer(serializers.Serializer):
    country_code = serializers.CharField(
        max_length=5,
        default="+91",
        help_text="Country code used"
    )
    phone = serializers.CharField(
        max_length=15,
        help_text="Phone number used for verification"
    )
    otp = serializers.CharField(
        max_length=6,
        help_text="OTP received on phone"
    )

    def validate(self, data):
        phone = data.get("phone")
        otp = data.get("otp")
        country_code = data.get("country_code")

        if not phone.isdigit():
            raise serializers.ValidationError("Phone number must contain only digits.")
        if not otp.isdigit():
            raise serializers.ValidationError("OTP must contain only digits.")
        if not country_code.startswith("+") or not country_code[1:].isdigit():
            raise serializers.ValidationError("Invalid country code.")

        return data

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