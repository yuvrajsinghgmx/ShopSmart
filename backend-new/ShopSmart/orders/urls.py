from django.urls import path
from . import views

urlpatterns = [
    # Cart endpoints
    path('cart/', views.ManageCartView.as_view(), name='manage-cart'),

    # Customer endpoints
    path('create/', views.CreateOrderView.as_view(), name='create-order'),
    path('', views.CustomerOrderListView.as_view(), name='customer-order-list'),
    path('<int:pk>/', views.CustomerOrderDetailView.as_view(), name='customer-order-detail'),

    # Shop Owner endpoints
    path('shop-owner/', views.ShopOwnerOrderListView.as_view(), name='shop-owner-order-list'),
    path('shop-owner/<int:pk>/', views.ShopOwnerOrderDetailView.as_view(), name='shop-owner-order-detail'),
    path('shop-owner/delivery-boys/', views.ShopOwnerManageDeliveryBoysView.as_view(), name='shop-owner-manage-delivery-boys'),
    path('shop-owner/delivery-boys/<int:pk>/', views.ShopOwnerUpdateDeliveryBoyView.as_view(), name='shop-owner-update-delivery-boy'),
    
    # Delivery Boy endpoints
    path('delivery/', views.DeliveryOrderListView.as_view(), name='delivery-order-list'),
    path('delivery/<int:pk>/update-status/', views.DeliveryUpdateOrderStatusView.as_view(), name='delivery-update-status'),
]