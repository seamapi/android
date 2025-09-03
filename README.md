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

- **Android Studio** — Arctic Fox (2020.3.1) or later
- **Compile SDK** — 34 or higher
- **Minimum SDK** — 24 (Android 7.0) or higher*
- **Kotlin** — 2.1.0 or later
- **Jetpack Compose** — 1.5.0 or later

*Higher minimum SDK may be required depending on Seam integration modules used (see [Seam SDK Requirements](../README.md#requirements)).

---

## Installation

### 1. Add GitHub Packages Repository

Add the Seam GitHub Packages repository to your project's `settings.gradle.kts` (or `settings.gradle`):

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/seampkg/seam-mobile-sdk")
            credentials {
                username = project.findProperty("seamUsername") as String? ?: System.getenv("SEAM_USERNAME")
                password = project.findProperty("seamPat") as String? ?: System.getenv("SEAM_PAT")
            }
        }
    }
}
```

### 2. Configure Credentials

Create a `local.properties` file in your project root with your GitHub credentials:

```properties
# local.properties (DO NOT COMMIT THIS FILE)
seamUsername=YOUR_GITHUB_USERNAME
seamPat=YOUR_SEAM_PROVIDED_PAT
```

**Important:** Contact Seam to obtain a Personal Access Token (PAT) with `read:packages` scope.

### 3. Add Dependencies

Add SeamComponents to your app's `build.gradle.kts`:

```kotlin
dependencies {
    // Seam Components (UI)
    implementation("co.seam:seam-phone-sdk-android-seamcomponents:$seamVersion")

    // Seam SDK Core (required)
    implementation("co.seam:seam-phone-sdk-android-core:$seamVersion")

    // Integration modules (add as needed)
    implementation("co.seam:seam-phone-sdk-android-saltoks:$seamVersion")
    implementation("co.seam:seam-phone-sdk-android-saltospace:$seamVersion")
    implementation("co.seam:seam-phone-sdk-android-latch:$seamVersion")
    implementation("co.seam:seam-phone-sdk-android-assaabloy:$seamVersion")
}
```

---

## Quick Start

The fastest way to get started is with `SeamAccessView`, which orchestrates all underlying components to deliver a complete unlock experience:

```kotlin
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import co.seam.seamcomponents.SeamAccessView

@Composable
fun MyAccessScreen() {
    SeamAccessView(
        clientSessionToken = "seam_cst_your_token_here",
        modifier = Modifier.fillMaxSize()
    )
}
```

**Note:** You'll need a valid Client Session Token (CST) from your Seam backend. See the [Authentication](#authentication) section below.

That's it — you now have a fully functional unlock UI in your Android app.

---

## Authentication

Seam Components require a **Client Session Token (CST)** to authenticate with Seam's backend services. This token:

- Must be obtained from your backend server after user authentication
- Should start with `seam_cst_`
- Provides secure access to user credentials and unlock capabilities

Contact your Seam integration team for guidance on CST generation and management.

## How It Works with Seam Mobile SDK

`SeamAccessView` automatically integrates with the Seam Mobile SDK for:

- **SDK initialization** — Automatic setup with your CST
- **Device discovery** — Bluetooth/NFC scanning and connection
- **Credential management** — Automatic sync and caching
- **Unlock flows** — Provider-specific unlock sequences
- **Error handling** — Network, permission, and hardware error recovery

The components handle SDK initialization, activation, and state management transparently.

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
