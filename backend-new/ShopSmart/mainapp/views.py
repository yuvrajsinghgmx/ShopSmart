from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import PhoneOTP
from .serializers import PhoneSerializer, VerifyOTPSerializer
import random


# Utility function to send OTP (you can integrate Twilio or other APIs later)
def send_otp(phone):
    otp = random.randint(1000, 9999)
    print(f"Generated OTP for {phone}: {otp}")  # Replace with actual sending logic
    return otp


class SendOTPView(APIView):
    def post(self, request):
        serializer = PhoneSerializer(data=request.data)
        if serializer.is_valid():
            phone = serializer.validated_data['phone']

            otp = send_otp(phone)

            # Update or create OTP entry
            phone_obj, created = PhoneOTP.objects.update_or_create(
                phone=phone,
                defaults={'otp': otp, 'is_verified': False}
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
                return Response({'message': 'Phone number verified'}, status=status.HTTP_200_OK)
            else:
                return Response({'error': 'Invalid OTP'}, status=status.HTTP_400_BAD_REQUEST)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
