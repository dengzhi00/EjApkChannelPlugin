# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Work\studioSDK\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#-applymapping mapping.txt

-optimizationpasses 5
#-dontusemixedcaseclassnames #混淆时不会产生形形色色的类名
-useuniqueclassmembernames  #确定统一的混淆类的成员名称来增加混淆
-dontskipnonpubliclibraryclasses #指定不去忽略非公共的库类
-dontpreverify #混淆时不做预校验
-verbose
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-ignorewarnings

-keepattributes Signature
-keepattributes SourceFile,InnerClasses,LineNumberTable
-keepattributes *Annotation* #保留注解
-keepattributes *JavascriptInterface*
-allowaccessmodification #优化时允许访问并修改有修饰符的类和类的成员

#-dontoptimize #不优化输入类文件(Log)

-keep class android.support.v4.** { *; }


-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.preference.Preference
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.support.v4.**
-keep public class * extends android.support.annotation.**
-keep public class * extends android.support.v7.**

#-keep public class com.ej.ejservicev_3.ui.adapter.listview.** { *; }
#-keepattributes Signature
#-keep class **.R$* {*;}
#-keep class com.ej.ejservicev_3.ui.adapter.holder.**{*;}
#-keepclassmembers class com.ej.ejservicev_3.ui.adapter.listview.HolderListAdapter{*;}
#-keepclassmembers class com.ej.ejservicev_3.ui.adapter.holder.FlowHolder{*;}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements android.os.Parcelable {
 public <fields>;
 private <fields>;
}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#如果引用了v4或者v7包
-dontwarn android.support.**

-keepclasseswithmembers class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class **.R$* {
    *;
}

#RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

#butterknife
#-keep class butterknife.** { *; }
#-dontwarn butterknife.internal.**
#-keep class **$$ViewBinder { *; }
#-keepclasseswithmembernames class * {
#    @butterknife.* <fields>;
#}
#-keepclasseswithmembernames class * {
#    @butterknife.* <methods>;
#}

#Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

#Gson
#-keepattributes Signature-keepattributes *Annotation*
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson 下面替换成自己的实体类
#-keep class com.ej.ejbase.bean.** { *; }

#OkHttp3
-dontwarn com.squareup.okhttp3.**
-keep class com.squareup.okhttp3.** { *;}
-dontwarn okio.**

-keepattributes SourceFile,LineNumberTable
-keep class com.parse.*{ *; }
-dontwarn com.parse.**
-dontwarn com.squareup.picasso.**
-keepclasseswithmembernames class * {
    native <methods>;
}

#Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

#支付宝支付
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-dontwarn android.net.**
-keep class android.net.SSLCertificateSocketFactory{*;}

#-libraryjars /libs/alipaySdk-20170309.jar
#-libraryjars /libs/pinyin4j-2.5.0.jar
-dontwarn demo.**
-keep class demo.**{*;}

-dontwarn net.sourceforge.pinyin4j.**
-keep class net.sourceforge.pinyin4j.**{*;}
-keep class net.sourceforge.pinyin4j.format.**{*;}
-keep class net.sourceforge.pinyin4j.format.exception.**{*;}

#微信分享
#-dontwarn com.tencent.**
#-keep class com.tencent.** {
#   *;
#}
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
#-keep class com.tencent.mm.sdk.** {
#   *;
#}
-keep class com.dzm.jcenter.share.** {*;}
#QQ
-keep class com.tencent.open.TDialog$*
-keep class com.tencent.open.TDialog$* {*;}
-keep class com.tencent.open.PKDialog
-keep class com.tencent.open.PKDialog {*;}
-keep class com.tencent.open.PKDialog$*
-keep class com.tencent.open.PKDialog$* {*;}

#微博
-dontwarn com.weibo.sdk.Android.WeiboDialog
-dontwarn android.NET.http.SslError
-dontwarn android.webkit.WebViewClient
-keep public class android.Net.http.SslError{
*;
}
-keep public class android.webkit.WebViewClient{
*;
}
-keep public class android.webkit.WebChromeClient{
*;
}
-keep public interface android.webkit.WebChromeClient$CustomViewCallback {
*;
}
-keep public interface android.webkit.ValueCallback {
*;
}
-keep class * implements android.webkit.WebChromeClient {
*;
}

-keep class com.sina.weibo.sdk.api.** {
*;
}

-keep class com.sina.weibo.sdk.** {
*;
}

#bugly
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}


#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.ej.ejservicev_3.R$*{
public static final int *;
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-dontwarn com.taobao.**
-dontwarn anet.channel.**
-dontwarn anetwork.channel.**
-dontwarn org.android.**
-dontwarn org.apache.thrift.**
-dontwarn com.xiaomi.**
-dontwarn com.huawei.**

-keepattributes *Annotation*

-keep class com.taobao.** {*;}
-keep class org.android.** {*;}
-keep class anet.channel.** {*;}
-keep class com.umeng.** {*;}
-keep class com.xiaomi.** {*;}
-keep class com.huawei.** {*;}
-keep class org.apache.thrift.** {*;}

-keep class com.alibaba.sdk.android.**{*;}
-keep class com.ut.**{*;}
-keep class com.ta.**{*;}

-keep public class **.R$*{
   public static final int *;
}

#（可选）避免Log打印输出
-assumenosideeffects class android.util.Log {
   public static *** v(...);
   public static *** d(...);
   public static *** i(...);
   public static *** w(...);
 }

 #router
 -keep class com.ej.ejbase.router.**{*;}
 -keep class * implements com.ej.ejbase.http.EjBaseInterface{*;}


#3D 地图 V5.0.0之前：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.amap.mapcore.*{*;}
-keep   class com.amap.api.trace.**{*;}

#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}