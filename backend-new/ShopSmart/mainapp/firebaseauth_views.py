from django.contrib.auth import get_user_model
from rest_framework import status, permissions
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework_simplejwt.tokens import RefreshToken
from firebase_admin import auth as firebase_auth
from firebase_admin.auth import InvalidIdTokenError, ExpiredIdTokenError, RevokedIdTokenError
from rest_framework.permissions import IsAuthenticated


User = get_user_model()


class FirebaseAuthView(APIView):
    permission_classes = [permissions.AllowAny]

    def post(self, request):
        id_token = request.data.get("id_token")
        if not id_token:
            return Response({"error": "ID token required"}, status=status.HTTP_400_BAD_REQUEST)
        try:
            decoded_token = firebase_auth.verify_id_token(id_token)
            phone = decoded_token.get("phone_number")
            uid = decoded_token.get("uid")

            if not phone:
                return Response({"error": "Phone number not found in token"}, status=status.HTTP_400_BAD_REQUEST)

            user, created = User.objects.get_or_create(username=uid, defaults={"phone_number": phone})

            if not created and user.phone_number != phone:
                user.phone_number = phone
                user.save()

            full_name = user.get_full_name().strip()
            role = user.role

            is_new_user = False
            if not full_name or not role:
                is_new_user = True

            refresh = RefreshToken.for_user(user)
            favorite_shops = []
            for fav_shop in user.favorite_shops.select_related('shop').all():
                shop = fav_shop.shop
                favorite_shops.append({
                    "id": shop.id,
                    "shop_id": getattr(shop, "shop_id", None),
                    "name": shop.name,
                    "images": getattr(shop, "images", []),
                    "address": shop.address,
                    "category": shop.category,
                    "description": shop.description,
                    "is_approved": getattr(shop, "is_approved", None),
                })
            favorite_products = []
            for fav_product in user.favorite_products.select_related('product').all():
                product = fav_product.product
                favorite_products.append({
                    "id": product.id,
                    "product_id": getattr(product, "product_id", None),
                    "name": product.name,
                    "images": getattr(product, "images", []),
                    "price": str(product.price),
                    "category": product.category,
                    "description": product.description,
                    "stock_quantity": product.stock_quantity,
                })

            return Response({
                "access": str(refresh.access_token),
                "refresh": str(refresh),
                "user": {
                    "id": user.id,
                    "phone_number": phone,
                    "name": full_name,
                    "role": role,
                    "profile_pic": getattr(user, "profile_image", None).url if getattr(user, "profile_image", None) else None,
                    "is_new_user": is_new_user,
                    "favorite_shops": favorite_shops,
                    "favorite_products": favorite_products,
                }
            }, status=status.HTTP_200_OK)

        except (InvalidIdTokenError, ExpiredIdTokenError, RevokedIdTokenError):
            return Response({"error": "Invalid or expired ID token"}, status=status.HTTP_400_BAD_REQUEST)
        except Exception as e:
            return Response({"error": f"Authentication failed: {str(e)}"}, status=status.HTTP_400_BAD_REQUEST)


class LogoutView(APIView):
    permission_classes = [IsAuthenticated]
    
    def post(self, request):
        try:
            refresh_token = request.data["refresh"]
            token = RefreshToken(refresh_token)
            token.blacklist()
            return Response({"message": "Successfully logged out"}, status=status.HTTP_200_OK)
        except Exception as e:
            return Response({"error": str(e)}, status=status.HTTP_400_BAD_REQUEST)
