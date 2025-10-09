from django.contrib import admin
from .models import SubscriptionPlan

@admin.register(SubscriptionPlan)
class SubscriptionPlanAdmin(admin.ModelAdmin):
    """
    Admin interface options for SubscriptionPlan model.
    """
    list_display = ('name', 'plan_type', 'position_level', 'price', 'duration_days', 'is_active', 'created_at')
    list_filter = ('plan_type', 'position_level', 'is_active')
    search_fields = ('name',)
    ordering = ('price',)
    fieldsets = (
        (None, {'fields': ('name', 'is_active')}),
        ('Plan Details', {'fields': ('plan_type', 'position_level', 'price', 'duration_days')}),
    )
    