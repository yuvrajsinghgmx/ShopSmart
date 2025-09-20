from django.contrib.auth import get_user_model
from rest_framework import status, permissions
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework_simplejwt.tokens import RefreshToken
from drf_spectacular.utils import extend_schema
from .serializers import AdminLoginSerializer, AdminAuthResponseSerializer
from .choices import Role

User = get_user_model()

class AdminLoginView(APIView):
    permission_classes = [permissions.AllowAny]

    @extend_schema(
        request=AdminLoginSerializer,
        responses={200: AdminAuthResponseSerializer}
    )
    def post(self, request):
        """
        Authenticates an admin user with email and password, returning JWT tokens.
        """
        serializer = AdminLoginSerializer(data=request.data)
        if not serializer.is_valid():
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        email = serializer.validated_data['email']
        password = serializer.validated_data['password']

        try:
            user = User.objects.get(email__iexact=email)
        except User.DoesNotExist:
            return Response({'error': 'Invalid credentials.'}, status=status.HTTP_401_UNAUTHORIZED)
        
        if user.check_password(password):
            if not (user.is_staff or user.role == Role.ADMIN):
                return Response({'error': 'You do not have permission to access the admin panel.'}, status=status.HTTP_403_FORBIDDEN)

            refresh = RefreshToken.for_user(user)
            response_data = {
                'access': str(refresh.access_token),
                'refresh': str(refresh),
                'user': {
                    'id': user.id,
                    'email': user.email,
                    'name': user.full_name or user.username,
                    'role': user.role
                }
            }
            return Response(response_data, status=status.HTTP_200_OK)
        else:
            return Response({'error': 'Invalid credentials.'}, status=status.HTTP_401_UNAUTHORIZED)