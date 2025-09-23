import uuid
from rest_framework import generics, status, views
from rest_framework.response import Response
from rest_framework.permissions import IsAuthenticated
from django.shortcuts import get_object_or_404
from django.db import transaction

from .models import Cart, CartItem, Order, ShopOrder, OrderItem, Product
from .serializers import (
    CartSerializer, CreateOrderSerializer, OrderSerializer, 
    ShopOwnerOrderSerializer, DeliveryOrderSerializer, DeliveryStatusUpdateSerializer
)
from .permissions import IsShopOwnerRole, IsDeliveryBoyRole
from .serializers import DeliveryBoyCreateSerializer, DeliveryBoyListSerializer
from mainapp.choices import Role
from django.contrib.auth import get_user_model
User = get_user_model()

# --- Cart Views ---
class ManageCartView(views.APIView):
    permission_classes = [IsAuthenticated]

    def get(self, request, *args, **kwargs):
        """View the user's cart"""
        cart, created = Cart.objects.get_or_create(user=request.user)
        serializer = CartSerializer(cart)
        return Response(serializer.data)

    def post(self, request, *args, **kwargs):
        """Add or update an item in the cart"""
        product_id = request.data.get('product_id')
        quantity = int(request.data.get('quantity', 1))
        
        if not product_id or quantity <= 0:
            return Response({"error": "Valid product_id and quantity are required."}, status=status.HTTP_400_BAD_REQUEST)

        product = get_object_or_404(Product, id=product_id)
        cart, created = Cart.objects.get_or_create(user=request.user)
        cart_item, item_created = CartItem.objects.get_or_create(cart=cart, product=product)
        
        cart_item.quantity = quantity
        cart_item.save()
        
        serializer = CartSerializer(cart)
        return Response(serializer.data, status=status.HTTP_200_OK)

    def delete(self, request, *args, **kwargs):
        """Remove an item from the cart"""
        product_id = request.data.get('product_id')
        if not product_id:
            return Response({"error": "product_id is required."}, status=status.HTTP_400_BAD_REQUEST)

        cart = get_object_or_404(Cart, user=request.user)
        get_object_or_404(CartItem, cart=cart, product_id=product_id).delete()
        
        return Response(status=status.HTTP_204_NO_CONTENT)

# --- Order Creation View ---
class CreateOrderView(generics.CreateAPIView):
    permission_classes = [IsAuthenticated]
    serializer_class = CreateOrderSerializer

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        
        shop_id = serializer.validated_data['shop_id']
        cart = request.user.cart
        
        cart_items = cart.items.filter(product__shop_id=shop_id).select_related('product__shop')

        if not cart_items:
            return Response({"error": f"No items from shop ID {shop_id} in cart."}, status=status.HTTP_400_BAD_REQUEST)

        with transaction.atomic():
            shop = get_object_or_404(Shop, id=shop_id)
            
            subtotal = sum(item.product.price * item.quantity for item in cart_items)
            tax_amount = subtotal * (shop.tax_percent / 100)
            total_amount = subtotal + tax_amount

            # Create the main Order record
            order = Order.objects.create(
                customer=request.user,
                total_amount=total_amount,
                shipping_address=serializer.validated_data['shipping_address'],
                payment_method=serializer.validated_data['payment_method']
            )

            # Create the single ShopOrder for this transaction
            shop_order = ShopOrder.objects.create(
                order=order,
                shop=shop,
                subtotal_amount=subtotal,
                tax_amount=tax_amount
            )
            
            # Create OrderItems for this ShopOrder and remove them from the cart
            for item in cart_items:
                OrderItem.objects.create(
                    shop_order=shop_order,
                    product=item.product,
                    product_name=item.product.name,
                    price_at_purchase=item.product.price,
                    quantity=item.quantity
                )
            
            # Delete only the ordered items from the cart
            cart_items.delete()
        
        return Response({
            "success": "Order placed successfully!", 
            "order_id": order.order_id,
            "shop_order_id": shop_order.shop_order_id
        }, status=status.HTTP_201_CREATED)
    

# --- Customer Order Views ---
class CustomerOrderListView(generics.ListAPIView):
    permission_classes = [IsAuthenticated]
    serializer_class = OrderSerializer

    def get_queryset(self):
        return Order.objects.filter(customer=self.request.user).order_by('-created_at')

class CustomerOrderDetailView(generics.RetrieveAPIView):
    permission_classes = [IsAuthenticated]
    serializer_class = OrderSerializer
    
    def get_queryset(self):
        return Order.objects.filter(customer=self.request.user)

# --- Shop Owner Views ---
class ShopOwnerOrderListView(generics.ListAPIView):
    permission_classes = [IsAuthenticated, IsShopOwnerRole]
    serializer_class = ShopOwnerOrderSerializer
    
    def get_queryset(self):
        return ShopOrder.objects.filter(shop__owner=self.request.user).order_by('-created_at')

class ShopOwnerOrderDetailView(generics.RetrieveUpdateAPIView):
    permission_classes = [IsAuthenticated, IsShopOwnerRole]
    serializer_class = ShopOwnerOrderSerializer
    
    def get_queryset(self):
        return ShopOrder.objects.filter(shop__owner=self.request.user)
    

class ShopOwnerManageDeliveryBoysView(generics.ListCreateAPIView):
    permission_classes = [IsAuthenticated, IsShopOwnerRole]
    
    def get_serializer_class(self):
        if self.request.method == 'POST':
            return DeliveryBoyCreateSerializer
        return DeliveryBoyListSerializer

    def get_queryset(self):
        # Return only the delivery boys managed by the current shop owner
        return User.objects.filter(managed_by=self.request.user, role=Role.DELIVERY_BOY)

    def perform_create(self, serializer):
        # When creating, set the role and link to the shop owner
        # A random unusable password is set as they will login via OTP
        user = serializer.save(
            role=Role.DELIVERY_BOY,
            managed_by=self.request.user,
            username=f"user_{uuid.uuid4().hex[:8]}" # Generate a unique username
        )
        user.set_unusable_password()
        user.save()

class ShopOwnerUpdateDeliveryBoyView(generics.RetrieveUpdateDestroyAPIView):
    permission_classes = [IsAuthenticated, IsShopOwnerRole]
    serializer_class = DeliveryBoyListSerializer
    
    def get_queryset(self):
        # A shop owner can only manage their own delivery boys
        return User.objects.filter(managed_by=self.request.user, role=Role.DELIVERY_BOY)

    def perform_destroy(self, instance):
        # Instead of deleting, we deactivate the user. This preserves records.
        instance.is_active = False
        instance.save()

# --- Delivery Boy Views ---
class DeliveryOrderListView(generics.ListAPIView):
    permission_classes = [IsAuthenticated, IsDeliveryBoyRole]
    serializer_class = DeliveryOrderSerializer
    
    def get_queryset(self):
        return ShopOrder.objects.filter(delivery_boy=self.request.user, status=ShopOrder.OrderStatus.OUT_FOR_DELIVERY).order_by('created_at')

class DeliveryUpdateOrderStatusView(generics.UpdateAPIView):
    permission_classes = [IsAuthenticated, IsDeliveryBoyRole]
    serializer_class = DeliveryStatusUpdateSerializer
    
    def get_queryset(self):
        return ShopOrder.objects.filter(delivery_boy=self.request.user)