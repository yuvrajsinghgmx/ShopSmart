# Contributors

## Project Admin

| ![Yuvraj Singh](https://avatars.githubusercontent.com/u/143984267?v=4&s=100) |
|:--:|
| **Yuvraj Singh** |
| [![LinkedIn](https://img.icons8.com/fluency/32/000000/linkedin.png)](https://www.linkedin.com/in/yuvrajsinghgmx/) |

Yuvraj Singh is the founder and lead developer of **ShopSmart**. With a passion for building user-centric solutions, Yuvraj oversees the project’s vision, manages its technical roadmap, and ensures the continuous growth and success of ShopSmart. You can connect with Yuvraj on LinkedIn to discuss the project or to explore potential collaborations.

---

## Contributors

We want to extend our deepest thanks to everyone who has contributed to **ShopSmart**. Whether through writing code, improving documentation, reporting bugs, or providing feedback, every contribution plays a crucial role in enhancing the project. We recognize that open-source projects thrive on community support, and we are immensely grateful to each contributor for their time and effort.

| **Contributors** |
|:--:|
| ![Contributors](https://contrib.rocks/image?repo=yuvrajsinghgmx/ShopSmart) |

---

## How to Contribute

We welcome contributions from anyone who is passionate about improving **ShopSmart**. No matter your level of expertise, we encourage you to get involved and help us make this project even better. Contributions come in many forms — you can contribute by coding, reporting issues, improving documentation, or even suggesting new features.

To get started, follow these steps:

1. **Fork the repository**: Create your own copy of the ShopSmart repository by clicking the 'Fork' button at the top right of the GitHub page.
2. **Clone the repository**: Clone your fork locally so you can make changes.
    ```bash
    git clone https://github.com/your-username/ShopSmart.git
    ```
3. **Create a new branch**: When you are ready to start working on a feature, create a new branch.
    ```bash
    git checkout -b <branch-name>
    ```
4. **Make your changes**: Work on your feature or bugfix. Ensure your code adheres to our guidelines and is well-documented.
5. **Commit your changes**: Once you're satisfied with your changes, commit them to your branch with a meaningful message.
    ```bash
    git commit -m 'Add AmazingFeature'
    ```
6. **Push your changes**: Push the changes from your local branch to GitHub.
    ```bash
    git push origin <branch-name>
    ```
7. **Open a Pull Request**: Go to the original repository and open a pull request from your branch to the main repository.


---


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

---

## Code of Conduct

To ensure a welcoming environment for all contributors, this project adheres to the [Contributor Covenant Code of Conduct](CODE_OF_CONDUCT.md). By participating in ShopSmart, you agree to abide by this code, which is designed to ensure that our community remains friendly, respectful, and inclusive. Please take a moment to familiarize yourself with the code, as we expect all contributors to uphold its principles.

---

## Recognition

A heartfelt **thank you** to everyone who has contributed to **ShopSmart**. Your input has been instrumental in shaping the project and making it better for users and developers alike. Every piece of code, bug report, or feature suggestion helps us grow and improve.

We acknowledge the importance of each contribution, and we are truly grateful for your efforts. To all our contributors — your hard work does not go unnoticed. Together, we continue to make **ShopSmart** a fantastic project for everyone!



