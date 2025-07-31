from django.shortcuts import render
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from .models import PhoneOTP
from .serializers import PhoneSerializer, VerifyOTPSerializer
from django.utils import timezone
from dotenv import load_dotenv
from twilio.rest import Client
import os
import random

# Load environment variables
load_dotenv()


# Utility function to send OTP using Twilio (fallback to print if env vars not set)
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


class SendOTPView(APIView):
    def post(self, request):
        serializer = PhoneSerializer(data=request.data)
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
                return Response({'message': 'Phone number verified successfully'},
                                status=status.HTTP_200_OK)
            else:
                return Response({'error': 'Invalid OTP'}, status=status.HTTP_400_BAD_REQUEST)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
