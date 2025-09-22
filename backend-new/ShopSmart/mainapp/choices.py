from django.db import models
from django.utils.translation import gettext_lazy as _

class Role(models.TextChoices):
    CUSTOMER = "CUSTOMER", _("Customer")
    SHOP_OWNER = "SHOP_OWNER", _("Shop Owner")
    ADMIN = "ADMIN", _("Admin")

class ShopTypes(models.TextChoices):
    GROCERY = "grocery", "Grocery"
    CLOTHING = "clothing", "Clothing"
    ELECTRONICS = "electronics", "Electronics"
    FOOD_DELIVERY = "food_delivery", "Food & Delivery"
    BEAUTY = "beauty", "Beauty & Personal Care"
    HOME = "home", "Home & Furniture"
    PHARMACY = "pharmacy", "Pharmacy & Wellness"
    SPORTS = "sports", "Sports & Fitness"


class ProductTypes(models.TextChoices):
    VEGETABLE = "vegetable", "Vegetable"
    FRUIT = "fruit", "Fruit"
    CLOTHES = "clothes", "Clothes"
    ELECTRONICS = "electronics", "Electronics"
    FASTFOOD = "fastfood", "Fast Food"
    BEAUTY = "beauty", "Beauty Product"
    MEDICINE = "medicine", "Medicine"
    HOME_APPLIANCE = "home_appliance", "Home Appliance"