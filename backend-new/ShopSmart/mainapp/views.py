import os
import random

from dotenv import load_dotenv
from twilio.rest import Client
from django.shortcuts import get_object_or_404
from rest_framework.decorators import permission_classes
from rest_framework.permissions import AllowAny
from rest_framework import generics, status
from rest_framework.permissions import IsAuthenticated
from rest_framework.response import Response
from rest_framework.views import APIView
from rest_framework_simplejwt.tokens import RefreshToken
from django.contrib.auth import get_user_model

from .models import PhoneOTP, Product, Shop
from .permissions import IsOwnerOfShop
from .serializers import SendOTPSerializer, ProductSerializer, VerifyOTPSerializer

load_dotenv()


def send_otp(phone):
    otp = str(random.randint(1000, 9999))

    account_sid = os.getenv("TWILIO_ACCOUNT_SID")
    auth_token = os.getenv("TWILIO_AUTH_TOKEN")
    from_phone = os.getenv("TWILIO_PHONE_NUMBER")

    if account_sid and auth_token and from_phone:
        try:
            client = Client(account_sid, auth_token)
            message = client.messages.create(
                body=f"Your OTP is {otp}",
                from_=from_phone,
                to=phone
            )
            print(f"OTP sent to {phone} via Twilio: SID={message.sid}")
        except Exception as e:
            print(f"Failed to send OTP via Twilio: {e}")
    else:
        print(f"[DEV MODE] OTP for {phone}: {otp}")

    return otp

@permission_classes([AllowAny])
class SendOTPView(APIView):
    def post(self, request):
        serializer = SendOTPSerializer(data=request.data)
        if serializer.is_valid():
            phone = serializer.validated_data['phone']

            otp = send_otp(phone)

            # Update or create OTP entry
            phone_obj, created = PhoneOTP.objects.update_or_create(
                phone_number=phone,
                defaults={'otp_code': otp, 'is_verified': False}
            )

            return Response({'message': 'OTP sent successfully'}, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


User = get_user_model()

@permission_classes([AllowAny])
class VerifyOTPView(APIView):
    def post(self, request):
        serializer = VerifyOTPSerializer(data=request.data)
        if serializer.is_valid():
            phone = serializer.validated_data['phone']
            otp = serializer.validated_data['otp']

            try:
                phone_obj = PhoneOTP.objects.get(phone_number=phone)
            except PhoneOTP.DoesNotExist:
                return Response({'error': 'Phone number not found'}, status=status.HTTP_404_NOT_FOUND)

            if phone_obj.is_expired():
                return Response({'error': 'OTP has expired. Please request a new one.'},
                                status=status.HTTP_400_BAD_REQUEST)

            if phone_obj.otp_code == otp:
                phone_obj.is_verified = True
                phone_obj.save()
                user, created = User.objects.get_or_create(phone_number=phone, defaults={'username': phone})
                refresh = RefreshToken.for_user(user)
                access_token = str(refresh.access_token)
                
                return Response({
                    'message': 'Phone number verified successfully',
                    'access': access_token,
                    'refresh': str(refresh),
                    'is_new_user': created
                }, status=status.HTTP_200_OK)

            return Response({'error': 'Invalid OTP'}, status=status.HTTP_400_BAD_REQUEST)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


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
