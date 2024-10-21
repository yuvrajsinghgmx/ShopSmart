
# **ShopSmart: Your Smart Shopping Companion**

**Effortless shopping, simplified.** 🛒

ShopSmart is a user-friendly shopping list app built with Kotlin and Jetpack Compose. From organizing items by category to adding them through voice commands, ShopSmart helps you streamline your grocery shopping experience—whether you're online or offline.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

<!-- Added Hacktoberfest 2024 and GSSoc Extended 2024 banners -->
### This project is now OFFICIALLY accepted for

<div align="center">
  <img src="https://raw.githubusercontent.com/SwanandD121/FeatherPerfect_fe/refs/heads/main/Untitled%20design.png" alt="GSSoC 2024 Extd" width="80%">
  <img src="https://cdn.discordapp.com/attachments/657543125190967316/1294560786114674748/Screenshot_2024-10-12_122347.png?ex=670b752f&is=670a23af&hm=26ddd7f41740b8b19ee4985e7568b3892091384b3b85e7165770a4b10f4d1050&" alt="Hacktoberfest 2024" width="80%">
</div>
<br>

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## Project Structure

<!-- START_STRUCTURE -->
```
├── Assets/
│   ├── Pasted image.png
│   ├── ScreenShot1.jpg
│   ├── ScreenShot2.jpg
│   ├── ScreenShot3.jpg
│   ├── ScreenShot4.jpg
│   ├── ShopSmartLogo.png
│   └── ShopSmartLogo2.png
├── CODE_OF_CONDUCT.md
├── DarkModeToggle
├── LICENSE
├── README.md
├── Screenshot 2024-10-03 202535.png
├── Screenshot 2024-10-03 202631.png
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── androidTest/
│       │   └── java/
│       │       └── com/
│       │           └── yuvrajsinghgmx/
│       │               └── shopsmart/
│       │                   └── ExampleInstrumentedTest.kt
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/
│       │   │   └── com/
│       │   │       └── yuvrajsinghgmx/
│       │   │           └── shopsmart/
│       │   │               ├── ApiData/
│       │   │               │   ├── Hit.kt
│       │   │               │   └── Pics.kt
│       │   │               ├── MainActivity.kt
│       │   │               ├── MyApp.kt
│       │   │               ├── Repository/
│       │   │               │   └── ImageRepo.kt
│       │   │               ├── api/
│       │   │               │   └── API.kt
│       │   │               ├── datastore/
│       │   │               │   ├── instance.kt
│       │   │               │   └── products.kt
│       │   │               ├── di/
│       │   │               │   └── NetworkModule.kt
│       │   │               ├── navbarpr.kt
│       │   │               ├── navigation/
│       │   │               │   └── Navigation.kt
│       │   │               ├── profilefeatures/
│       │   │               │   └── ImageComponents.kt
│       │   │               ├── screens/
│       │   │               │   ├── ContactUsScreen.kt
│       │   │               │   ├── EmailSignUpScreen.kt
│       │   │               │   ├── FAQScreen.kt
│       │   │               │   ├── HelpS.kt
│       │   │               │   ├── HomeScreen.kt
│       │   │               │   ├── ListScreen.kt
│       │   │               │   ├── MyOrders.kt
│       │   │               │   ├── Profile.kt
│       │   │               │   ├── ShopSmartNavBar.kt
│       │   │               │   ├── Signup.kt
│       │   │               │   ├── TermsCondition.kt
│       │   │               │   └── Upcoming.kt
│       │   │               ├── ui/
│       │   │               │   ├── Authpage.kt.txt
│       │   │               │   └── theme/
│       │   │               │       ├── Color.kt
│       │   │               │       ├── Theme.kt
│       │   │               │       └── Type.kt
│       │   │               ├── utils/
│       │   │               │   ├── ImageHelper.kt
│       │   │               │   └── SharedPrefsHelper.kt
│       │   │               └── viewmodel/
│       │   │                   ├── HomeScreenViewModel.kt
│       │   │                   └── ShoppingListViewModel.kt
│       │   └── res/
│       │       ├── drawable/
│       │       │   ├── addicon.png
│       │       │   ├── baseline_keyboard_voice_24.xml
│       │       │   ├── baseline_star_24.xml
│       │       │   ├── bell.png
│       │       │   ├── checkout.png
│       │       │   ├── customer_care.xml
│       │       │   ├── edit.png
│       │       │   ├── empty_dark.png
│       │       │   ├── empty_light.png
│       │       │   ├── fb.xml
│       │       │   ├── file__1_.png
│       │       │   ├── gmail.xml
│       │       │   ├── google.xml
│       │       │   ├── help.png
│       │       │   ├── ic_launcher_background.xml
│       │       │   ├── ic_launcher_foreground.xml
│       │       │   ├── instagram.xml
│       │       │   ├── linkedin.xml
│       │       │   ├── logo1.png
│       │       │   ├── logo2.png
│       │       │   ├── profile.png
│       │       │   ├── profilenewone.png
│       │       │   ├── setting.png
│       │       │   ├── shopinterior.jpeg
│       │       │   ├── shoppingbag.png
│       │       │   ├── shopsmart.png
│       │       │   └── whatsapp.xml
│       │       ├── font/
│       │       │   ├── abril_fatface_regular.ttf
│       │       │   ├── lexend_black.ttf
│       │       │   ├── lexend_bold.ttf
│       │       │   ├── lexend_extrabold.ttf
│       │       │   ├── lexend_light.ttf
│       │       │   ├── lexend_medium.ttf
│       │       │   ├── lexend_regular.ttf
│       │       │   ├── lexend_semibold.ttf
│       │       │   ├── lexend_thin.ttf
│       │       │   ├── montserrat_bold.ttf
│       │       │   └── montserrat_regular.ttf
│       │       ├── mipmap-anydpi/
│       │       │   ├── ic_launcher.xml
│       │       │   └── ic_launcher_round.xml
│       │       ├── mipmap-hdpi/
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-mdpi/
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xhdpi/
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xxhdpi/
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── mipmap-xxxhdpi/
│       │       │   ├── ic_launcher.webp
│       │       │   └── ic_launcher_round.webp
│       │       ├── values/
│       │       │   ├── colors.xml
│       │       │   ├── strings.xml
│       │       │   └── themes.xml
│       │       └── xml/
│       │           ├── backup_rules.xml
│       │           ├── data_extraction_rules.xml
│       │           └── file_paths.xml
│       └── test/
│           └── java/
│               └── com/
│                   └── yuvrajsinghgmx/
│                       └── shopsmart/
│                           └── ExampleUnitTest.kt
├── build.gradle.kts
├── contributors.md
├── gradle/
│   ├── libs.versions.toml
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradle.properties
├── gradlew
├── gradlew.bat
├── repo_structure.txt
└── settings.gradle.kts
```
<!-- END_STRUCTURE -->

