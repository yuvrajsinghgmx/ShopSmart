from django.db import models
from django.core.validators import MinValueValidator
from django.core.exceptions import ValidationError

from .choices import PositionLevel, PlanType


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
            