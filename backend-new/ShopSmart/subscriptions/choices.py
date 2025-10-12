from django.db import models

class PlanType(models.TextChoices):
    SHOP_POSITION = 'SHOP_POSITION', 'Shop Position'
    PRODUCT_POSITION = 'PRODUCT_POSITION', 'Product Position'
    BANNER = 'BANNER', 'Banner'

class PositionLevel(models.IntegerChoices):
    FEATURED = 2, 'Featured (Level 2)'
    SPONSORED = 3, 'Sponsored (Level 3)'
