from rest_framework import serializers
from .models import SubscriptionPlan
from .choices import PlanType

class SubscriptionPlanSerializer(serializers.ModelSerializer):
    """
    Serializer for the SubscriptionPlan model, admins CRUD operations.
    """
    class Meta:
        model = SubscriptionPlan
        fields = ['id', 'name', 'plan_type', 'position_level', 'price', 'duration_days', 
                'is_active', 'created_at', 'updated_at',]
        read_only_fields = ['id', 'created_at', 'updated_at']

    def validate(self, data):
        """
        Custom validation to ensure position_level is handled correctly based on plan_type.
        """
        plan_type = data.get('plan_type', getattr(self.instance, 'plan_type', None))
        position_level = data.get('position_level', getattr(self.instance, 'position_level', None))

        if plan_type in [PlanType.SHOP_POSITION, PlanType.PRODUCT_POSITION]:
            if not position_level:
                raise serializers.ValidationError({"position_level": "This field is required for Shop or Product position plans."})
        
        if plan_type == PlanType.BANNER:
            if 'position_level' in data:
                data['position_level'] = None
        
        return data