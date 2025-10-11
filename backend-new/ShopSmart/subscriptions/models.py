from django.db import models
from django.utils import timezone
from datetime import timedelta
from django.core.validators import MinValueValidator
from django.core.exceptions import ValidationError
from django.conf import settings
from django.contrib.contenttypes.fields import GenericForeignKey
from django.contrib.contenttypes.models import ContentType

from .choices import PositionLevel, PlanType

User = settings.AUTH_USER_MODEL


class SubscriptionPlan(models.Model):
    name = models.CharField(max_length=100, unique=True)
    plan_type = models.CharField(max_length=20, choices=PlanType.choices)
    position_level = models.PositiveSmallIntegerField(choices=PositionLevel.choices, null=True, blank=True)
    price = models.DecimalField(max_digits=10, decimal_places=2, validators=[MinValueValidator(0)])
    duration_days = models.PositiveIntegerField(validators=[MinValueValidator(1)])
    is_active = models.BooleanField(default=True)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.name} ({self.get_plan_type_display()})"

    def clean(self):
        # To make sure shop/product position plan also have position when creating.
        if self.plan_type in [PlanType.SHOP_POSITION, PlanType.PRODUCT_POSITION] and self.position_level is None:
            raise ValidationError("Position level is required for Shop and Product position plans.")
        # To make sure banner plan dont have position when creating.
        if self.plan_type == PlanType.BANNER and self.position_level is not None:
            self.position_level = None


class ActiveSubscription(models.Model):
    user = models.ForeignKey(User, on_delete=models.CASCADE, related_name="subscriptions")
    plan = models.ForeignKey(SubscriptionPlan, on_delete=models.PROTECT, related_name="activations")
    
    content_type = models.ForeignKey(ContentType, on_delete=models.CASCADE)
    object_id = models.PositiveIntegerField()
    content_object = GenericForeignKey('content_type', 'object_id') # GenericForeignKey to link to either a Shop or a Product
    
    start_date = models.DateTimeField(auto_now_add=True)
    end_date = models.DateTimeField()
    is_active = models.BooleanField(default=True)

    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)

    def __str__(self):
        return f"{self.user.username}'s {self.plan.name} plan for {self.content_object}"

    def save(self, *args, **kwargs):
        if not self.pk: # i.e., when creating
            self.end_date = timezone.now() + timedelta(days=self.plan.duration_days)
        super().save(*args, **kwargs)

    @property
    def is_expired(self):
        return self.end_date < timezone.now()