## **🔗 Table of Contents**
1. [Features](#features)
2. [Screenshots](#screenshots)
3. [Upcoming Features](#upcoming-features)
4. [Technologies Used](#technologies-used)
5. [Setup Guide](#setup-guide)
6. [Contributing](#contributing)
7. [License](#license)

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **✨ Features**

- **List Creation with Image Generation**: Users can create and add multiple shopping lists. Each list will automatically generate a relevant image for better organization.
- **Offline Mode**: All features are available without an internet connection.
- **Price Estimation**: Add price estimates for items to keep track of budgets.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **Upcoming Features**

- **Item Sorting**: Items are sorted by categories for easier shopping.
- **Voice Input**: Users can add items using voice input.
- **Reminders**: Set reminders for shopping trips.
- **Dark Mode**: An optional dark mode for better usability in low-light environments.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **Technologies Used**

- **Kotlin**: The primary programming language used for app development.
- **Android SDK**: Android Software Development Kit for building Android apps.
- **MVVM Architecture**: Model-View-ViewModel architecture pattern for clean and maintainable code.
- **Room**: Used for local database management and data storage.
- **Retrofit**: HTTP client for API requests.
- **Pixabay API**: Used to fetch images for list creation.
- **ViewModel**: Manages UI-related data in a lifecycle-conscious way.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **Screenshots**
| App Splash Screen        | Shopping List Creation  |
|------------------------|-------------------------|
| ![Home Screen](./Assets/ScreenShot4.jpg) | ![List Creation](./Assets/ScreenShot2.jpg) |

| Items List    | Price Estimation Feature |
|--------------------------|--------------------------|
| ![Voice Input](./Assets/ScreenShot3.jpg) | ![Price Estimation](./Assets/ScreenShot1.jpg) |

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **🚀 Setup Guide**

To get started with ShopSmart, follow these steps:

1. **Fork It**: Fork the project to create your own copy.

2. **Clone the repository:**

   ```bash
   $ git clone https://github.com/<your-account-username>/<your-forked-project>.git
   ```

3. **Open in Android Studio**:  
   Open the cloned project in Android Studio.

4. **Build & Run**:  
   Connect your Android device or launch an emulator, then hit the **Run** button in Android Studio to build and deploy the app.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **🤝Contributing**

We’re excited for you to contribute to ShopSmart! 

Please refer the [Contributors Guide](./contributors.md) Before you start Contributing.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## Important Guidelines ⚡

1. Contributors should only work on issues that have been assigned to them.
2. Each pull request should be associated with one issue only.
3. No minor text edits should be submitted unless necessary.
4. Unethical behavior, tampering with files, or harassment will result in disqualification.
5. Follow the community guidelines while contributing to ensure a healthy collaborative environment.
6. No Issue Repetitions are allowed.
7. Check the issues before you raise an issue.
8. No Plagiarism of Codes.
9. Make sure the Code is genuine and it helps maximum to this project.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## Community Guidelines 🤝

Please follow these guidelines while contributing:

- Be respectful and considerate towards others.
- Use inclusive language and foster a welcoming environment.
- Avoid personal attacks, harassment, or discrimination.
- Keep discussions focused on constructive topics.

- ## Code Reviews ✅

- Be open to feedback from other contributors.
- Participate in code reviews to help improve the project.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

<!-- Modified the Contributors Mention Section -->
<div>
  <h2 align = "center"><img src="https://raw.githubusercontent.com/Tarikul-Islam-Anik/Animated-Fluent-Emojis/master/Emojis/Smilies/Red%20Heart.png" width="35" height="35">Our Contributors</h2>
  <div align = "center">
 <h3>Thank you for contributing to our repository</h3>

![Contributors](https://contrib.rocks/image?repo=yuvrajsinghgmx/ShopSmart&v=1)

</div>

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **Code of Conduct**

Please note that this project is released with a [Contributor Code of Conduct](https://www.contributor-covenant.org/). By participating in this project, you agree to abide by its terms.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **📜License**

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

<!-- Added Team section -->
## 👥 Team
| ![Yuvraj Singh](https://avatars.githubusercontent.com/u/143984267?v=4&s=80) |
|:--:|
| **Yuvraj Singh** <br> <sub>Project Admin</sub> | 
| [![LinkedIn](https://img.icons8.com/fluency/32/000000/linkedin.png)](https://www.linkedin.com/in/yuvrajsinghgmx/) |

For any inquiries or feedback, please contact. Happy Contributing 🫡

<!--Line-->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="900">

## **🙏 Support**

Found a bug or have a feature request? Please open an issue.
If you like the project, don't forget to give it a ⭐!

**Crafted with ❤️  by Yuvraj Singh**
