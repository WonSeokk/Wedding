# Wedding Invitation Web App

A beautiful wedding invitation website built with Kotlin Multiplatform and Compose for Web, featuring Firebase Realtime Database integration for interactive like functionality.

## 🌟 Features

- **Responsive Design**: Works perfectly on desktop and mobile devices
- **Interactive Animations**: Beautiful animations and transitions with floating heart effects
- **Photo Gallery**: Elegant photo gallery with shared element transitions
- **Video Content**: Integrated video player for special moments
- **Interactive Like System**: Firebase-powered like counter with heart animations
- **Location Integration**: Interactive map with navigation options
- **Multi-language Support**: Korean and English text support

## 🏗️ Technology Stack

- **Frontend**: Kotlin Multiplatform + Compose for Web (WASM)
- **Backend**: Firebase Realtime Database
- **Deployment**: GitHub Pages
- **CI/CD**: GitHub Actions

## 🚀 Getting Started

### Prerequisites

- JDK 17 or higher
- Gradle 8.9+
- Modern web browser with WASM support

### Local Development

1. Clone the repository:
```bash
git clone https://github.com/wonseokk/Wedding.git
cd Wedding
```

2. Run the development server:
```bash
./gradlew :composeApp:wasmJsBrowserDevelopmentRun
```

3. Open your browser and navigate to the provided local URL (usually `http://localhost:8080`)

## 💖 Firebase Like System

### Features
- **❤️ Heart Button**: Express love and congratulations
- **🙏 Blessing Button**: Send blessings and prayers
- **Real-time Counters**: Live count updates from Firebase
- **Floating Animations**: Beautiful heart floating effects on click

### Basic Usage

```kotlin
// Increment like count
val success = likeManager.incrementLike("heart")

// Get current count
val currentCount = likeManager.getLikeCount("heart")
```

### Testing

Open browser developer tools (F12) and test:

```javascript
// Test heart likes
await window.incrementLike('heart');
const heartCount = await window.getLikeCount('heart');
console.log('Heart count:', heartCount);

// Test blessing likes
await window.incrementLike('blessing');
const blessingCount = await window.getLikeCount('blessing');
console.log('Blessing count:', blessingCount);
```

## 🔧 Firebase Setup

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com)
2. Enable Realtime Database
3. Configure security rules (see `firebase-setup.md`)
4. Update Firebase config in `index.html`

## 📁 Project Structure

```
Wedding/
├── composeApp/
│   ├── src/
│   │   ├── commonMain/kotlin/
│   │   │   └── com/wwon_seokk/wedding/
│   │   │       ├── LikeManager.kt      # Firebase integration
│   │   │       ├── LikeSection.kt      # Like UI components
│   │   │       └── FloatingIconEffect.kt # Heart animations
│   │   ├── wasmJsMain/
│   │   │   ├── kotlin/                 # WASM-specific code
│   │   │   └── resources/
│   │   │       ├── index.html          # Firebase SDK integration
│   │   │       └── asset/              # Images and videos
│   │   └── commonMain/composeResources/ # App resources
│   └── build.gradle.kts
├── .github/workflows/ci.yml            # GitHub Actions deployment
├── firebase-setup.md                   # Firebase configuration guide
└── README.md
```

## 🎨 Customization

### Adding New Like Types

```kotlin
// In LikeSection.kt
Column(horizontalAlignment = Alignment.CenterHorizontally) {
    FloatingIconEffect(
        onClick = {
            scope.launch {
                if (likeManager.incrementLike("celebration")) {
                    celebrationCount = likeManager.getLikeCount("celebration")
                }
            }
        }
    ) { }
    Text(text = "$celebrationCount 🎉")
}
```

### Customizing Animations

Modify `FloatingIconEffect.kt` to adjust:
- Animation duration
- Float height
- Wave amplitude
- Animation easing

## 🚀 Deployment

The project automatically deploys to GitHub Pages when pushing to the main branch.

**Manual Build:**
```bash
./gradlew :composeApp:wasmJsBrowserDistribution
```

## 📱 Browser Support

**Supported Browsers:**
- Chrome 119+
- Firefox 120+
- Safari 17+
- Edge 119+

**Note**: WebAssembly (WASM) support is required.

## 🛡️ Security

- Firebase security rules prevent unauthorized access
- Domain restrictions limit usage to approved domains
- Transaction-based increments prevent race conditions

## 📄 License

This project is created for personal use. Please respect the privacy of the wedding couple.

---

**Live Demo**: [https://wonseokk.github.io/Wedding/](https://wonseokk.github.io/Wedding/)

**🎉 Visitors can now express their love and blessings with interactive heart animations!**
