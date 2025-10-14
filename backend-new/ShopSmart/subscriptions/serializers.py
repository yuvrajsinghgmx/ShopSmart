from rest_framework import serializers
from django.utils import timezone
from mainapp.models import Shop, Product
from .models import SubscriptionPlan, ActiveSubscription
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


class SubscriptionPurchaseSerializer(serializers.Serializer):
    plan_id = serializers.IntegerField(write_only=True)
    item_type = serializers.ChoiceField(choices=['shop', 'product'], write_only=True)
    item_id = serializers.IntegerField(write_only=True)

    def validate(self, data):
        request = self.context['request']
        user = request.user
        
        try:
            plan = SubscriptionPlan.objects.get(id=data['plan_id'], is_active=True)
        except SubscriptionPlan.DoesNotExist:
            raise serializers.ValidationError({"plan_id": "The selected plan is not valid or active."})
            
        item_type = data['item_type']
        item_id = data['item_id']
        item = None
        
        if item_type == 'shop':
            try:
                item = Shop.objects.get(id=item_id, owner=user, is_approved=True)
            except Shop.DoesNotExist:
                raise serializers.ValidationError({"item_id": "Approved shop not found or you are not the owner."})
            if plan.plan_type != PlanType.SHOP_POSITION:
                raise serializers.ValidationError({"plan_id": "This plan is not applicable to shops."})

        elif item_type == 'product':
            try:
                item = Product.objects.get(id=item_id, shop__owner=user, shop__is_approved=True)
            except Product.DoesNotExist:
                raise serializers.ValidationError({"item_id": "Product not found in an approved shop or you are not the owner."})
            if plan.plan_type != PlanType.PRODUCT_POSITION:
                raise serializers.ValidationError({"plan_id": "This plan is not applicable to products."})
        
        if ActiveSubscription.objects.filter(object_id=item.id, content_type__model=item_type, 
                                            plan__position_level=plan.position_level, 
                                            end_date__gt=timezone.now(), is_active=True
        ).exists():
            raise serializers.ValidationError(
                f"This {item_type} already has an active subscription for this position level."
            )

        data['plan'] = plan
        data['item'] = item
        return data


class ActiveSubscriptionSerializer(serializers.ModelSerializer):
    plan = SubscriptionPlanSerializer(read_only=True)
    item_details = serializers.SerializerMethodField()

    class Meta:
        model = ActiveSubscription
        fields = ['id', 'plan', 'item_details', 'start_date', 'end_date', 'is_active']
        
    def get_item_details(self, obj):
        if isinstance(obj.content_object, Shop):
            return {
                "type": "shop",
                "id": obj.content_object.id,
                "name": obj.content_object.name,
                "position": obj.content_object.position
            }
        if isinstance(obj.content_object, Product):
            return {
                "type": "product",
                "id": obj.content_object.id,
                "name": obj.content_object.name,
                "position": obj.content_object.position
            }
        return None