from django.contrib.auth import get_user_model
from rest_framework import status, permissions
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework_simplejwt.tokens import RefreshToken
from firebase_admin import auth as firebase_auth
from firebase_admin.auth import InvalidIdTokenError, ExpiredIdTokenError, RevokedIdTokenError
from rest_framework.permissions import IsAuthenticated
from drf_spectacular.utils import extend_schema

# Import the new serializers
from .serializers import (
    FirebaseAuthRequestSerializer, FirebaseAuthResponseSerializer,
    LogoutRequestSerializer, LogoutResponseSerializer
)

User = get_user_model()


class FirebaseAuthView(APIView):
    permission_classes = [permissions.AllowAny]

    @extend_schema(
        request=FirebaseAuthRequestSerializer,
        responses={200: FirebaseAuthResponseSerializer}
    )
    def post(self, request):
        """
        Authenticates a user with a Firebase ID token and returns JWT tokens.
        """
        id_token = request.data.get("id_token")
        if not id_token:
            return Response({"error": "ID token required"}, status=status.HTTP_400_BAD_REQUEST)
        try:
            decoded_token = firebase_auth.verify_id_token(id_token)
            phone = decoded_token.get("phone_number")
            uid = decoded_token.get("uid")
            firebase_email = decoded_token.get("email")

            if not phone or not uid:
                return Response({"error": "Token is missing phone number or UID"}, status=status.HTTP_400_BAD_REQUEST)

            defaults = {"phone_number": phone, "email": None}
            
            if firebase_email:
                if not User.objects.filter(email__iexact=firebase_email).exists():
                    defaults["email"] = firebase_email

            user, created = User.objects.get_or_create(username=uid, defaults=defaults)

            if not created:
                # For existing users, only update the phone number if it has changed.
                if user.phone_number != phone:
                    user.phone_number = phone
                    user.save(update_fields=['phone_number'])

            refresh = RefreshToken.for_user(user)

            # NOTE: Building favorite lists manually is kept for performance,
            # but the response structure is now documented by the serializers.
            favorite_shops_data = [
                {"id": fav.shop.id, "shop_id": fav.shop.shop_id, "name": fav.shop.name, "images": fav.shop.images, "address": fav.shop.address, "category": fav.shop.category, "description": fav.shop.description, "is_approved": fav.shop.is_approved}
                for fav in user.favorite_shops.select_related('shop').all()
            ]
            favorite_products_data = [
                {"id": fav.product.id, "product_id": fav.product.product_id, "name": fav.product.name, "images": fav.product.images, "price": str(fav.product.price), "category": fav.product.category, "description": fav.product.description, "stock_quantity": fav.product.stock_quantity}
                for fav in user.favorite_products.select_related('product').all()
            ]

            response_data = {
                "access": str(refresh.access_token),
                "refresh": str(refresh),
                "user": {
                    "id": user.id,
                    "phone_number": user.phone_number,
                    "name": user.full_name,
                    "role": user.role,   
                    "profile_pic": user.profile_image,
                    "is_new_user": not user.onboarding_completed,
                    "favorite_shops": favorite_shops_data,
                    "favorite_products": favorite_products_data,
                }
            }
            return Response(response_data, status=status.HTTP_200_OK)

        except (InvalidIdTokenError, ExpiredIdTokenError, RevokedIdTokenError) as e:
            return Response({"error": f"Invalid or expired ID token: {e}"}, status=status.HTTP_401_UNAUTHORIZED)
        except Exception as e:
            return Response({"error": f"Authentication failed: {str(e)}"}, status=status.HTTP_500_INTERNAL_SERVER_ERROR)


class LogoutView(APIView):
    permission_classes = [IsAuthenticated]
    
    @extend_schema(
        request=LogoutRequestSerializer,
        responses={200: LogoutResponseSerializer}
    )
    def post(self, request):
        """
        Blacklists a refresh token to log the user out.
        """
        try:
            refresh_token = request.data["refresh"]
            token = RefreshToken(refresh_token)
            token.blacklist()
            return Response({"message": "Successfully logged out"}, status=status.HTTP_200_OK)
        except Exception as e:
            return Response({"error": str(e)}, status=status.HTTP_400_BAD_REQUEST)