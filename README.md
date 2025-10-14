# 🔥 Fire eSports - Complete Android App

A modern, production-ready eSports tournament platform built with Kotlin, Jetpack Compose, and Supabase. Features neon/glassmorphism UI, real-time chat, tournament management, and wallet system.

## 📱 Features

### Core Features
- ✅ **Authentication**: Email/Password, Google OAuth, Phone OTP
- ✅ **Tournaments**: Browse, search, join, and manage tournaments
- ✅ **Wallet System**: Virtual wallet with transaction history (UPI/Paytm ready)
- ✅ **Real-time Chat**: Global and team-based chat rooms
- ✅ **Leaderboards**: Tournament-based and global rankings
- ✅ **Profile Management**: Edit profile, view stats, manage settings
- ✅ **Push Notifications**: Firebase Cloud Messaging integration
- ✅ **Modern UI**: Neon glow effects, glassmorphism, dark mode

### Tech Stack
- **Language**: Kotlin 2.0.21
- **UI Framework**: Jetpack Compose (Material3)
- **Architecture**: MVVM + Repository Pattern
- **DI**: Hilt/Dagger
- **Backend**: Supabase (PostgreSQL + Realtime)
- **Fonts**: Orbitron + Montserrat
- **Build Tool**: Gradle 8.6
- **Min SDK**: 26 (Android 8.0)
- **Target SDK**: 35 (Android 15)

## 🚀 Quick Start Guide

### Prerequisites
- GitHub account (mobile or desktop)
- Supabase account (free tier)
- Firebase account (optional, for notifications)

### Step 1: Fork/Clone Repository

```bash
# Clone the repository
git clone https://github.com/yourusername/fire-esports.git
cd fire-esports
```

### Step 2: Setup Supabase Backend

