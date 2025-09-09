# Seam Components - How to Use

This guide provides comprehensive documentation on how to use the Seam Components library, which offers both complete screen solutions and individual UI components for building credential management applications.

## Table of Contents

1. [Quick Start - Complete Integration](#quick-start---complete-integration)
2. [Screen Components](#screen-components)
3. [UI Components](#ui-components)
   - [Common Components](#common-components)
   - [Key Components](#key-components)
   - [Unlock Components](#unlock-components)
   - [Bluetooth Components](#bluetooth-components)
4. [Theming and Customization](#theming-and-customization)
5. [Advanced Usage](#advanced-usage)

## Quick Start - Complete Integration

### SeamAccessView - Your Complete Solution

`SeamAccessView` is the main entry point composable for the Seam access management interface. This powerful component manages the entire user flow for accessing and managing credentials through the Seam SDK, making it the perfect choice for most applications.

**What SeamAccessView handles for you:**
- **SDK Initialization**: Automatically initializes the Seam SDK with your session token
- **Navigation Management**: Handles screen transitions and navigation state
- **UI State Management**: Manages loading, error handling, and different application states
- **OTP Authorization**: Automatically handles OTP verification flows when required
- **Bluetooth Permissions**: Guides users through Bluetooth setup when needed for credential operations

**Screen Orchestration:**
SeamAccessView automatically navigates between these screens based on application state:
- **Credentials List Screen**: For viewing available keys with pull-to-refresh functionality
- **OTP Authorization Screen**: For completing authentication flows in a WebView
- **Bluetooth Redirect Screen**: For handling Bluetooth permission and setup
- **Unlock Overlay**: For interacting with individual credentials through a modal interface

### Basic Usage

For most use cases, you can use `SeamAccessView` as a complete solution:

```kotlin
@Composable
fun MyApp() {
    SeamThemeProvider {
        SeamAccessView(
            clientSessionToken = "your-session-token-here"
        )
    }
}
```

### With Custom Navigation

If you need more control over navigation or want to integrate with your existing navigation setup:

```kotlin
@Composable
fun MyApp() {
    val navController = rememberNavController()

    SeamThemeProvider {
        SeamAccessView(
            clientSessionToken = "your-session-token-here",
            context = LocalContext.current,
            navController = navController
        )
    }
}
```

**Parameters:**
- `clientSessionToken`: Required session token for SDK authentication and initialization
- `context`: Android context (defaults to current composition's local context)
- `navController`: Navigation controller for screen transitions (defaults to a new instance)

## Screen Components

When you need more granular control than `SeamAccessView` provides, you can use individual screen components to build custom flows while still benefiting from Seam's pre-built UI.

### SeamCredentialsView

**What it does:** A composable screen that displays cards for user credentials (keys) with comprehensive state management and user interaction support.

**Key Features:**
- **State Management**: Automatically handles loading, success with data, and empty states
- **Pull-to-Refresh**: Built-in refresh functionality for updating credential data
- **Error Handling**: Displays appropriate user feedback for error states
- **Navigation Integration**: Provides callback for seamless navigation to unlock interfaces
- **Internet Status**: Shows connection status and handles offline scenarios

**When to use:** When you want a complete credentials listing screen but need to customize the navigation or integrate with your own view models and navigation system.

```kotlin
@Composable
fun CredentialsScreen() {
    // viewModel is optional
    val viewModel: KeysViewModel = viewModel()

    SeamCredentialsView(
        viewModel = viewModel,
        onNavigateToUnlock = { keyCard ->
            // Navigate to unlock screen
            println("Unlocking key: ${keyCard.name}")
        }
    )
}
```

### SeamUnlockCardView

**What it does:** A modal bottom sheet composable that provides a complete user interface for unlocking credentials with phase-based state management.

**Key Features:**
- **Modal Presentation**: Displays as an overlay bottom sheet that doesn't disrupt the main UI
- **Unlock Phases**: Manages different states (idle, scanning, success, failed) with appropriate UI feedback
- **Theme Integration**: Supports customization through SeamUnlockCardStyle theming
- **Automatic State Reset**: Handles cleanup when dismissed or navigation occurs
- **Error Recovery**: Built-in error states with retry functionality

**When to use:** When you want to provide unlock functionality in a modal format, either triggered from your custom credential list or integrated into your own navigation flow.

```kotlin
@Composable
fun UnlockModal() {
    var showUnlock by remember { mutableStateOf(false) }
    val keyCard = KeyCard(
        id = "key-1",
        location = "Grand Hotel & Spa",
        name = "1205",
        checkoutDate = LocalDateTime.now().plusDays(2),
        code = "1234"
    )

    Button(onClick = { showUnlock = true }) {
        Text("Show Unlock Modal")
    }

    if (showUnlock) {
        SeamUnlockCardView(
            keyCard = keyCard,
            onNavigateBack = { showUnlock = false }
        )
    }
}
```

### SeamOtpView

**What it does:** A full-screen dialog composable that displays an OTP (One-Time Password) verification interface using a WebView for interactive authentication flows.

**Key Features:**
- **Full-Screen Presentation**: Renders as a dialog that overlays the host app's UI completely
- **WebView Integration**: Handles OTP verification flows with JavaScript enabled for interactivity
- **Custom Navigation**: Includes a styled top bar with back navigation controls
- **URL Handling**: Loads the provided OTP URL and manages the verification process
- **Dialog Management**: Proper dismissal handling that integrates with your app's navigation

**When to use:** When your authentication flow requires OTP verification and you want a seamless, full-screen experience that doesn't disrupt your main app navigation.

```kotlin
@Composable
fun OtpScreen() {
    SeamOtpView(
        otpUrl = "https://example.com/otp-verification",
        onNavigateBack = {
            // Handle navigation back
        }
    )
}
```

## UI Components

For maximum flexibility, Seam provides individual UI components that you can combine to build completely custom interfaces. These components are organized into categories based on their functionality and can be mixed and matched according to your needs.

**Component Categories:**
- **[Common Components](#common-components)**: Essential UI elements like loading states, errors, and buttons
- **[Key Components](#key-components)**: Components for displaying and managing credentials
- **[Unlock Components](#unlock-components)**: Specialized components for the unlock process
- **[Bluetooth Components](#bluetooth-components)**: Components for Bluetooth setup and permissions

### Common Components

Essential UI components for handling common application states and user interactions.

#### LoadingContent

Display loading states with customizable text:

```kotlin
@Composable
fun MyLoadingScreen() {
    LoadingContent(
        title = "Loading your mobile keys..."
    )
}

// With default loading text
@Composable
fun SimpleLoading() {
    LoadingContent()
}
```

#### ErrorContent

Show error states with retry functionality:

```kotlin
@Composable
fun MyErrorScreen() {
    ErrorContent(
        errorState = SeamErrorState.InternetConnectionRequired,
        onRetry = {
            // Retry the failed operation
        }
    )
}
```

#### ErrorBanner

Dismissible error banner with animations:

```kotlin
@Composable
fun MyScreen() {
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Column {
        ErrorBanner(
            errorMessage = errorMessage,
            onDismiss = { errorMessage = null }
        )

        // Your main content
        Button(
            onClick = { errorMessage = "Something went wrong!" }
        ) {
            Text("Trigger Error")
        }
    }
}
```

#### Buttons

Seam provides three button variants:

```kotlin
@Composable
fun ButtonExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Primary button (full-width)
        SeamPrimaryButton(
            buttonText = "Unlock Door",
            onClick = { /* Primary action */ }
        )

        // Secondary button (full-width with border)
        SeamSecondaryButton(
            buttonText = "Cancel",
            onClick = { /* Secondary action */ }
        )

        // Custom button with full control
        SeamButton(
            text = "Custom Button",
            backgroundColor = Color.Red,
            textColor = Color.White,
            borderColor = Color.DarkRed,
            isFullWidth = false,
            onClick = { /* Custom action */ }
        )
    }
}
```

### Key Components

Components specifically designed for displaying and managing credential keys.

#### KeyCardComponent

**What it does:** A composable that displays a stylized key card with gradient background and comprehensive credential information. This is the visual representation of your mobile keys.

**Key Features:**
- **Rich Visual Design**: Gradient backgrounds with customizable theming through SeamKeyCardStyle
- **Hotel/Location Information**: Displays room names, access codes, and checkout dates with proper formatting
- **Brand Logo Support**: Optional brand logo display (URL or resource-based) with fallback handling
- **Multiple States**: Supports loading, expired, and active states with appropriate visual indicators
- **Interactive/Display Modes**: Can be configured for interaction or display-only purposes
- **Accessibility**: Proper content descriptions and interaction feedback

**Card States:**
- **Active**: Normal interactive state with full functionality
- **Loading**: Shows loading indicator overlay when credential is being processed
- **Expired**: Displays error indicator and disables interaction for expired credentials

```kotlin
@Composable
fun KeyCardExample() {
    val keyCard = KeyCard(
        id = "key-1",
        location = "Grand Hotel & Spa",
        name = "1205",
        checkoutDate = LocalDateTime.now().plusDays(2),
        code = "1234"
    )

    KeyCardComponent(
        keyCard = keyCard,
        onPress = {
            // Handle key card tap
            println("Key card pressed: ${keyCard.name}")
        }
    )
}

// Disabled/display-only card
@Composable
fun DisplayOnlyCard() {
    KeyCardComponent(
        keyCard = keyCard,
        onPress = null // No interaction
    )
}
```

#### EmptyKeysState

**What it does:** A composable that displays an empty state when no keys are available, providing user guidance and refresh functionality.

**Key Features:**
- **Informative Design**: Shows illustration, title, and subtitle to inform users about the empty state
- **Refresh Action**: Includes a refresh button to allow users to retry loading keys
- **Consistent Styling**: Integrates with Seam theme for consistent visual appearance
- **User Guidance**: Clear messaging about why no keys are shown and what users can do

**When to use:** Display this when credential loading results in no available keys, or when users need to refresh their credential list.

```kotlin
@Composable
fun EmptyStateExample() {
    EmptyKeysState(
        onRefresh = {
            // Refresh keys
            println("Refreshing keys...")
        }
    )
}
```

### Unlock Components

Specialized components for building custom unlock interfaces. These components work together to create comprehensive unlock experiences, from user instructions to visual feedback during the unlock process.

**Core Unlock Flow Components:**
- **[CircleSpinner](#circlespinner)**: Animated loading indicator
- **[UnlockButton](#unlockbutton)**: Main interaction button with phase-based states
- **[StatusMessage](#statusmessage)**: Status and instruction text display
- **[UnlockHeader](#unlockheader)**: Key card information display
- **[UnlockContent](#unlockcontent)**: Complete unlock interface (combines multiple components)
- **[UnlockError](#unlockerror)**: Error state with retry functionality
- **[UnlockInstructions](#unlockinstructions)**: Step-by-step user guidance

#### CircleSpinner

**What it does:** A customizable animated circular spinner composable with gradient effects and optional content support.

**Key Features:**
- **Smooth Animation**: Continuous 360-degree rotation with linear easing for smooth visual feedback
- **Gradient Effects**: Uses sweep gradient with customizable colors from SeamUnlockCardStyle theming
- **Flexible Sizing**: Configurable diameter and border width for different use cases
- **Background Ring**: Optional subtle background ring to enhance visual contrast
- **Content Support**: Can display any composable content in the center (text, icons, etc.)
- **Theme Integration**: Colors automatically adapt to your Seam theme configuration

**Customization Options:**
- `size`: Spinner diameter in dp (default: 160)
- `borderWidth`: Stroke width in pixels (default: 16f)
- `showBackgroundRing`: Display background ring (default: false)
- `content`: Optional centered content

```kotlin
@Composable
fun SpinnerExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Basic spinner
        CircleSpinner()

        // Spinner with custom size and background ring
        CircleSpinner(
            size = 120,
            borderWidth = 12f,
            showBackgroundRing = true
        )

        // Spinner with centered content
        CircleSpinner(
            size = 160,
            showBackgroundRing = true
        ) {
            Text(
                text = "Loading...",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
```

#### UnlockButton

**What it does:** A central unlock button that dynamically adapts its appearance and behavior based on the current unlock phase, providing visual feedback throughout the unlock process.

**Key Features:**
- **Phase-Based States**: Different visual representations for each unlock phase
- **Theme Integration**: Fully customizable through SeamUnlockCardStyle theming
- **Smooth Transitions**: Seamless visual transitions between states
- **Accessibility**: Proper content descriptions and interaction feedback

**Visual States:**
- **IDLE**: Gradient button with key icon, ready to initiate unlock
- **SCANNING**: Animated CircleSpinner around key icon during unlock attempt
- **SUCCESS**: Green background with checkmark icon indicating successful unlock
- **FAILED**: No visual representation (error handling is managed by other components)

**Styling Options (via theming):**
- Button gradient colors and key icon colors for different states
- Success color and icon customization
- Shadow and elevation effects

```kotlin
@Composable
fun UnlockButtonExample() {
    var phase by remember { mutableStateOf(UnlockPhase.IDLE) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        UnlockButton(
            unlockPhase = phase,
            onPress = {
                when (phase) {
                    UnlockPhase.IDLE -> phase = UnlockPhase.SCANNING
                    UnlockPhase.SCANNING -> phase = UnlockPhase.SUCCESS
                    UnlockPhase.SUCCESS -> phase = UnlockPhase.IDLE
                    UnlockPhase.FAILED -> phase = UnlockPhase.IDLE
                }
            }
        )

        // Control buttons for demo
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = { phase = UnlockPhase.IDLE }) { Text("Idle") }
            Button(onClick = { phase = UnlockPhase.SCANNING }) { Text("Scanning") }
            Button(onClick = { phase = UnlockPhase.SUCCESS }) { Text("Success") }
            Button(onClick = { phase = UnlockPhase.FAILED }) { Text("Failed") }
        }
    }
}
```

#### StatusMessage

Display status information during unlock:

```kotlin
@Composable
fun StatusMessageExample() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        StatusMessage(
            statusText = "Tap to Unlock",
            instructionText = null
        )

        StatusMessage(
            statusText = "Connecting...",
            instructionText = "Please hold your phone near the lock"
        )

        StatusMessage(
            statusText = "Success!",
            instructionText = "Door unlocked successfully"
        )
    }
}
```

#### UnlockHeader

Display key card information in unlock interfaces:

```kotlin
@Composable
fun UnlockHeaderExample() {
    val keyCard = KeyCard(
        id = "key-1",
        location = "Grand Hotel & Spa",
        name = "Room 1205",
        checkoutDate = LocalDateTime.now().plusDays(2),
        code = "1234"
    )

    UnlockHeader(
        keyCard = keyCard
    )
}
```

#### UnlockContent

Main unlock interface content:

```kotlin
@Composable
fun UnlockContentExample() {
    var phase by remember { mutableStateOf(UnlockPhase.IDLE) }

    UnlockContent(
        unlockPhase = phase,
        onPressPrimaryButton = {
            phase = when (phase) {
                UnlockPhase.IDLE -> UnlockPhase.SCANNING
                UnlockPhase.SCANNING -> UnlockPhase.SUCCESS
                else -> UnlockPhase.IDLE
            }
        }
    )
}
```

#### UnlockError

Display error states with retry:

```kotlin
@Composable
fun UnlockErrorExample() {
    UnlockError(
        title = "Connection Failed",
        description = "Unable to connect to the lock. Please check your connection and try again.",
        onTryAgain = {
            // Retry unlock operation
            println("Retrying unlock...")
        }
    )
}
```

#### UnlockInstructions

Step-by-step unlock instructions:

```kotlin
@Composable
fun InstructionsExample() {
    UnlockInstructions()
}
```

### Bluetooth Components

Components for handling Bluetooth setup and permissions required for credential operations.

#### BluetoothRedirectScreen

**What it does:** A full-screen composable that guides users to enable Bluetooth when it's required for key operations, providing clear instructions and system integration.

**Key Features:**
- **Clear Guidance**: Explains why Bluetooth is needed with informative title and description
- **System Integration**: Includes button that directly opens Android Bluetooth settings
- **Flexible UX**: Optional skip functionality for non-critical Bluetooth requirements
- **Consistent Design**: Follows Seam design patterns with proper spacing and typography
- **Icon Support**: Uses appropriate Bluetooth icon for visual clarity

**When to use:** Display this screen when Bluetooth is disabled but required for credential operations. Commonly used by SeamAccessView automatically, but can be used independently for custom flows.

**Skip vs. Required Modes:**
- **With Skip**: Allows users to proceed without Bluetooth (for non-critical features)
- **Required Mode**: Forces users to enable Bluetooth before proceeding (set `onSkipClicked` to null)

```kotlin
@Composable
fun BluetoothSetup() {
    BluetoothRedirectScreen(
        onSkipClicked = {
            // Handle skip action
            println("User skipped Bluetooth setup")
        }
    )
}

// Without skip option
@Composable
fun BluetoothSetupRequired() {
    BluetoothRedirectScreen(
        onSkipClicked = null // Hide skip button
    )
}
```

## Theming and Customization

Seam Mobile Components are fully customizable using Material 3 theming integrated with Seam's component-specific styling system. You can customize colors, typography, and detailed component styles to match your brand.

### Basic Theme Setup with Material 3

The simplest way to theme Seam components is to use `SeamComponentsTheme` with your Material 3 color schemes:

```kotlin
@Composable
fun MyApp() {
    val lightColors = lightColorScheme(
        primary = Color(0xFF1976D2),
        onPrimary = Color.White,
        secondary = Color(0xFF757575),
        onSecondary = Color.White,
        background = Color.White,
        onBackground = Color(0xFF212121)
    )
    
    val darkColors = darkColorScheme(
        primary = Color(0xFF90CAF9),
        onPrimary = Color(0xFF003C7E),
        secondary = Color(0xFF424242),
        onSecondary = Color.White,
        background = Color(0xFF121212),
        onBackground = Color.White
    )
    
    val colorScheme = if (isSystemInDarkTheme()) darkColors else lightColors
    
    SeamComponentsTheme(
        colorScheme = colorScheme
    ) {
        SeamAccessView(
            clientSessionToken = "your-token"
        )
    }
}
```

### Custom Typography Integration

You can integrate custom typography with Material 3's typography system:

```kotlin
@Composable
fun MyThemedApp() {
    val customTypography = Typography(
        headlineLarge = TextStyle(
            fontFamily = FontFamily.Serif,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp
        ),
        bodyLarge = TextStyle(
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp
        ),
        labelMedium = TextStyle(
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            letterSpacing = 0.5.sp
        )
    )
    
    val colorScheme = lightColorScheme(
        primary = Color(0xFF6366F1),
        onPrimary = Color.White
    )
    
    SeamComponentsTheme(
        colorScheme = colorScheme,
        typography = customTypography
    ) {
        SeamAccessView(
            clientSessionToken = "your-token"
        )
    }
}
```

### Component-Specific Theming

For detailed customization, you can use Seam's component-specific styling with the `seamTheme` parameter:

```kotlin
@Composable
fun BrandedSeamApp() {
    val hotelBranding = SeamTheme(
        keyCard = SeamKeyCardStyle(
            backgroundGradient = listOf(
                Color(0xFF1E3A8A), // Deep blue
                Color(0xFF3B82F6)  // Bright blue
            ),
            accentColor = Color(0xFFEAB308), // Gold accent
            cornerRadius = 16.dp,
            textColor = Color.White,
            shadowColor = Color(0x33000000),
            shadowYOffset = 8.dp,
            brandLogoUrl = "https://example.com/hotel-logo.png"
        ),
        unlockCard = SeamUnlockCardStyle(
            cardBackground = Color(0xFF1F2937),
            headerBackground = Color(0xFF111827),
            headerTitleColor = Color.White,
            keyIconColorIdle = Color(0xFFEAB308),
            keyIconColorActive = Color.White,
            successColor = Color(0xFF10B981)
        )
    )
    
    val colorScheme = lightColorScheme(
        primary = Color(0xFF1E3A8A),
        onPrimary = Color.White
    )
    
    SeamComponentsTheme(
        seamTheme = hotelBranding,
        colorScheme = colorScheme
    ) {
        SeamAccessView(
            clientSessionToken = "your-token"
        )
    }
}
```

### SeamKeyCardStyle Properties

Complete customization options for key card appearance:

```kotlin
SeamKeyCardStyle(
    // Brand Integration
    brandLogoUrl = "https://example.com/logo.png",      // Remote logo URL
    brandLogoRes = R.drawable.brand_logo,               // Local drawable resource
    backgroundTextureRes = R.drawable.card_texture,     // Background texture overlay
    
    // Visual Design
    backgroundGradient = listOf(Color.Blue, Color.Cyan), // Gradient colors (top to bottom)
    accentColor = Color(0xFFFFD700),                     // Accent bar color
    textColor = Color.White,                             // All text color on card
    
    // Layout & Effects
    cornerRadius = 20.dp,                                // Card corner radius
    shadowColor = Color(0x44000000),                     // Shadow color
    shadowYOffset = 6.dp,                                // Shadow vertical offset
    
    // State Colors
    errorColor = Color.Red                               // Error state indicator color
)
```

### SeamUnlockCardStyle Properties

Complete customization options for unlock interface:

```kotlin
SeamUnlockCardStyle(
    // Container Backgrounds
    cardBackground = Color(0xFF1F2937),          // Main card background
    headerBackground = Color(0xFF111827),        // Header section background
    
    // Header Text Colors
    headerTitleColor = Color.White,              // Main header text color
    headerSubtitleColor = Color(0xFFD1D5DB),     // Subtitle text color
    
    // Key Icon Colors
    keyIconColorIdle = Color(0xFFEAB308),        // Key icon when idle
    keyIconColorActive = Color.White,            // Key icon when active
    
    // Button Styling
    keyButtonGradient = listOf(                  // Button gradient colors
        Color(0xFF3B82F6),                       // Top color
        Color(0xFF1E40AF)                        // Bottom color
    ),
    keyButtonShadowColor = Color(0x33000000),    // Button shadow color
    keyButtonShadowRadius = 12.dp,               // Button shadow blur radius
    
    // Instructional Elements
    instructionTextColor = Color(0xFFD1D5DB),    // Instruction text color
    bulletBackground = Color(0xFF3B82F6),        // Bullet point background
    bulletTextColor = Color.White,               // Bullet point text color
    
    // State Colors
    successColor = Color(0xFF10B981),            // Success state color
    successIconColor = Color.White,              // Success icon color
    errorColor = Color(0xFFEF4444)               // Error state color
)
```

### Practical Theming Examples

#### Hotel Chain Branding

```kotlin
@Composable
fun HotelChainTheme() {
    val hotelTheme = SeamTheme(
        keyCard = SeamKeyCardStyle(
            backgroundGradient = listOf(
                Color(0xFF8B5A3C),  // Warm brown
                Color(0xFF6B4226)   // Dark brown
            ),
            accentColor = Color(0xFFD4AF37),        // Gold
            textColor = Color(0xFFF5F5DC),          // Beige
            cornerRadius = 12.dp,
            brandLogoUrl = "https://hotel.com/logo.png",
            shadowColor = Color(0x55000000),
            shadowYOffset = 4.dp
        ),
        unlockCard = SeamUnlockCardStyle(
            cardBackground = Color(0xFF2D1810),      // Dark brown
            headerBackground = Color(0xFF1A0F08),    // Very dark brown
            keyIconColorIdle = Color(0xFFD4AF37),    // Gold
            keyButtonGradient = listOf(
                Color(0xFFD4AF37),                   // Gold
                Color(0xFFB8941F)                    // Dark gold
            ),
            successColor = Color(0xFF22C55E)
        )
    )
    
    SeamComponentsTheme(
        seamTheme = hotelTheme,
        colorScheme = lightColorScheme(primary = Color(0xFF8B5A3C))
    ) {
        SeamAccessView(clientSessionToken = "token")
    }
}
```

#### Corporate Technology Branding

```kotlin
@Composable
fun TechCorpTheme() {
    val techTheme = SeamTheme(
        keyCard = SeamKeyCardStyle(
            backgroundGradient = listOf(
                Color(0xFF0F172A),  // Slate 900
                Color(0xFF1E293B)   // Slate 800
            ),
            accentColor = Color(0xFF06B6D4),        // Cyan
            textColor = Color(0xFFF8FAFC),          // Slate 50
            cornerRadius = 8.dp,                    // Sharp corners
            brandLogoRes = R.drawable.tech_logo,
            shadowColor = Color(0x66000000),
            shadowYOffset = 8.dp
        ),
        unlockCard = SeamUnlockCardStyle(
            cardBackground = Color(0xFF020617),      // Slate 950
            headerBackground = Color(0xFF0F172A),    // Slate 900
            keyIconColorIdle = Color(0xFF06B6D4),    // Cyan
            keyIconColorActive = Color(0xFF67E8F9),  // Cyan 300
            keyButtonGradient = listOf(
                Color(0xFF0891B2),                   // Cyan 600
                Color(0xFF0E7490)                    // Cyan 700
            ),
            successColor = Color(0xFF10B981),        // Emerald 500
            bulletBackground = Color(0xFF06B6D4),
            instructionTextColor = Color(0xFF94A3B8) // Slate 400
        )
    )
    
    SeamComponentsTheme(
        seamTheme = techTheme,
        colorScheme = darkColorScheme(
            primary = Color(0xFF06B6D4),
            background = Color(0xFF020617)
        )
    ) {
        SeamAccessView(clientSessionToken = "token")
    }
}
```

## Advanced Usage

### Custom Key Card Implementation

```kotlin
@Composable
fun CustomKeyList() {
    val keys = listOf(
        KeyCard(id = "1", location = "Hotel A", name = "Room 101", /* ... */),
        KeyCard(id = "2", location = "Hotel B", name = "Room 202", /* ... */)
    )

    LazyColumn {
        items(keys) { keyCard ->
            KeyCardComponent(
                keyCard = keyCard,
                onPress = {
                    // Custom unlock logic
                    handleUnlock(keyCard)
                },
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

private fun handleUnlock(keyCard: KeyCard) {
    // Your custom unlock implementation
}
```

### Building Custom Unlock Flow

```kotlin
@Composable
fun CustomUnlockFlow() {
    var currentPhase by remember { mutableStateOf(UnlockPhase.IDLE) }
    var showError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Text(
            text = "Unlock Room 1205",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Main unlock area
        when {
            showError -> {
                UnlockError(
                    title = "Unlock Failed",
                    description = "Please try again",
                    onTryAgain = {
                        showError = false
                        currentPhase = UnlockPhase.IDLE
                    }
                )
            }
            else -> {
                UnlockButton(
                    unlockPhase = currentPhase,
                    onPress = {
                        performUnlock { success ->
                            if (success) {
                                currentPhase = UnlockPhase.SUCCESS
                            } else {
                                showError = true
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                StatusMessage(
                    statusText = when (currentPhase) {
                        UnlockPhase.IDLE -> "Tap to Unlock"
                        UnlockPhase.SCANNING -> "Connecting..."
                        UnlockPhase.SUCCESS -> "Success!"
                        UnlockPhase.FAILED -> "Failed"
                    },
                    instructionText = when (currentPhase) {
                        UnlockPhase.SCANNING -> "Hold phone near the lock"
                        else -> null
                    }
                )

                if (currentPhase == UnlockPhase.IDLE) {
                    Spacer(modifier = Modifier.height(24.dp))
                    UnlockInstructions()
                }
            }
        }
    }
}

private fun performUnlock(callback: (Boolean) -> Unit) {
    // Simulate unlock process
    // In real implementation, this would interact with the Seam SDK
    callback(true) // or false for failure
}
```

### Error Handling Patterns

```kotlin
@Composable
fun ErrorHandlingExample() {
    var errorState by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column {
        ErrorBanner(
            errorMessage = errorState,
            onDismiss = { errorState = null }
        )

        when {
            isLoading -> {
                LoadingContent(title = "Processing request...")
            }
            errorState != null -> {
                ErrorContent(
                    errorState = SeamErrorState.Unknown,
                    onRetry = {
                        errorState = null
                        // Retry operation
                    }
                )
            }
            else -> {
                // Normal content
                Text("Your content here")
            }
        }
    }
}
```

## Component Relationships and Integration

Understanding how components work together can help you build more effective custom interfaces:

**Complete Flow Components:**
- `SeamAccessView` → Uses `SeamCredentialsView`, `SeamOtpView`, `SeamUnlockCardView`, and `BluetoothRedirectScreen`
- `SeamCredentialsView` → Uses `KeyCardComponent`, `EmptyKeysState`, `LoadingContent`, and `ErrorBanner`
- `SeamUnlockCardView` → Uses `UnlockContent`, `UnlockHeader`, and `UnlockError`
- `UnlockContent` → Uses `UnlockButton`, `StatusMessage`, and `UnlockInstructions`

**Standalone Utility Components:**
- Error handling: `ErrorContent`, `ErrorBanner`
- Loading states: `LoadingContent`, `CircleSpinner`
- Buttons: `SeamButton`, `SeamPrimaryButton`, `SeamSecondaryButton`

---

Whether you choose `SeamAccessView` for a complete out-of-the-box solution or mix and match individual components to build custom credential management interfaces, you now have all the information needed to integrate Seam's powerful mobile credential capabilities into your application.

For additional customization options, refer to your SeamComponentsThemeData configuration and the individual component parameters documented above.
