from django.contrib.auth.models import BaseUserManager
from django.utils.translation import gettext_lazy as _
import uuid

class CustomUserManager(BaseUserManager):
    """
    Custom user model manager where email is the unique identifier
    for authentication.
    """
    def create_user(self, email, password, **extra_fields):
        """
        Create and save a User with the given email and password.
        """
        if not email:
            raise ValueError(_('The Email must be set'))
        
        email = self.normalize_email(email)

        # The username is required by AbstractUser, generate a unique one if not provided.
        if 'username' not in extra_fields or not extra_fields['username']:
            extra_fields['username'] = str(uuid.uuid4())

        user = self.model(email=email, **extra_fields)
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, email, password, **extra_fields):
        """
        Create and save a SuperUser with the given email and password.
        """
        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', True)
        extra_fields.setdefault('is_active', True)
        extra_fields.setdefault('role', 'ADMIN')

        if extra_fields.get('is_staff') is not True:
            raise ValueError(_('Superuser must have is_staff=True.'))
        if extra_fields.get('is_superuser') is not True:
            raise ValueError(_('Superuser must have is_superuser=True.'))

        # For superusers created via command line, phone_number is not required.
        extra_fields.setdefault('phone_number', None)

        return self.create_user(email, password, **extra_fields)