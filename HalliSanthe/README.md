# 🏺 Halli-Santhe Digital

**Hyper-Local Marketplace for Rural Artisans**

A Kotlin Android application connecting artisans with urban buyers, digitizing the traditional weekly market (santhe).

---

## 📱 Features

### Buyer Module
- Browse products in a 2-column grid (RecyclerView + GridLayoutManager)
- Live search by product name or category
- Detailed product view with collapsing toolbar + artisan info
- Bottom-sheet inquiry/messaging system

### Artisan Module
- Upload product with name, price, category, description, location & artisan name
- Pick product image from gallery (runtime permissions handled)
- View and manage all listings (count badge in dashboard)
- Delete listings with confirmation dialog

---

## 🏗 Architecture

```
MVVM
├── Model       → Room DB (Product, Inquiry entities + DAOs)
├── ViewModel   → ProductViewModel (LiveData, switchMap, coroutines, sealed results)
└── View        → Activities + ViewBinding + RecyclerView adapters
```

---

## 🎨 UI Theme

| Token | Value |
|---|---|
| Primary (Forest green) | `#2E7D32` |
| Dark green | `#1B5E20` |
| Brown earthy | `#5D4037` |
| Brown dark | `#3E2723` |
| Amber accent | `#FF8F00` |
| Surface | `#F9F5F0` |

---

## 🚀 Setup Instructions

### Prerequisites
- Android Studio Hedgehog or newer
- JDK 17
- Android SDK 34

### Steps

1. **Open in Android Studio**
   ```
   File → Open → Select the HalliSanthe folder
   ```

2. **Add Noto Serif fonts** *(optional but recommended)*
   - Download `noto_serif_regular.ttf` and `noto_serif_bold.ttf` from [Google Fonts](https://fonts.google.com/specimen/Noto+Serif)
   - Place them in `app/src/main/res/font/`

3. **Sync Gradle**
   Android Studio will prompt you — click **Sync Now**

4. **Run on Device / Emulator**
   - Min SDK: API 21 (Android 5.0)
   - Click the ▶ Run button

> ⚠️ No Firebase/API keys needed — the app uses a local Room database only.

---

## 📂 Project Structure

```
HalliSanthe/
├── build.gradle                   ← Root build (Kotlin + AGP classpath)
├── settings.gradle
├── gradle.properties
├── .gitignore
└── app/
    ├── build.gradle               ← App deps: Room, Glide, Lifecycle, Material
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml
        ├── java/com/halliSanthe/app/
        │   ├── data/
        │   │   ├── model/
        │   │   │   ├── Product.kt          ← Room entity (id, name, price, category,
        │   │   │   │                           description, location, artisanName, imageUri)
        │   │   │   └── Inquiry.kt          ← Room entity with FK to Product
        │   │   └── repository/
        │   │       ├── AppDatabase.kt      ← Singleton Room DB
        │   │       ├── ProductDao.kt       ← CRUD + search + category queries
        │   │       ├── InquiryDao.kt       ← Insert, mark-read, unread count
        │   │       └── ProductRepository.kt← Single source of truth
        │   ├── viewmodel/
        │   │   └── ProductViewModel.kt     ← switchMap search, sealed OperationResult
        │   └── ui/
        │       ├── SplashActivity.kt       ← Role selection (Buyer / Artisan)
        │       ├── buyer/
        │       │   ├── MainActivity.kt     ← Grid browse + SearchView
        │       │   └── ProductAdapter.kt   ← ListAdapter + DiffUtil
        │       ├── artisan/
        │       │   ├── ArtisanDashboardActivity.kt   ← Listings + FAB
        │       │   ├── ArtisanProductAdapter.kt       ← ListAdapter + delete
        │       │   └── AddProductActivity.kt          ← Form + image picker
        │       └── detail/
        │           └── ProductDetailActivity.kt       ← Collapsing toolbar + inquiry sheet
        └── res/
            ├── layout/
            │   ├── activity_splash.xml
            │   ├── activity_main.xml
            │   ├── activity_artisan_dashboard.xml
            │   ├── activity_add_product.xml
            │   ├── activity_product_detail.xml
            │   ├── dialog_inquiry.xml
            │   ├── item_product.xml            ← Buyer grid card
            │   ├── item_artisan_product.xml    ← Artisan list row
            │   └── layout_empty_state.xml
            ├── values/
            │   ├── colors.xml
            │   ├── strings.xml
            │   ├── themes.xml
            │   └── dimens.xml
            ├── font/
            │   └── noto_serif.xml              ← Font family (add .ttf files here)
            └── drawable/
                ├── ic_product_placeholder.xml
                ├── ic_location.xml
                ├── ic_add.xml
                ├── ic_message.xml
                ├── ic_artisan.xml
                ├── ic_market_logo.xml
                ├── ic_empty_basket.xml
                ├── ic_image_placeholder.xml
                ├── bg_category_badge.xml
                ├── bg_price_tag.xml
                ├── bg_search.xml
                ├── bg_image_picker.xml
                ├── bg_bottom_sheet_handle.xml
                └── gradient_scrim.xml
```

---

## ✅ PRD Objectives

| Requirement | Status | Notes |
|---|---|---|
| Artisan upload products | ✅ | AddProductActivity with full validation |
| Buyer browse grid layout | ✅ | 2-col GridLayoutManager + DiffUtil |
| Search by name/category | ✅ | switchMap LiveData, SQL LIKE query |
| Product detail view | ✅ | CollapsingToolbarLayout + parallax image |
| Mock inquiry/messaging | ✅ | Bottom-sheet dialog → Room Inquiry entity |
| Empty state handling | ✅ | Custom layout_empty_state |
| MVVM architecture | ✅ | ViewModel + Repository + LiveData |
| Room local database | ✅ | Product + Inquiry entities, FK with CASCADE |
| Image upload & preview | ✅ | Gallery picker + Glide + runtime permissions |
| Cultural green-brown UI | ✅ | Full Material theme with brand tokens |
| Runtime permissions | ✅ | READ_MEDIA_IMAGES (API 33+) / READ_EXTERNAL_STORAGE |
| ViewBinding | ✅ | All Activities use ViewBinding |
| Unread inquiry badge | ✅ | LiveData<Int> from InquiryDao |
