# Contributors 🎯

We want to extend our deepest thanks to everyone who has contributed to **ShopSmart**. Whether through writing code, improving documentation, reporting bugs, or providing feedback, every contribution plays a crucial role in enhancing the project. We recognize that open-source projects thrive on community support, and we are immensely grateful to each contributor for their time and effort.

<br>

# Code of Conduct 📃

To ensure a welcoming environment for all contributors, this project adheres to the [Code of Conduct](https://github.com/yuvrajsinghgmx/ShopSmart/blob/master/CODE_OF_CONDUCT.md). By participating in ShopSmart, you agree to abide by this code, which is designed to ensure that our community remains friendly, respectful, and inclusive. Please take a moment to familiarize yourself with the code, as we expect all contributors to uphold its principles.

# <h1 align="center">Star our Repository ⭐</h1>

# <p align = "center">[![Discord](https://img.shields.io/badge/Discord-darkblue?style=for-the-badge&logo=discord&logoColor=white)](https://discord.gg/Wq6MZ88ecf) </p>

<div align = "center" style = "display:flex; justify-content:space-evenly; gap:100px;"> 

[![Stars](https://img.shields.io/github/stars/yuvrajsinghgmx/ShopSmart?style=for-the-badge&logo=github)](https://github.com/yuvrajsinghgmx/ShopSmart/stargazers) 
[![Forks](https://img.shields.io/github/forks/yuvrajsinghgmx/ShopSmart?style=for-the-badge&logo=github)](https://github.com/yuvrajsinghgmx/ShopSmart/network/members) 
[![Issues](https://img.shields.io/github/issues/yuvrajsinghgmx/ShopSmart?style=for-the-badge&logo=github)](https://github.com/yuvrajsinghgmx/ShopSmart/issues) 
[![PRs Open](https://img.shields.io/github/issues-pr/yuvrajsinghgmx/ShopSmart?style=for-the-badge&logo=github)](https://github.com/yuvrajsinghgmx/ShopSmart/pulls) 
[![PRs Closed](https://img.shields.io/github/issues-pr-closed/yuvrajsinghgmx/ShopSmart?style=for-the-badge&logo=github&color=2cbe4e)](https://github.com/yuvrajsinghgmx/ShopSmart/pulls?q=is%3Apr+is%3Aclosed)

</div>

<br>

# <div align="center"> For Help And Support 💬 </div>

<div align="center">

| ![Yuvraj Singh](https://avatars.githubusercontent.com/u/143984267?v=4&s=100) |
|:--:|
| **Yuvraj Singh** |
| <a href="mailto:your_email@example.com"><img src="https://img.icons8.com/fluency/32/000000/email.png" alt="Email"/></a> |
| [![LinkedIn](https://img.icons8.com/fluency/32/000000/linkedin.png)](https://www.linkedin.com/in/yuvrajsinghgmx/) |

</div>

<div align="center">

|  **Contributors** |
|:--:|
| ![Contributors](https://contrib.rocks/image?repo=yuvrajsinghgmx/ShopSmart) |

Yuvraj Singh is the founder and lead developer of **ShopSmart**. With a passion for building user-centric solutions, Yuvraj oversees the project’s vision, manages its technical roadmap, and ensures the continuous growth and success of ShopSmart. You can connect with Yuvraj on LinkedIn to discuss the project or to explore potential collaborations.

</div>

<br>

# Need Help With The Basics? 🤔

If you're new to Git and GitHub, no worries! Here are some useful resources:

- [Forking a Repository](https://help.github.com/en/github/getting-started-with-github/fork-a-repo)
- [Cloning a Repository](https://help.github.com/en/desktop/contributing-to-projects/creating-an-issue-or-pull-request)
- [How to Create a Pull Request](https://opensource.com/article/19/7/create-pull-request-github)
- [Getting Started with Git and GitHub](https://towardsdatascience.com/getting-started-with-git-and-github-6fcd0f2d4ac6)
- [Learn GitHub from Scratch](https://docs.github.com/en/get-started/start-your-journey/git-and-github-learning-resources)

<br>

# Project Structure

<!-- START_STRUCTURE -->

```bash
SHOPSMART/
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

<br>

# First Pull Request ✨

1. **Star this repository**
    Click on the top right corner marked as **Stars** at last.

2. **Fork this repository**
    Click on the top right corner marked as **Fork** at second last.

3. **Clone the forked repository**

```bash
git clone https://github.com/<your-github-username>/ShopSmart.git
```
  
4. **Navigate to the project directory**

```bash
cd ShopSmart
```

5. **Create a new branch**

```bash
git checkout -b <your_branch_name>
```

6. **To make changes**

```bash
git add .
```

7. **Now to commit**

```bash
git commit -m "add comment according to your changes or addition of features inside this"
```

8. **Push your local commits to the remote repository**

```bash
git push -u origin <your_branch_name>
```

9. **Create a Pull Request**

10. **Congratulations! 🎉 you've made your contribution**

<br>

# Alternatively, contribute using GitHub Desktop 🖥️

1. **Open GitHub Desktop:**
  Launch GitHub Desktop and log in to your GitHub account if you haven't already.

2. **Clone the Repository:**
- If you haven't cloned the project repository yet, you can do so by clicking on the "File" menu and selecting "Clone Repository."
- Choose the project repository from the list of repositories on GitHub and clone it to your local machine.

3.**Switch to the Correct Branch:**
- Ensure you are on the branch that you want to submit a pull request for.
- If you need to switch branches, you can do so by clicking on the "Current Branch" dropdown menu and selecting the desired branch.

4. **Make Changes:**
- Make your changes to the code or files in the repository using your preferred code editor.

5. **Commit Changes:**
- In GitHub Desktop, you'll see a list of the files you've changed. Check the box next to each file you want to include in the commit.
- Enter a summary and description for your changes in the "Summary" and "Description" fields, respectively. Click the "Commit to <branch-name>" button to commit your changes to the local branch.

6. **Push Changes to GitHub:**
- After committing your changes, click the "Push origin" button in the top right corner of GitHub Desktop to push your changes to your forked repository on GitHub.

7. **Create a Pull Request:**
- Go to the GitHub website and navigate to your fork of the project repository.
- You should see a button to "Compare & pull request" between your fork and the original repository. Click on it.

8. **Review and Submit:**
- On the pull request page, review your changes and add any additional information, such as a title and description, that you want to include with your pull request.
- Once you're satisfied, click the "Create pull request" button to submit your pull request.

9. **Wait for Review:**
Your pull request will now be available for review by the project maintainers. They may provide feedback or ask for changes before merging your pull request into the main branch of the project repository.

<br>

# Good Coding Practices 🧑‍💻

1. **Follow the Project's Code Style**

   - Maintain consistency with the existing code style (indentation, spacing, comments).
   - Use meaningful and descriptive names for variables, functions, and classes.
   - Keep functions short and focused on a single task.
   - Avoid hardcoding values; instead, use constants or configuration files when possible.

2. **Write Clear and Concise Comments**

   - Use comments to explain why you did something, not just what you did.
   - Avoid unnecessary comments that state the obvious.
   - Document complex logic and functions with brief explanations to help others understand your thought -process.

3. **Keep Code DRY (Don't Repeat Yourself)**

   - Avoid duplicating code. Reuse functions, methods, and components whenever possible.
   - If you find yourself copying and pasting code, consider creating a new function or component.

4. **Write Tests**

   - Write unit tests for your functions and components.
   - Ensure your tests cover both expected outcomes and edge cases.
   - Run tests locally before making a pull request to make sure your changes don’t introduce new bugs.

5. **Code Reviews and Feedback**

   - Be open to receiving constructive feedback from other contributors.
   - Conduct code reviews for others and provide meaningful suggestions to improve the code.
   - Always refactor your code based on feedback to meet the project's standards.

<br>

# Pull Request Process 🚀

When submitting a pull request, please adhere to the following:

1. **Self-review your code** before submission. 😀
2. Include a detailed description of the functionality you’ve added or modified.
3. Comment your code, especially in complex sections, to aid understanding.
4. Add relevant screenshots to assist in the review process.
5. Submit your PR using the provided template and hang tight; we'll review it as soon as possible! 🚀

<br>

# Issue Report Process 📌

To report an issue, follow these steps:

1. Navigate to the project's issues section :- [Issues](https://github.com/yuvrajsinghgmx/ShopSmart/issues/new)
2. Please kindly choose the appropriate template according to your issue.
3. Provide a clear and concise description of the issue.
4. Wait until someone looks into your report.
5. Begin working on the issue only after you have been assigned to it. 🚀

<br>

# Recognition 💫

A heartfelt **thank you** to everyone who has contributed to **ShopSmart**. Your input has been instrumental in shaping the project and making it better for users and developers alike. Every piece of code, bug report, or feature suggestion helps us grow and improve.

We acknowledge the importance of each contribution, and we are truly grateful for your efforts. To all our contributors — your hard work does not go unnoticed. Together, we continue to make **ShopSmart** a fantastic project for everyone!

<br>

# Thank you for contributing 💗

We truly appreciate your time and effort to help improve our project. Feel free to reach out if you have any questions or need guidance. Happy coding! 🚀

##