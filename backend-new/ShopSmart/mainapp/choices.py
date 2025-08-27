from django.db import models


class ShopTypes(models.TextChoices):
    GROCERY = "grocery", "Grocery"
    CLOTHING = "clothing", "Clothing"
    ELECTRONICS = "electronics", "Electronics"


class ProductTypes(models.TextChoices):
    VEGETABLE = "vegetable", "Vegetable"
    FRUIT = "fruit", "Fruit"
    CLOTHES = "clothes", "Clothes"
