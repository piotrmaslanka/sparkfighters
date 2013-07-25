-injars ..\..\bin\data\SparkFightersGame.jar
-outjars ..\..\bin\data\SparkFightersGame_proguard.jar

-libraryjars ..\..\monitor\libs\tools.jar
-libraryjars ..\..\client_game\libs
-libraryjars 'C:\Program Files\Java\jre7\lib'

-forceprocessing
-dontshrink
-dontoptimize
-optimizationpasses 9
-obfuscationdictionary dictionaries\compact.txt
-classobfuscationdictionary dictionaries\compact.txt
-packageobfuscationdictionary dictionaries\compact.txt
-flattenpackagehierarchy com.sparkfighters.client.game
-repackageclasses com.sparkfighters.client.game
-keepattributes *Annotation*,Signature
-adaptclassstrings *
-adaptresourcefilecontents **.fxml,**.properties,META-INF/MANIFEST.MF
-dontnote


-keep class sun.misc.Unsafe {
    <fields>;
    <methods>;
}

-keep class com.google.gson.stream.** {
    <fields>;
    <methods>;
}

-keep class com.google.gson.examples.android.model.** {
    <fields>;
    <methods>;
}

# Explicitly preserve all serialization members. The Serializable interface
# is only a marker interface, so it wouldn't save them.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Preserve static fields of inner classes of R classes that might be accessed
# through introspection.
-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers,allowshrinking class * {
    @javafx.fxml.FXML
    <fields>;
    @javafx.fxml.FXML
    <methods>;
}

# Keep - Applications. Keep all application classes, along with their 'main'
# methods.
-keepclasseswithmembers public class com.sparkfighters.client.game.Main {
    public static void main(java.lang.String[]);
}

# Keep - Library. Keep all public and protected classes, fields, and methods.
-keep public class com.sparkfighters.shared.** {
    public protected <fields>;
    public protected <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

# Keep names - Native method names. Keep all native class/method names.
-keepclasseswithmembers,allowshrinking class * {
    native <methods>;
}
