from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import PhoneOTP, User
from .serializers import PhoneSerializer, VerifyOTPSerializer
import random
from django.utils import timezone
from datetime import timedelta
from rest_framework_simplejwt.tokens import RefreshToken


# Utility function to send OTP (you can integrate Twilio or other APIs later)
def send_otp(phone):
    otp = str(random.randint(1000, 9999))
    print(f"Generated OTP for {phone}: {otp}")  # Replace with actual sending logic (e.g., Twilio)
    return otp


def get_tokens_for_user(user):
    refresh = RefreshToken.for_user(user)
    return {
        'refresh': str(refresh),
        'access': str(refresh.access_token),
    }


class SendOTPView(APIView):
    def post(self, request):
        serializer = PhoneSerializer(data=request.data)
        if serializer.is_valid():
            phone = serializer.validated_data['phone']

            otp = send_otp(phone)

            # Check if OTP was sent too recently
            phone_obj = PhoneOTP.objects.filter(phone=phone).first()
            if phone_obj and timezone.now() - phone_obj.created_at < timedelta(seconds=60):
                return Response({'error': 'Please wait before requesting a new OTP.'},
                                status=status.HTTP_429_TOO_MANY_REQUESTS)

            # Update or create OTP entry
            PhoneOTP.objects.update_or_create(
                phone=phone,
                defaults={'otp': otp, 'is_verified': False, 'created_at': timezone.now()}
            )

            return Response({'message': 'OTP sent successfully'}, status=status.HTTP_200_OK)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)


class VerifyOTPView(APIView):
    def post(self, request):
        serializer = VerifyOTPSerializer(data=request.data)
        if serializer.is_valid():
            phone = serializer.validated_data['phone']
            otp = serializer.validated_data['otp']

            try:
                phone_obj = PhoneOTP.objects.get(phone=phone)
            except PhoneOTP.DoesNotExist:
                return Response({'error': 'Phone number not found'}, status=status.HTTP_404_NOT_FOUND)

            if phone_obj.otp == otp:
                phone_obj.is_verified = True
                phone_obj.save()

                # Create or get user
                user, created = User.objects.get_or_create(phone_number=phone, defaults={
                    "username": phone
                })

                # Generate tokens
                tokens = get_tokens_for_user(user)

                return Response({
                    'message': 'Phone number verified',
                    'user_id': user.id,
                    'tokens': tokens
                }, status=status.HTTP_200_OK)

            return Response({'error': 'Invalid OTP'}, status=status.HTTP_400_BAD_REQUEST)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
