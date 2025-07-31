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
                name = "T-Shirt",
                price = "$99",
                category = "Clothing",
                review = "4.5",
                description = "Comfortable cotton t-shirt",
                imageUrl = listOf("https://cdn.pixabay.com/photo/2024/02/06/18/10/ai-generated-8557635_1280.jpg"),
                shopName = "Urban Fashion",
                shopNumber = "123-456-7890",
                distance = "2.5 km"
            ),
            Product(
                productId = 2,
                name = "Shoes",
                price = "$129",
                category = "Footwear",
                review = "4.5",
                description = "Comfortable running shoes",
                imageUrl = listOf("https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/99486859-0ff3-46b4-949b-2d16af2ad421/custom-nike-dunk-high-by-you-shoes.png"),
                shopName = "Sports Gear",
                shopNumber = "123-476-7190",
                distance = "1.5 km"
            ),
            Product(
                productId = 3,
                name = "Heels",
                price = "$150",
                category = "Footwear",
                review = "4.5",
                description = "Comfortable cotton t-shirt",
                imageUrl = listOf("https://tse2.mm.bing.net/th/id/OIP.cUWUbvpQOA_X1Z2wjZv4NAHaHa?pid=Api&P=0&h=180"),
                shopName = "Urban Fashion",
                shopNumber = "123-456-7890",
                distance = "2.5 km"
            )
        )
    }
    fun getNearbyShops(): List<Shop> {
        return listOf(
            Shop(
                shopName = "Urban Wear",
                shopNumber = 1,
                category = "Clothing",
                distance = "1.2 km",
                imageUrl = listOf("https://c2.peakpx.com/wallpaper/571/315/827/clothing-shop-jeans-costume-pants-wallpaper.jpg")
            ),
            Shop(
                shopName = "Fresh Market",
                shopNumber = 2,
                category = "Grocery Store",
                distance = "2.4 km",
                imageUrl = listOf("https://cdn.pixabay.com/photo/2017/08/10/07/20/grocery-store-2619380_1280.jpg")
            ),
            Shop(
                shopName = "Sports Unlimited",
                shopNumber = 3,
                category = "Sports Store",
                distance = "0.8 km",
                imageUrl = listOf("https://4.bp.blogspot.com/-fjLkRmTkG9I/Wh4ZL_ox2hI/AAAAAAAA-sA/QZnV_LeoB1Q37TNXQJCyB3rasqVqjkeKwCLcBGAs/s1600/1.jpg")
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