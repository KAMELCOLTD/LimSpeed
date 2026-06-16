# 📱 LimSpeed - تطبيق التحكم بسرعة الإنترنت

تطبيق أندرويد خفيف وسريع يسمح لك بالتحكم الدقيق في سرعة الإنترنت لكل تطبيق على حدة، دون الحاجة إلى صلاحيات Root.

---

## 🎯 المميزات الرئيسية

- 🎯 **تحديد سرعة عامة** - من 50 كيلوبايت إلى 10 ميجابايت/ثانية
- 📱 **تحديد سرعة لكل تطبيق** - سرعة مختلفة لكل تطبيق
- 🌓 **واجهة مزدوجة** - نهارية وليلية حسب إعدادات الجهاز
- 🌍 **دعم لغتين** - عربي وإنجليزي حسب لغة الجهاز
- ⚡ **خفيف جداً** - حجم APK أقل من 3 ميجابايت
- 🔋 **لا يستهلك البطارية** - يعمل بتقنية VPN الذكية
- 🚀 **سريع وفعال** - بدون تأخير في الأداء

---

## 🏗️ هيكل المشروع

```
LimSpeed/
│
├── app/                              # تطبيق أندرويد الرئيسي
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/limspeed/
│   │   │   │   ├── ui/                # واجهات المستخدم
│   │   │   │   │   ├── activities/
│   │   │   │   │   ├── fragments/
│   │   │   │   │   └── dialogs/
│   │   │   │   ├── services/          # خدمات التطبيق
│   │   │   │   │   ├── VpnService.kt
│   │   │   │   │   └── SpeedLimiter.kt
│   │   │   │   ├── managers/          # إدارة البيانات والعمليات
│   │   │   │   │   ├── AppManager.kt
│   │   │   │   │   └── SpeedManager.kt
│   │   │   │   ├── models/            # نماذج البيانات
│   │   │   │   │   ├── App.kt
│   │   │   │   │   └── SpeedLimit.kt
│   │   │   │   ├── repository/        # الوصول للبيانات
│   │   │   │   ├── viewmodel/         # ViewModels (MVVM)
│   │   │   │   ├── utils/             # أدوات مساعدة
│   │   │   │   │   ├── Constants.kt
│   │   │   │   │   ├── Extensions.kt
│   │   │   │   │   └── Localization.kt
│   │   │   │   ├── database/          # قاعدة البيانات
│   │   │   │   ├── preferences/       # SharedPreferences
│   │   │   │   └── LimSpeedApp.kt     # Application Class
│   │   │   ├── res/
│   │   │   │   ├── layout/            # ملفات التخطيط
│   │   │   │   ├── drawable/          # الصور والأيقونات
│   │   │   │   ├── values/            # الألوان والنصوص (الإنجليزية)
│   │   │   │   ├── values-ar/         # النصوص (العربية)
│   │   │   │   ├── values-night/      # الألوان (الوضع الليلي)
│   │   │   │   └── menu/              # القوائم
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                      # اختبارات Unit
│   │   └── androidTest/               # اختبارات Integration
│   ├── build.gradle.kts               # إعدادات البناء
│   └── proguard-rules.pro             # قوانين ProGuard
│
├── gradle/                            # ملفات Gradle
│
├── .gitignore
├── build.gradle.kts                   # إعدادات البناء الرئيسية
├── settings.gradle.kts
├── gradle.properties
│
├── docs/                              # التوثيق
│   ├── ARCHITECTURE.md
│   ├── SETUP.md
│   └── API.md
│
└── .github/
    └── workflows/                     # CI/CD (اختياري)
        └── build.yml
```

---

## 🛠️ كيفية البدء

```bash
# استنساخ المشروع
git clone https://github.com/KAMELCOLTD/LimSpeed.git
cd LimSpeed

# بناء التطبيق
./gradlew build

# تشغيل الاختبارات
./gradlew test
```

---

## 📋 المتطلبات

- **Android SDK**: API 26 (Android 7.0) فأعلى
- **Java**: 11 أو أحدث
- **Gradle**: 8.0+
- **Android Studio**: Giraffe أو أحدث

---

## 🔒 الصلاحيات المطلوبة

- VPN Service
- Usage Stats
- Foreground Service

---

## 👨‍💻 المطور

**كامل الحكيمي (Kamel Alhakimi)**  
GitHub: [@KAMELCOLTD](https://github.com/KAMELCOLTD)

---

## 📄 الترخيص

هذا المشروع مرخص تحت MIT License

