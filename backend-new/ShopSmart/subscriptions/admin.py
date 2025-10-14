from django.contrib import admin
from .models import SubscriptionPlan, ActiveSubscription

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
    
@admin.register(ActiveSubscription)
class ActiveSubscriptionAdmin(admin.ModelAdmin):
    """
    Admin interface for managing active user subscriptions.
    """
    list_display = ('id', 'user', 'plan', 'content_object', 'end_date', 'is_active', 'is_expired_status')
    list_filter = ('is_active', 'plan__plan_type', 'end_date')
    search_fields = ('user__username', 'plan__name')
    readonly_fields = ('content_object', 'start_date', 'created_at', 'updated_at')
    list_select_related = ('user', 'plan')
    ordering = ('-end_date',)

    @admin.display(boolean=True, description='Expired?')
    def is_expired_status(self, obj):
        return obj.is_expired