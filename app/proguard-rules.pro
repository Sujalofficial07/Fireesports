# Keep data models
-keep class com.fireesports.data.model.** { *; }

# Keep Supabase classes
-keep class io.github.jan.supabase.** { *; }
-dontwarn io.github.jan.supabase.**

# Keep Ktor classes
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Keep Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.fireesports.**$$serializer { *; }
-keepclassmembers class com.fireesports.** {
    *** Companion;
}
-keepclasseswithmembers class com.fireesports.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Keep Kotlin metadata
-keep class kotlin.Metadata { *; }
-keepattributes Signature
-keepattributes Exception
-keepattributes *Annotation*

# SLF4J - Ignore missing implementation
-dontwarn org.slf4j.**
-dontwarn org.slf4j.impl.StaticLoggerBinder
-keep class org.slf4j.** { *; }

# Conscrypt
-dontwarn org.conscrypt.**
-keep class org.conscrypt.** { *; }

# BouncyCastle
-dontwarn org.bouncycastle.**
-keep class org.bouncycastle.** { *; }

# OpenJSSE
-dontwarn org.openjsse.**

# Firebase
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keep class com.google.firebase.** { *; }
-dontwarn com.google.firebase.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager$FragmentContextWrapper { *; }
-keepclassmembers class * {
    @javax.inject.* <fields>;
    @javax.inject.* <methods>;
}

# Compose
-keep class androidx.compose.** { *; }
-dontwarn androidx.compose.**

# Navigation
-keep class androidx.navigation.** { *; }

# Lifecycle
-keep class androidx.lifecycle.** { *; }

# Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}
-dontwarn kotlinx.coroutines.**

# Gson (if used)
-keepattributes Signature
-keepattributes *Annotation*
-dontwarn sun.misc.**
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Keep native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep enums
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Keep Parcelables
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator CREATOR;
}

# Keep Serializable
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep custom views
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

# Remove logging in release
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}

# Optimization flags
-optimizationpasses 5
-allowaccessmodification
-dontpreverify
