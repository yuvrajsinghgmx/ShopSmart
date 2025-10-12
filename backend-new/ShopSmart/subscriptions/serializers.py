from rest_framework import serializers
from django.utils import timezone
from mainapp.models import Shop, Product
from .models import SubscriptionPlan, ActiveSubscription, Banner
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
    banner_image = serializers.ImageField(write_only=True, required=False)

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

        if plan.plan_type == PlanType.BANNER:
            if item_type != 'shop':
                raise serializers.ValidationError({"item_type": "Banner plans can only be applied to shops."})
            if 'banner_image' not in data:
                raise serializers.ValidationError({"banner_image": "An image is required for a banner subscription."})
            
            try:
                item = Shop.objects.get(id=item_id, owner=user, is_approved=True)
            except Shop.DoesNotExist:
                raise serializers.ValidationError({"item_id": "Approved shop not found or you are not the owner."})
            
            if Banner.objects.filter(shop=item, is_active=True, end_date__gt=timezone.now()).exists():
                raise serializers.ValidationError({"item_id": "This shop already has an active banner."})
        
        elif plan.plan_type == PlanType.SHOP_POSITION:
            if item_type != 'shop':
                raise serializers.ValidationError({"item_type": "This plan requires a 'shop' item type."})
            try:
                item = Shop.objects.get(id=item_id, owner=user, is_approved=True)
            except Shop.DoesNotExist:
                raise serializers.ValidationError({"item_id": "Approved shop not found or you are not the owner."})
            if ActiveSubscription.objects.filter(object_id=item.id, content_type__model='shop', plan__position_level=plan.position_level, end_date__gt=timezone.now(), is_active=True).exists():
                raise serializers.ValidationError("This shop already has an active subscription for this position level.")

        elif plan.plan_type == PlanType.PRODUCT_POSITION:
            if item_type != 'product':
                raise serializers.ValidationError({"item_type": "This plan requires a 'product' item type."})
            try:
                item = Product.objects.get(id=item_id, shop__owner=user, shop__is_approved=True)
            except Product.DoesNotExist:
                raise serializers.ValidationError({"item_id": "Product not found in an approved shop or you are not the owner."})
            if ActiveSubscription.objects.filter(object_id=item.id, content_type__model='product', plan__position_level=plan.position_level, end_date__gt=timezone.now(), is_active=True).exists():
                raise serializers.ValidationError("This product already has an active subscription for this position level.")
        
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


class BannerSerializer(serializers.ModelSerializer):
    shop_id = serializers.IntegerField(source='shop.id', read_only=True)
    shop_name = serializers.CharField(source='shop.name', read_only=True)

    class Meta:
        model = Banner
        fields = ['id', 'image_url', 'shop_id', 'shop_name']