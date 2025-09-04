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
val seamVersion = "2.0.0" // Or the desired version

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
def seamVersion = "2.0.0" // Or the desired version

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
- **`SeamOtpView`** — OTP authorization handling
- **`SeamCredentialsView`** — Credential list and management

### UI Components

- **`UnlockContent`** — Unlock progress and controls
- **`UnlockHeader`** — Credential information display
- **`KeyCardComponent`** — Individual credential cards
- **`ErrorBanner`** — Contextual error messaging
- **`SeamButton`** — Branded action buttons

---

## Theming

Seam Mobile Components are fully brandable using Material3 theming via `SeamThemeProvider`. Customize colors, typography, and component styles:

```kotlin
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import co.seam.seamcomponents.ui.theme.SeamThemeProvider
import co.seam.seamcomponents.ui.theme.SeamTheme
import co.seam.seamcomponents.ui.theme.SeamTypography
import co.seam.seamcomponents.ui.theme.SeamKeyCardStyle

@Composable
fun MyApp() {
    SeamThemeProvider(
        lightColorScheme = lightColorScheme(
            primary = Color(0xFFFF6B35), // Your brand color
            secondary = Color(0xFF03DAC5),
            background = Color(0xFFF8F9FA)
        ),
        darkColorScheme = darkColorScheme(
            primary = Color(0xFFFF8A65),
            secondary = Color(0xFF03DAC5),
            background = Color(0xFF121212)
        ),
        seamTheme = SeamTheme(
            typography = SeamTypography.default,
            keyCard = SeamKeyCardStyle.default
        )
    ) {
        SeamAccessView(clientSessionToken = "seam_cst_...")
    }
}
```

### Custom Key Card Styling

Customize the appearance of credential cards:

```kotlin
import androidx.compose.ui.unit.dp
import co.seam.seamcomponents.ui.theme.SeamKeyCardStyle

SeamThemeProvider(
    seamTheme = SeamTheme(
        typography = SeamTypography.default,
        keyCard = SeamKeyCardStyle.default.copy(
            cornerRadius = 16.dp,
            elevation = 4.dp
            // Colors are handled through Material3 ColorScheme
        )
    )
) {
    SeamAccessView(clientSessionToken = "seam_cst_...")
}
```

---

## Advanced Usage

### Custom Integration

Use individual components for custom flows:

```kotlin
@Composable
fun CustomUnlockFlow(credential: KeyCard) {
    var unlockPhase by remember { mutableStateOf(UnlockPhase.IDLE) }

    Column {
        UnlockHeader(
            keyCard = credential,
            modifier = Modifier.padding(16.dp)
        )

        UnlockContent(
            unlockPhase = unlockPhase,
            onPressPrimaryButton = {
                // Handle unlock logic
            }
        )

        if (unlockPhase == UnlockPhase.FAILED) {
            ErrorBanner(
                errorMessage = "Unlock failed",
                onDismiss = { /* handle dismiss */ }
            )
        }
    }
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
