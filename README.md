# Seam Mobile Components (Android)

Pre-built Jetpack Compose views for unlocking, credential handling, error states, and feedback — ready to drop into your Android app and fully customizable to match your brand.

---

## Features

- 🚀 **Fast integration** — Add complete access flows in minutes with `SeamAccessView`.
- 🎨 **Customizable** — Material3 theming via `SeamTheme` (colors, fonts, key cards, unlock cards).
- ✅ **Production-ready** — Includes error handling, retries, haptic feedback, and animations out of the box.
- 🔗 **Seam SDK integration** — Works seamlessly with [Seam Mobile SDK](https://docs.seam.co/latest/capability-guides/mobile-access/mobile-device-sdks).
- 🧩 **Composable** — Use all-in-one views or mix with your own UI components.
- 📱 **Modern Android** — Built with Jetpack Compose and Material3 design system.

---

## Requirements

To integrate the Seam Phone Android SDK into your application, please ensure your project meets the following minimum requirements:

*   **Compile SDK:** 34
*   **Kotlin Version:** 2.1.0 or greater
*   **Minimum Android SDK:** The required `minSdk` depends on the specific Seam integration modules you include:
    *   Base SDK (Core, API, Common, Network, Analytics): **API Level 24** (Android 7.0)
    *   Including `saltoks` or `saltospace`: **API Level 24** (Android 7.0)
    *   Including `latch`: **API Level 26** (Android 8.0)
    *   Including `assaabloy`: **API Level 28** (Android 9.0)

    Your application's `minSdk` must be set to the **highest** level required by any of the Seam modules you use.

## Dependencies and Credentials

This project relies on the Seam Mobile SDK artifacts hosted on GitHub Packages. To successfully build the project, you need appropriate credentials.

### GitHub Packages Repository

The `settings.gradle.kts` file configures Gradle to look for dependencies in the Seam GitHub Packages repository:

```kotlin
// getPropertyOrNull is a helper function defined in settings.gradle.kts
// to safely read properties from local.properties
fun getPropertyOrNull(propertyName: String): String? {
    val propertiesFile = file("local.properties")

    if (!propertiesFile.exists()) return null

    val properties = Properties()
    properties.load(propertiesFile.inputStream())
    return properties.getProperty(propertyName, null)
}

// settings.gradle.kts
repositories {
    // ... other repositories
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/seampkg/seam-mobile-sdk")
        credentials {
            username = getPropertyOrNull("seamUsername")
            password = getPropertyOrNull("seamPat")
        }
    }
}
```

if you are using groovy, your `settings.gradle` should look like this:
```groovy
// getPropertyOrNull is a helper function defined in settings.gradle
// to safely read properties from local.properties
String getPropertyOrNull(String propertyName) {
    def propertiesFile = file("local.properties")

    if (!propertiesFile.exists()) return null

    def properties = new Properties()
    properties.load(propertiesFile.inputStream())
    return properties.getProperty(propertyName, null)
}

// settings.gradle
repositories {
    // ... other repositories
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/seampkg/seam-mobile-sdk")
        credentials {
            username = getPropertyOrNull("seamUsername")
            password = getPropertyOrNull("seamPat")
        }
    }
}
```

### Credentials (`local.properties`)

Gradle requires a username and a Personal Access Token (PAT) with `read:packages` scope to access this repository. These credentials **must** be defined in a `local.properties` file at the root of the project:

```properties
# local.properties (DO NOT COMMIT THIS FILE)
seamUsername=YOUR_GITHUB_USERNAME
seamPat=YOUR_SEAM_PROVIDED_PAT
```

Replace `YOUR_GITHUB_USERNAME` with your GitHub username. For the Personal Access Token (PAT), please **ask Seam for a token** with the necessary `read:packages` scope.

**Important:** The `local.properties` file is included in `.gitignore` and should **never** be committed to your version control system.

### Seam SDK Dependencies

The specific Seam SDK components are included in the `app/build.gradle.kts`.
The `seam-phone-sdk-android-core` contains the core code for the SDK. It is necessary for all other components to work.
The other dependencies are related to specific integrations. For example, if you want to use the Salto Space integration, you need to add `seam-phone-sdk-android-saltospace` to the project.
You can add more integrations dependencies as needed.

Kotlin script (`app/build.gradle.kts`)
```kotlin
val seamVersion = "3.0.12" // Or the desired version

dependencies {
    // ... other dependencies
    implementation("co.seam:seam-phone-sdk-android-seamcomponents:$seamVersion")
    implementation("co.seam:seam-phone-sdk-android-core:$seamVersion")

    // include as needed
    implementation("co.seam:seam-phone-sdk-android-saltoks:$seamVersion")
    implementation("co.seam:seam-phone-sdk-android-saltospace:$seamVersion")
    implementation("co.seam:seam-phone-sdk-android-latch:$seamVersion")
    implementation("co.seam:seam-phone-sdk-android-assaabloy:$seamVersion")
    // ...
}
```

Groovy script (`app/build.gradle`)
```groovy
// app/build.gradle.kts
val seamVersion = "3.0.12" // Or the desired version

dependencies {
    // ... other dependencies
    implementation "co.seam:seam-phone-sdk-android-seamcomponents:$seamVersion"
    implementation "co.seam:seam-phone-sdk-android-core:$seamVersion"
    implementation "co.seam:seam-phone-sdk-android-saltoks:$seamVersion"
    implementation "co.seam:seam-phone-sdk-android-saltospace:$seamVersion"
    implementation "co.seam:seam-phone-sdk-android-latch:$seamVersion"
    implementation "co.seam:seam-phone-sdk-android-assaabloy:$seamVersion"
    // ...
}
```

## Setup

Before running the app, you need a **Client Session Token (CST)**. This token authenticates the mobile device with Seam's backend. You typically obtain this token through your backend server after authenticating the user.
Pleas ask Seam on how to obtain a CST since it is not part of the mobile SDK.

---

## Quick Start

The fastest way to get started is with `SeamAccessView`, which orchestrates all underlying components to deliver a complete unlock experience:

```kotlin
import co.seam.seamcomponents.SeamAccessView

@Composable
fun MyAccessScreen() {
    SeamAccessView(
        clientSessionToken = "seam_cst_your_token_here"
    )
}
```

That's it — you now have a fully functional unlock UI in your Android app.
`SeamAccessView` automatically integrates with the Seam Mobile SDK for:

- **SDK initialization** — Automatic setup with your CST
- **Device discovery** — Bluetooth/NFC scanning and connection
- **Credential management** — Automatic sync and caching
- **Unlock flows** — Provider-specific unlock sequences
- **Error handling** — Network, permission, and hardware error recovery

The Seam Components handle SDK initialization, activation, and state management transparently.

---

## Available Components

### Core Views

- **`SeamAccessView`** — Complete access management interface
- **`SeamUnlockCardView`** — Individual credential unlock interface
- **`SeamCredentialsView`** — Credential list and management

### UI Components

- **`UnlockContent`** — Unlock progress and controls
- **`UnlockHeader`** — Credential information display
- **`KeyCardComponent`** — Individual credential cards
- **`ErrorBanner`** — Contextual error messaging
- **`SeamButton`** — Branded action buttons

---

## Using separated components

```kotlin
import co.seam.seamcomponents.SeamAccessView

@Composable
fun MyAccessScreen() {
    // Let's initialize the Seam SDK with the client session token
    val context = LocalContext.current
    LaunchedEffect(showSeamComponent) {
        SeamSDK.initialize(
            context,
            "seam_cst_your_token_here"
        )
        // Let's activate the Seam SDK after initializing it
        SeamSDK.getInstance().activate()
    }

    var unlockKeyCard by remember { mutableStateOf<KeyCard?>(null) }
    // Rendering the Cards view
    SeamCredentialsView(
        onNavigateToUnlock = { keyCard ->
            // handling card click.
            unlockKeyCard = keyCard
        }
    )

    // Navigate to Unlock view when unlockKeyCard is not null
    unlockKeyCard?.let {
        SeamUnlockCardView(
            keyCard = it,
            onNavigateBack = {
                unlockKeyCard = null
            }
        )
    }
}
```


## Theming

Seam Mobile Components are fully brandable using Material3 theming via `SeamComponentsTheme`. Customize colors, typography, and component styles.

```kotlin

private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFFADC7FF),
        onPrimary = Color(0xFF002E69),
        ...
    )

private val LightColorScheme =
    darkColorScheme(
        primary = Color(0xFFAD00FF),
        onPrimary = Color(0xFFBA2E69),
        ...
    )

// Create your Theme as usual
@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme)  DarkColorScheme else LightColorScheme


    // Instead of MaterialTheme use SeamComponentsTheme here
    SeamComponentsTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
```

### Custom Typography

Typography is optioal, but you can Material Typography as usual

```kotlin

private val DarkColorScheme =
    darkColorScheme(
        primary = Color(0xFFADC7FF),
        onPrimary = Color(0xFF002E69),
        ...
    )

private val LightColorScheme =
    darkColorScheme(
        primary = Color(0xFFAD00FF),
        onPrimary = Color(0xFFBA2E69),
        ...
    )

val Typography = Typography(
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
    ),
    ...
)
// Create your Theme as usual
@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme)  DarkColorScheme else LightColorScheme


    // Instead of MaterialTheme use SeamComponentsTheme here
    SeamComponentsTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
```

### Custom Key Card Styling

It is possible to go further with customization by setting the `seamTheme` attribute on `SeamComponentsTheme`.

```kotlin
@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme)  DarkColorScheme else LightColorScheme

    // Define the Key Card Style
    val keyCardStyle = SeamKeyCardStyle(
        backgroundGradient = listOf(Color.Red, Color.Cyan),
        textColor = Color.Green,
        accentColor = Color.Green,
        cornerRadius = 24.dp,
        shadowColor = Color.Gray,
        shadowYOffset = 15.dp,
        errorColor = Color.Cyan
    )
    val seamComponentsThemeData = SeamComponentsThemeData(
        keyCard = keyCardStyle
    )

    // Instead of MaterialTheme use SeamComponentsTheme here
    SeamComponentsTheme(
        seamTheme = seamComponentsThemeData,
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}


```

### Custom Unlock Card Styling

It is similar to the Key Card customization

```kotlin
@Composable
fun MyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme)  DarkColorScheme else LightColorScheme

    // Define the Key Card Style
    val keyCardStyle = SeamKeyCardStyle(
        backgroundGradient = listOf(Color.Red, Color.Cyan),
        textColor = Color.Green,
        accentColor = Color.Green,
        cornerRadius = 24.dp,
        shadowColor = Color.Gray,
        shadowYOffset = 15.dp,
        errorColor = Color.Cyan
    )

    // Define Unlock Card Style
    val unlockCardStyle = SeamUnlockCardStyle(
        keyButtonGradient = listOf(Color.Red, Color.Cyan),
        keyButtonShadowColor = Color.Red,
        keyButtonShadowRadius = 12.dp,
        bulletBackground = Color.Magenta,
        bulletTextColor = Color.Cyan,
        instructionTextColor = Color.Green,
        headerBackground = Color.Cyan,
        headerTitleColor = Color.Green,
        headerSubtitleColor = Color.Magenta,
        keyIconColorIdle = Color.Green,
        keyIconColorActive = Color.Cyan,
        successColor = Color.Magenta,
        successIconColor = Color.Green,
        errorColor = Color.Cyan,
    )

    val seamComponentsThemeData = SeamComponentsThemeData(
        keyCard = keyCardStyle,
        unlockCard = unlockCardStyle
    )

    // Instead of MaterialTheme use SeamComponentsTheme here
    SeamComponentsTheme(
        seamTheme = seamComponentsThemeData,
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}


```

---

## Documentation

- [Seam Components Overview](https://docs.seam.co/latest/ui-components/seam-mobile-components)
- [Android SDK Integration](../README.md)
- [API Reference](https://docs.seam.co/latest/capability-guides/mobile-access/mobile-device-sdks)

---

## License

[MIT](LICENSE)

---

## Contributing

Issues and pull requests are welcome! Please open a GitHub issue for bugs, feature requests, or integration questions.

For questions about Seam Mobile Components:
- 📧 Email: [support@seam.co](mailto:support@seam.co)
- 📖 Documentation: [docs.seam.co](https://docs.seam.co)
