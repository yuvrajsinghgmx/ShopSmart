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


class ProductTypes(models.TextChoices):
    VEGETABLE = "vegetable", "Vegetable"
    FRUIT = "fruit", "Fruit"
    CLOTHES = "clothes", "Clothes"
    ELECTRONICS = "electronics", "Electronics"
