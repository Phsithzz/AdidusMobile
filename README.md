# Adidus Sneaker E-Commerce (Android Mobile App)

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0+-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Architecture](https://img.shields.io/badge/Architecture-MVVM-brightgreen)](https://developer.android.com/topic/architecture)
[![Backend](https://img.shields.io/badge/Backend-Node.js%20%2B%20Express-339933?logo=nodedotjs&logoColor=white)](https://expressjs.com)
[![Database](https://img.shields.io/badge/Database-PostgreSQL-4169E1?logo=postgresql&logoColor=white)](https://www.postgresql.org)

Modern, high-performance Android mobile application for sneaker and athletic footwear e-commerce built natively with **Kotlin**, **Jetpack Compose (Material 3)**, and **Retrofit**, connected with the fullstack **AdidusWeb** backend (Express & PostgreSQL).

---

## 🌟 Key Features

- **🛍️ Product Discovery & Real-Time Search**:
  - Filter by category tabs: *All*, *Sneaker*, *Football*, *FlipFlops*.
  - Real-time search bar filtering across name, brand, and footwear styles.
  - Interactive product cards with dynamic pricing and wishlist toggle.
- **👟 Variants & Size Selection**:
  - Full shoe size variant picker (UK 7.0 – 11.5) with real-time stock availability.
  - High-res product showcase gallery with related product previews.
  - Multi-variant "Add to Cart" and "Buy Now" workflows.
- **🛒 Cart Management**:
  - Real-time cart synchronization with backend (`GET /cart`, `POST /cart/items`).
  - Dynamic quantity adjustments (+ / -), item deletion, and subtotal calculation.
  - Bottom navigation badge indicator reflecting live cart item count.
- **💳 Checkout & Delivery**:
  - Complete shipping address form (House number, Subdistrict, District, Province, Postal code).
  - Flexible payment method selector (*Cash on Delivery*, *PromptPay QR*, *Credit Card*).
  - Single-transaction atomic checkout (`POST /cart/checkout`) with confirmation alert.
- **📦 Order History**:
  - Real-time view of past placed orders (`GET /cart/orders`).
  - Itemized purchase breakdown with sizes, quantities, and delivery status badges.
- **🔐 Secure Authentication & Session**:
  - User registration and login with JWT token persistence via `SessionManager`.
  - Auto-attaching `Authorization: Bearer <token>` and `Cookie: token=<token>` via OkHttp Interceptors.
- **⚙️ Profile & Developer Mock Mode**:
  - Host switcher: Configure backend IP (Android Emulator `10.0.2.2:3000` vs LAN Wi-Fi `192.168.x.x:3000`).
  - **Mock Mode toggle**: Test and present all features seamlessly offline without requiring a live PostgreSQL database.

---

## 📐 Architecture & Tech Stack

```text
app/src/main/java/com/example/adidusshop/
├── MainActivity.kt               # Navigation Host & Session Auto-check
├── models/                       # Data classes (Product, Variant, CartItem, Order)
│   ├── requestModel/             # Login, Register, AddCart, Checkout
│   └── responseModel/            # Login, Register, User, Cart, Order
├── services/                     # Network Layer & Persistence
│   ├── ApiService.kt             # Retrofit Interface & OkHttp Auth Interceptor
│   ├── SessionManager.kt         # SharedPreferences session & token storage
│   └── MockData.kt               # Offline fallback catalog & sample orders
├── viewModels/                   # State Holders (MVVM)
│   ├── AuthViewModel.kt          # Login, Register, Logout
│   ├── ProductViewModel.kt       # Catalog, Search, Categories, Detail
│   ├── CartViewModel.kt          # Cart items, Quantity, Badge, Subtotal
│   ├── OrderViewModel.kt         # Checkout transaction, Order History
│   ├── CurrencyUtil.kt           # Thai Baht (฿) currency formatter
│   └── States                    # UI State models (LoginState, CartState, OrderState)
├── screens/                      # Jetpack Compose UI
│   ├── MainScreen.kt             # Material 3 Bottom Navigation Shell
│   ├── HomeProduct.kt            # Product catalog & Search bar
│   ├── ProductDetail.kt          # Shoe size variants selector & Buy Now
│   ├── CartScreen.kt             # Cart list & Subtotal summary
│   ├── CheckoutScreen.kt         # Delivery address form & Payment
│   ├── OrderHistoryScreen.kt     # Past orders log
│   ├── ProfileScreen.kt          # User info, Server switcher & Mock toggle
│   ├── LoginScreen.kt            # Sign In form
│   └── SignUpScreen.kt           # Sign Up form
└── ui/theme/                     # Design tokens & Material 3 Theme
```

---

## 🛠️ Getting Started

### Prerequisites
- Android Studio Ladybug / Meerkat (or IntelliJ IDEA with Android plugin)
- Android SDK 34+ (compileSdk: 36, minSdk: 24)
- JDK 17
- (Optional) Running Express backend from [AdidusWeb](../AdidusWeb)

### Run Unit Tests
```bash
./gradlew testDebugUnitTest
```

### Build & Install App
```bash
./gradlew assembleDebug
```
The APK will be generated at `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🧪 Unit Tests Included
- `CurrencyUtilTest`: Validates Thai Baht (฿) formatting across amounts.
- `ModelTest`: Tests integrity of Product, ProductVariant, and Cart subtotal calculation.
- `MockDataTest`: Verifies catalog schema and offline demo readiness.
