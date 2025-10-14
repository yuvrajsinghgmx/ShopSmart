from rest_framework import serializers,validators

from .models import Cart, CartItem, Order, ShopOrder, OrderItem
from mainapp.models import Product, Shop
from django.contrib.auth import get_user_model
User = get_user_model()

class CartItemSerializer(serializers.ModelSerializer):
    product_name = serializers.CharField(source='product.name', read_only=True)
    shop_id = serializers.IntegerField(source='product.shop.id', read_only=True)
    price = serializers.DecimalField(source='product.price', max_digits=10, decimal_places=2, read_only=True)
    shop_name = serializers.CharField(source='product.shop.name', read_only=True)
    class Meta:
        model = CartItem
        fields = ['id', 'product', 'product_name', 'price', 'quantity', 'shop_id', 'shop_name']

class CartSerializer(serializers.ModelSerializer):
    items = CartItemSerializer(many=True, read_only=True)
    total_cart_value = serializers.SerializerMethodField()

    class Meta:
        model = Cart
        fields = ['id', 'user', 'items', 'total_cart_value']
    
    def get_total_cart_value(self, obj):
        return sum(item.product.price * item.quantity for item in obj.items.all())

class OrderItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = OrderItem
        fields = ['product_name', 'price_at_purchase', 'quantity']

class ShopOrderSerializer(serializers.ModelSerializer):
    items = OrderItemSerializer(many=True, read_only=True)
    shop_name = serializers.CharField(source='shop.name', read_only=True)

    class Meta:
        model = ShopOrder
        fields = ['shop_order_id', 'shop_name', 'subtotal_amount', 'status', 'items']

class OrderSerializer(serializers.ModelSerializer):
    shop_orders = ShopOrderSerializer(many=True, read_only=True)

    class Meta:
        model = Order
        fields = ['order_id', 'total_amount', 'shipping_address', 'payment_status', 'payment_method', 'created_at', 'shop_orders']

class CreateOrderSerializer(serializers.Serializer):
    shop_id = serializers.IntegerField(write_only=True)
    payment_method = serializers.ChoiceField(choices=Order.PaymentMethod.choices)
    shipping_address = serializers.CharField()

    def validate_shop_id(self, value):
        cart = self.context['request'].user.cart
        if not cart.items.filter(product__shop_id=value).exists():
            raise serializers.ValidationError("You have no items from this shop in your cart.")
        return value


# Serializer for Shop Owners
class ShopOwnerOrderSerializer(serializers.ModelSerializer):
    items = OrderItemSerializer(many=True, read_only=True)
    customer_name = serializers.CharField(source='order.customer.full_name', read_only=True)
    shipping_address = serializers.CharField(source='order.shipping_address', read_only=True)
    
    class Meta:
        model = ShopOrder
        fields = ['shop_order_id', 'status', 'subtotal_amount', 'tax_amount', 'items', 'customer_name', 'shipping_address', 'delivery_boy']
        read_only_fields = ['subtotal_amount', 'tax_amount', 'items', 'customer_name', 'shipping_address']

    def validate_delivery_boy(self, value):
        if value:
            shop_owner = self.context['request'].user
            if value.managed_by != shop_owner or not value.is_active:
                raise serializers.ValidationError("You can only assign an active delivery boy that you manage.")
        return value

    def validate(self, data):
        if 'status' in data:
            new_status = data['status']
            current_status = self.instance.status
            
            allowed_transitions = {
                ShopOrder.OrderStatus.PENDING: [ShopOrder.OrderStatus.ACCEPTED, ShopOrder.OrderStatus.CANCELLED],
                ShopOrder.OrderStatus.ACCEPTED: [ShopOrder.OrderStatus.PREPARING,ShopOrder.OrderStatus.DELIVERED, ShopOrder.OrderStatus.CANCELLED],
                ShopOrder.OrderStatus.PREPARING: [ShopOrder.OrderStatus.OUT_FOR_DELIVERY,ShopOrder.OrderStatus.DELIVERED]
            }
            
            if current_status not in allowed_transitions or new_status not in allowed_transitions[current_status]:
                raise serializers.ValidationError(f"Cannot change status from '{current_status}' to '{new_status}'.")
            
  
        return data
    

class DeliveryBoyCreateSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['full_name', 'phone_number', 'current_address', 'profile_image']
        extra_kwargs = {
            'phone_number': {'validators': [validators.UniqueValidator(queryset=User.objects.all())]} 
        }

# Serializer to display the list of delivery boys to the shop owner
class DeliveryBoyListSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['id', 'full_name', 'phone_number', 'current_address', 'profile_image', 'is_active']

# Serializer for Delivery Boys
class DeliveryOrderSerializer(serializers.ModelSerializer):
    items = OrderItemSerializer(many=True, read_only=True)
    customer_name = serializers.CharField(source='order.customer.full_name', read_only=True)
    customer_phone = serializers.CharField(source='order.customer.phone_number', read_only=True)
    shipping_address = serializers.CharField(source='order.shipping_address', read_only=True)
    shop_name = serializers.CharField(source='shop.name', read_only=True)
    shop_address = serializers.CharField(source='shop.address', read_only=True)

    class Meta:
        model = ShopOrder
        fields = ['shop_order_id', 'status', 'shop_name', 'shop_address', 'items', 'customer_name', 'customer_phone', 'shipping_address']

class DeliveryStatusUpdateSerializer(serializers.ModelSerializer):
    class Meta:
        model = ShopOrder
        fields = ['status']

    def validate_status(self, value):
        if self.instance.status != ShopOrder.OrderStatus.OUT_FOR_DELIVERY:
            raise serializers.ValidationError("Can only update status from 'Out for Delivery'.")
        if value != ShopOrder.OrderStatus.DELIVERED:
            raise serializers.ValidationError("Status can only be updated to 'Delivered'.")
        return value
    
