# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
# 保留所有Activity、Fragment等组件（避免Manifest中引用的类被混淆）
# -keep public class * extends android.app.Activity
# -keep public class * extends android.app.Fragment
# -keep public class * extends androidx.fragment.app.Fragment

 # 保留所有自定义View的构造方法（避免XML布局引用失败）
 -keep public class * extends android.view.View {
     public <init>(android.content.Context);
     public <init>(android.content.Context, android.util.AttributeSet);
     public <init>(android.content.Context, android.util.AttributeSet, int);
     public void set*(...);
 }

 # 保留枚举类（避免valueOf等方法被混淆）
 -keepclassmembers enum * {
     public static **[] values();
     public static** valueOf(java.lang.String);
 }

 # 保留Parcelable相关类（避免序列化失败）
 -keep class * implements android.os.Parcelable {
     public static final android.os.Parcelable$Creator *;
 }

 # 保留Compose注解和相关类（核心规则）
# -keep @androidx.compose.runtime.Composable class *
# -keep class androidx.compose.** { *; }
# -keep interface androidx.compose.** { *; }

 # 保留Composable函数（避免方法名被混淆导致调用失败）
# -keepclassmembers class * {
#     @androidx.compose.runtime.Composable <methods>;
# }

 # 保留Compose的状态类（如MutableState）
# -keep class androidx.compose.runtime.MutableState { *; }
# -keep class androidx.compose.runtime.State { *; }

# # 保留Compose预览相关类（如果需要release版本支持预览，可选）
# -keep class * extends androidx.compose.ui.tooling.preview.PreviewActivity { *; }
#
 # 保留协程核心类和方法
 -keep class kotlinx.coroutines.** { *; }
 -keep interface kotlinx.coroutines.** { *; }

# # 保留挂起函数（协程的核心机制依赖这些方法的签名）
# -keepclassmembers class * {
#     kotlin.coroutines.jvm.internal.BaseContinuationImpl <fields>;
#     kotlin.coroutines.jvm.internal.BaseContinuationImpl <methods>;
# }

 # 保留协程的调度器（如Dispatchers.Main）
 -keep class kotlinx.coroutines.Dispatchers {
     public static <fields>;
 }