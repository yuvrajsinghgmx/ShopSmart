package com.yuvrajsinghgmx.shopsmart.modelclass.repository

import com.yuvrajsinghgmx.shopsmart.modelclass.Product
import com.yuvrajsinghgmx.shopsmart.modelclass.Shop
import com.yuvrajsinghgmx.shopsmart.modelclass.User
import java.util.UUID
import javax.inject.Inject

class Repository @Inject constructor(){
    fun getProductList(): List<Product> {
        return listOf(
            Product(
                productId = 1,
                name = "Premium Leather Tote Bag",
                price = "$89.99",
                category = "Bags",
                review = "4.8",
                description = "Elegant leather tote bag for all occasions.",
                imageUrl = listOf("https://images.unsplash.com/photo-1512436991641-6745cdb1723f?auto=format&fit=crop&w=400&q=80"),
                shopName = "Urban Leather Co.",
                shopNumber = "101",
                distance = "2.1 km"
            ),
            Product(
                productId = 2,
                name = "Classic Leather Backpack",
                price = "$129.99",
                category = "Bags",
                review = "4.6",
                description = "Spacious, stylish backpack in genuine leather.",
                imageUrl = listOf("https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80"),
                shopName = "Leather Workshop",
                shopNumber = "102",
                distance = "3.4 km"
            ),
            Product(
                productId = 3,
                name = "Handcrafted Leather Wallet",
                price = "$49.99",
                category = "Accessories",
                review = "4.7",
                description = "Durable and classic handcrafted wallet.",
                imageUrl = listOf("https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80"),
                shopName = "Artisan Leather",
                shopNumber = "103",
                distance = "4.2 km"
            ),
            Product(
                productId = 4,
                name = "Wireless Earbuds",
                price = "$59.99",
                category = "Electronics",
                review = "4.5",
                description = "Noise-cancelling wireless earbuds with long battery life.",
                imageUrl = listOf("https://images.unsplash.com/photo-1580910051076-7b8375b3b14e?auto=format&fit=crop&w=400&q=80"),
                shopName = "ElectroMart",
                shopNumber = "104",
                distance = "1.7 km"
            ),
            Product(
                productId = 5,
                name = "Fresh Gala Apples (1kg)",
                price = "$3.99",
                category = "Groceries",
                review = "4.9",
                description = "Crisp, juicy apples directly from organic farms.",
                imageUrl = listOf("https://images.unsplash.com/photo-1502741338009-cac2772e18bc?auto=format&fit=crop&w=400&q=80"),
                shopName = "Fresh Market",
                shopNumber = "105",
                distance = "2.4 km"
            ),
            Product(
                productId = 6,
                name = "Whole Wheat Bread",
                price = "$2.49",
                category = "Groceries",
                review = "4.6",
                description = "Healthy, delicious, and freshly baked bread.",
                imageUrl = listOf("https://images.unsplash.com/photo-1519864600265-abb248e49891?auto=format&fit=crop&w=400&q=80"),
                shopName = "Fresh Market",
                shopNumber = "105",
                distance = "2.4 km"
            ),
            Product(
                productId = 7,
                name = "Running Shoes",
                price = "$79.99",
                category = "Sportswear",
                review = "4.3",
                description = "Comfortable, lightweight shoes perfect for runners.",
                imageUrl = listOf("https://images.unsplash.com/photo-1526178613658-3c7e4b3d9997?auto=format&fit=crop&w=400&q=80"),
                shopName = "Sports Unlimited",
                shopNumber = "106",
                distance = "2.0 km"
            ),
            Product(
                productId = 8,
                name = "Basketball",
                price = "$29.99",
                category = "Sportswear",
                review = "4.8",
                description = "Durable outdoor basketball, official size.",
                imageUrl = listOf("https://images.unsplash.com/photo-1517649763962-0c623066013b?auto=format&fit=crop&w=400&q=80"),
                shopName = "Sports Unlimited",
                shopNumber = "106",
                distance = "2.0 km"
            ),
            Product(
                productId = 9,
                name = "Bestselling Novel",
                price = "$14.99",
                category = "Books",
                review = "4.9",
                description = "A must-read book for the year!",
                imageUrl = listOf("https://images.unsplash.com/photo-1463320898484-cdee8141c787?auto=format&fit=crop&w=400&q=80"),
                shopName = "Book Haven",
                shopNumber = "107",
                distance = "2.1 km"
            ),
            Product(
                productId = 10,
                name = "Children's Story Book",
                price = "$8.99",
                category = "Books",
                review = "4.7",
                description = "Delightful stories for growing minds.",
                imageUrl = listOf("https://images.unsplash.com/photo-1455885662065-29d2d09995c4?auto=format&fit=crop&w=400&q=80"),
                shopName = "Book Haven",
                shopNumber = "107",
                distance = "2.1 km"
            ),
            Product(
                productId = 11,
                name = "Pain Relief Balm",
                price = "$5.99",
                category = "Pharmacy",
                review = "4.6",
                description = "Soothes and relieves aches and pains.",
                imageUrl = listOf("https://images.unsplash.com/photo-1500937408085-8b75b7b6e0d2?auto=format&fit=crop&w=400&q=80"),
                shopName = "Pharma Store",
                shopNumber = "108",
                distance = "0.5 km"
            ),
            Product(
                productId = 12,
                name = "Custom Gift Basket",
                price = "$34.99",
                category = "Gifts",
                review = "4.9",
                description = "Perfect personalized gift basket for every occasion.",
                imageUrl = listOf("https://images.unsplash.com/photo-1512428559087-c1e9b0a180ba?auto=format&fit=crop&w=400&q=80"),
                shopName = "Gift Paradise",
                shopNumber = "109",
                distance = "1.0 km"
            ),
            Product(
                productId = 13,
                name = "Fresh Gala Apples (1kg)",
                price = "$3.99",
                category = "Groceries",
                review = "4.9",
                description = "Crisp, juicy apples directly from organic farms.",
                imageUrl = listOf("https://images.unsplash.com/photo-1502741338009-cac2772e18bc?auto=format&fit=crop&w=400&q=80"),
                shopName = "Fresh Market",
                shopNumber = "105",
                distance = "2.4 km"
            ),
        )
    }
    fun getNearbyShops(): List<Shop> {
        return listOf(
            Shop(
                shopName = "Urban Leather Co.",
                shopNumber = 101,
                category = "Bags",
                distance = "2.1 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6143,
                longitude = 77.2099
            ),
            Shop(
                shopName = "Leather Workshop",
                shopNumber = 102,
                category = "Bags",
                distance = "3.4 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1465101046530-73398c7f28ca?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6147,
                longitude = 77.2108
            ),
            Shop(
                shopName = "Artisan Leather",
                shopNumber = 103,
                category = "Accessories",
                distance = "4.2 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1512436991641-6745cdb1723f?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6150,
                longitude = 77.2111
            ),
            Shop(
                shopName = "ElectroMart",
                shopNumber = 104,
                category = "Electronics",
                distance = "1.7 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1580910051076-7b8375b3b14e?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6153,
                longitude = 77.2075
            ),
            Shop(
                shopName = "Fresh Market",
                shopNumber = 105,
                category = "Groceries",
                distance = "2.4 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1502741338009-cac2772e18bc?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6125,
                longitude = 77.2103
            ),
            Shop(
                shopName = "Sports Unlimited",
                shopNumber = 106,
                category = "Sportswear",
                distance = "2.0 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1517649763962-0c623066013b?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6131,
                longitude = 77.2082
            ),
            Shop(
                shopName = "Book Haven",
                shopNumber = 107,
                category = "Books",
                distance = "2.1 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1463320898484-cdee8141c787?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6135,
                longitude = 77.2120
            ),
            Shop(
                shopName = "Pharma Store",
                shopNumber = 108,
                category = "Pharmacy",
                distance = "0.5 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1500937408085-8b75b7b6e0d2?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6130,
                longitude = 77.2120
            ),
            Shop(
                shopName = "Gift Paradise",
                shopNumber = 109,
                category = "Gifts",
                distance = "1.0 km",
                imageUrl = listOf("https://images.unsplash.com/photo-1512428559087-c1e9b0a180ba?auto=format&fit=crop&w=400&q=80"),
                latitude = 28.6154,
                longitude = 77.2096
            )
        )
    }

    fun getUserData(): User{
        return User(
            userId = UUID.randomUUID().toString(),
            userName = "John Doe",
            userPhoneNumber = 123456789,
            userType = "Customer"
        )
    }
}