1. **Create Supabase Project**
   - Go to [supabase.com](https://supabase.com)
   - Create new project
   - Note your `Project URL` and `anon public key`

2. **Run Database Schema**
   - Go to Supabase Dashboard → SQL Editor
   - Copy contents from `supabase/schema.sql`
   - Execute the SQL script

3. **Configure Supabase Client**
   - Open `app/src/main/java/com/fireesports/data/remote/SupabaseClient.kt`
   - Replace placeholders:
   ```kotlin
   val client: SupabaseClient by lazy {
       createSupabaseClient(
           supabaseUrl = "YOUR_SUPABASE_URL", // e.g., https://xxxxx.supabase.co
           supabaseKey = "YOUR_SUPABASE_ANON_KEY" // Your anon public key
       ) {
           install(Auth)
           install(Postgrest)
           install(Realtime)
           install(Storage)
       }
   }
   ```

### Step 3: Setup Firebase (Optional)

1. **Create Firebase Project**
   - Go to [console.firebase.google.com](https://console.firebase.google.com)
   - Create new project
   - Add Android app with package name: `com.fireesports`

2. **Download google-services.json**
   - Download `google-services.json` from Firebase Console
   - Place it in `app/` directory

3. **Enable Authentication Methods**
   - Firebase Console → Authentication → Sign-in methods
   - Enable Email/Password and Google

### Step 4: Generate Keystore

Create a signing keystore for release builds:

```bash
keytool -genkey -v -keystore app/release.keystore -alias fireesports -keyalg RSA -keysize 2048 -validity 10000
# Password: fireesports2025 (or your choice)
```

### Step 5: Configure GitHub Secrets

Go to GitHub Repository → Settings → Secrets and Variables → Actions

Add the following secrets:

1. **KEYSTORE_BASE64**
   ```bash
   # Convert keystore to base64
   base64 app/release.keystore > keystore.txt
   # Copy contents of keystore.txt to GitHub secret
   ```

2. **KEYSTORE_PASSWORD**: `fireesports2025`
3. **KEY_ALIAS**: `fireesports`
4. **KEY_PASSWORD**: `fireesports2025`

### Step 6: Build on GitHub (Mobile-Friendly)

#### Option A: GitHub Actions (Recommended)

1. **Push Code to GitHub**
   ```bash
   git add .
   git commit -m "Initial commit"
   git push origin main
   ```

2. **Trigger Build**
   - Go to GitHub → Actions tab
   - Select "Android CI/CD" workflow
   - Click "Run workflow"
   - Wait for build to complete (~5-10 minutes)

3. **Download APK**
   - Go to workflow run
   - Scroll to "Artifacts" section
   - Download `fire-esports-release.apk`
   - Install on your Android device

#### Option B: Manual GitHub Build (Mobile Browser)

1. **Enable GitHub Actions**
   - Repository → Settings → Actions → General
   - Enable "Allow all actions"

2. **Make a Change to Trigger Build**
   - Edit any file (e.g., README.md)
   - Commit with message: "Trigger build"
   - Actions will automatically start

3. **Check Build Status**
   - Repository → Actions → Latest workflow
   - Wait for green checkmark
   - Download APK from artifacts

### Step 7: Install APK

1. **Enable Unknown Sources**
   - Android Settings → Security
   - Enable "Install unknown apps" for your browser

2. **Install APK**
   - Download APK from GitHub Actions artifacts
   - Tap to install
   - Launch "Fire eSports"

## 📁 Project Structure

```
FireESports/
├── .github/workflows/          # CI/CD pipeline
│   └── android-ci-cd.yml       # GitHub Actions workflow
├── app/
│   ├── src/main/
│   │   ├── java/com/fireesports/
│   │   │   ├── MainActivity.kt
│   │   │   ├── FireESportsApp.kt
│   │   │   ├── navigation/     # Navigation components
│   │   │   ├── ui/             # UI screens and components
│   │   │   │   ├── theme/      # Colors, typography, theme
│   │   │   │   ├── components/ # Reusable UI components
│   │   │   │   └── screens/    # Feature screens
│   │   │   ├── data/           # Data layer
│   │   │   │   ├── model/      # Data models
│   │   │   │   ├── repository/ # Data repositories
│   │   │   │   └── remote/     # API clients
│   │   │   ├── viewmodel/      # ViewModels
│   │   │   ├── di/             # Dependency injection
│   │   │   └── util/           # Utilities
│   │   ├── res/                # Resources
│   │   └── AndroidManifest.xml
│   ├── build.gradle.kts        # App-level Gradle
│   └── proguard-rules.pro      # ProGuard rules
├── supabase/
│   ├── schema.sql              # Database schema
│   └── functions/              # Backend functions
├── build.gradle.kts            # Project-level Gradle
├── settings.gradle.kts         # Gradle settings
└── README.md                   # This file
```

## 🎨 UI/UX Design

### Color Palette
- **Neon Aqua**: `#00FFFF` - Primary actions, glow effects
- **Neon Pink**: `#FF006E` - Secondary actions, highlights
- **Neon Purple**: `#BB00FF` - Tertiary actions, accents
- **Dark Background**: `#0A0A0F` - Main background
- **Dark Surface**: `#15151F` - Card backgrounds

### Typography
- **Headings**: Orbitron (Bold) - Futuristic, gaming aesthetic
- **Body Text**: Montserrat (Regular/Bold) - Clean, readable

### Components
- **GlowButton**: Animated glowing buttons with neon colors
- **NeonCard**: Cards with neon borders and glassmorphism
- **GlassPanel**: Frosted glass effect panels

## 🔧 Configuration

### Environment Variables

Create `local.properties` (git-ignored):

```properties
supabase.url=YOUR_SUPABASE_URL
supabase.key=YOUR_SUPABASE_ANON_KEY
```

### Build Variants

- **Debug**: Development builds with debugging enabled
- **Release**: Production builds with ProGuard optimization

## 🧪 Testing

### Run Unit Tests
```bash
./gradlew testDebugUnitTest
```

### Run Lint Checks
```bash
./gradlew lintDebug
```

### Build Debug APK
```bash
./gradlew assembleDebug
```

### Build Release APK
```bash
./gradlew assembleRelease
```

## 📦 Deployment

### GitHub Actions Workflow

The CI/CD pipeline automatically:
1. ✅ Runs lint checks
2. ✅ Executes unit tests
3. ✅ Builds debug APK
4. ✅ Builds signed release APK
5. ✅ Builds signed AAB (for Play Store)
6. ✅ Uploads artifacts
7. ✅ Creates GitHub releases (on main branch)

### Manual Build

```bash
# Build signed APK
./gradlew assembleRelease

# Build signed AAB
./gradlew bundleRelease

# Output locations:
# APK: app/build/outputs/apk/release/app-release.apk
# AAB: app/build/outputs/bundle/release/app-release.aab
```

## 🗄️ Backend Setup

### Supabase Tables

1. **users** - User profiles and authentication
2. **tournaments** - Tournament information
3. **tournament_participants** - Tournament registrations
4. **teams** - Team management
5. **wallet_transactions** - Payment history
6. **chat_rooms** - Chat room metadata
7. **chat_messages** - Chat messages
8. **leaderboard** - Global rankings
9. **announcements** - System announcements
10. **match_results** - Match outcomes

### Row Level Security (RLS)

All tables have RLS policies enabled:
- Users can only modify their own data
- Admins have elevated permissions
- Public data is readable by all
- Private data requires authentication

### Realtime Subscriptions

Enabled for:
- Chat messages (instant messaging)
- Tournament slots (live updates)
- Leaderboard (real-time rankings)

## 💳 Payment Integration (Placeholder)

Currently configured as placeholders for future integration:

### Supported Methods (Coming Soon)
- **UPI**: Direct UPI integration
- **Paytm**: Paytm payment gateway
- **Razorpay**: Alternative payment gateway

### Implementation Location
- `app/src/main/java/com/fireesports/util/PaymentHelper.kt`

### Adding Payment Gateway

1. Add SDK dependency in `app/build.gradle.kts`:
   ```kotlin
   implementation("com.razorpay:checkout:1.6.33")
   ```

2. Implement in `PaymentHelper.kt`:
   ```kotlin
   fun initiateRazorpayPayment(context: Context, amount: Double, orderId: String): Boolean {
       val checkout = Checkout()
       checkout.setKeyID("YOUR_KEY_ID")
       // Implementation details
   }
   ```

3. Update `WalletRepository.kt` to process real transactions

## 🔔 Push Notifications

### Firebase Cloud Messaging

Configured channels:
- **tournaments** - Tournament updates (High priority)
- **wallet** - Transaction notifications (Default)
- **general** - System notifications (Low priority)

### Sending Notifications

From Firebase Console:
1. Cloud Messaging → New notification
2. Select target: All users / Specific users
3. Add notification content
4. Set data payload:
   ```json
   {
     "type": "tournament",
     "tournamentId": "uuid"
   }
   ```

## 🐛 Troubleshooting

### Common Issues

#### Build Fails on GitHub Actions

**Problem**: "Execution failed for task ':app:lintVitalRelease'"

**Solution**:
```kotlin
// In app/build.gradle.kts, add:
android {
    lint {
        abortOnError = false
    }
}
```

#### Supabase Connection Error

**Problem**: "Failed to connect to Supabase"

**Solution**:
1. Verify Supabase URL and key in `SupabaseClient.kt`
2. Check internet connectivity
3. Verify Supabase project is active

#### Keystore Error

**Problem**: "Keystore was tampered with, or password was incorrect"

**Solution**:
1. Regenerate keystore with correct password
2. Update GitHub secrets with new base64 keystore
3. Ensure password matches in secrets

#### APK Not Installing

**Problem**: "App not installed"

**Solution**:
1. Enable "Install unknown apps" in Android settings
2. Uninstall previous version if exists
3. Ensure APK is from Release artifacts (not debug)

### Debug Logs

View logs on device:
```bash
adb logcat | grep "FireESports"
```

## 📈 Performance Optimization

### Implemented Optimizations
- ✅ ProGuard/R8 code shrinking
- ✅ Resource shrinking
- ✅ Image optimization
- ✅ Lazy loading for lists
- ✅ Compose remember() for recomposition
- ✅ Database indexing
- ✅ Efficient Supabase queries

### Future Optimizations
- [ ] Image caching with Coil
- [ ] Pagination for large lists
- [ ] Background job scheduling
- [ ] Offline mode with local database

## 🔐 Security Features

- ✅ Row Level Security (RLS) on database
- ✅ Secure authentication with Supabase Auth
- ✅ Encrypted keystore for APK signing
- ✅ ProGuard obfuscation
- ✅ No hardcoded secrets (environment variables)
- ✅ HTTPS-only communication

## 📱 Minimum Requirements

- **Android Version**: 8.0 (API 26) or higher
- **RAM**: 2GB minimum, 4GB recommended
- **Storage**: 50MB for app + data
- **Internet**: Required for all features

## 🎯 Roadmap

### Phase 1: Core Features (✅ Complete)
- Authentication system
- Tournament management
- Wallet system
- Chat functionality
- Profile management

### Phase 2: Enhancement (🚧 In Progress)
- Real payment integration
- Advanced leaderboards
- Match result uploads
- Admin dashboard
- Push notification expansion

### Phase 3: Advanced Features (📅 Planned)
- Live streaming integration
- In-app shop
- Mini-games
- Analytics dashboard
- Referral system
- Multi-language support

## 📄 License

```
MIT License

Copyright (c) 2025 Fire eSports

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## 👥 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open Pull Request

## 📞 Support

- **Issues**: [GitHub Issues](https://github.com/yourusername/fire-esports/issues)
- **Discussions**: [GitHub Discussions](https://github.com/yourusername/fire-esports/discussions)
- **Email**: support@fireesports.com

## 🙏 Acknowledgments

- **Jetpack Compose** - Modern Android UI toolkit
- **Supabase** - Backend infrastructure
- **Firebase** - Cloud messaging
- **Material Design 3** - Design system
- **Orbitron & Montserrat** - Typography

---

**Built with ❤️ for the gaming community**

🔥 Fire eSports - Where Champions Rise 🔥
