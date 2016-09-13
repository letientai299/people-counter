-keepattributes EnclosingMethod
-keep class java.lang.** {*;}

# ButterKnife
# Retain generated class which implement Unbinder.
-keep public class * implements butterknife.Unbinder { public <init>(...); }

# Prevent obfuscation of types which use ButterKnife annotations since the simple name
# is used to reflectively look up the generated ViewBinding.
-keep class butterknife.*
-keepclasseswithmembernames class * { @butterknife.* <methods>; }
-keepclasseswithmembernames class * { @butterknife.* <fields>; }

#Rx
-keep class rx.** {*;}
-keep class rx.schedulers.** {*;}
-dontnote rx.**
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



# GreenDAO

-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.** {*;}


# For photo view
-keep public class uk.co.senab.** {*;}

# For apache poi
-keep class org.apache.** {*;}
-dontwarn org.apache.**

-keep public class schemasMicrosoftComVml.** {*;}
-dontwarn schemasMicrosoftComVml.**

-keep public class schemasMicrosoftComOfficeOffice.** {*;}
-dontwarn schemasMicrosoftComOfficeOffice.**

-keep public class schemasMicrosoftComOfficeExcel.** {*;}
-dontwarn schemasMicrosoftComOfficeExcel.**

-keep class com.bea.** {*;}
-keep class org.w3.** {*;}
-keep class org.etsi.** {*;}
-dontwarn org.**
-dontwarn com.microsoft.**

-keep class org.openxmlformats.** {*;}

-dontwarn org.openxmlformats.**
-keep public class aavax.xml.** {*;}

-dontnote org.apache.**
-dontnote schemasMicrosoftComVml.**


# Retrolambda
-dontwarn java.lang.invoke.*
