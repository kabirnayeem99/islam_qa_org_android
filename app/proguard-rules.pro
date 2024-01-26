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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn kotlin.reflect.jvm.internal.**

-keep class kotlin.reflect.jvm.internal.** { *; }
-keep class   it.skrape.fetcher.** { *; }
-keep class   io.ktor.client.** { *; }
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-keep interface javax.annotation.Nullable

-keepattributes Signature,RuntimeVisibleAnnotations,AnnotationDefault

-keep class com.google.android.gms.** { *; }
-dontwarn com.google.android.gms.*
-keep class com.google.api.client.** {*;}

-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

-keep public class io.github.kabirnayeem99.islamqaorg.data.dto.**
-keep public class ch.qos.logback.core.**
-keep public class io.github.kabirnayeem99.islamqaorg.domain.entity.**
-keep public class io.github.kabirnayeem99.islamqaorg.data.dataSource.**

-dontwarn javax.annotation.**

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

-keep public class * implements com.bumptech.glide.module.GlideModule

-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep class org.apache.commons.logging.LogFactory
-keep class org.apache.commons.logging.impl.LogFactoryImpl
-keep public class  org.apache.** {
  public protected *;
}
-dontwarn androidx.room.paging.**

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn groovy.lang.GroovyObject
-dontwarn groovy.lang.MetaClass
-dontwarn java.applet.Applet
-dontwarn java.applet.AppletContext
-dontwarn java.applet.AppletStub
-dontwarn java.awt.datatransfer.ClipboardOwner
-dontwarn java.awt.font.FontRenderContext
-dontwarn java.awt.font.LineBreakMeasurer
-dontwarn java.awt.font.TextLayout
-dontwarn java.awt.geom.AffineTransform
-dontwarn java.beans.BeanDescriptor
-dontwarn java.beans.BeanInfo
-dontwarn java.beans.IntrospectionException
-dontwarn java.beans.Introspector
-dontwarn java.beans.PropertyDescriptor
-dontwarn java.lang.management.ManagementFactory
-dontwarn javax.imageio.ImageIO
-dontwarn javax.imageio.ImageReader
-dontwarn javax.imageio.stream.ImageInputStream
-dontwarn javax.lang.model.SourceVersion
-dontwarn javax.management.InstanceNotFoundException
-dontwarn javax.management.MBeanRegistrationException
-dontwarn javax.management.MBeanServer
-dontwarn javax.management.MalformedObjectNameException
-dontwarn javax.management.ObjectInstance
-dontwarn javax.management.ObjectName
-dontwarn javax.naming.Context
-dontwarn javax.naming.InitialContext
-dontwarn javax.naming.InvalidNameException
-dontwarn javax.naming.NamingException
-dontwarn javax.naming.directory.Attribute
-dontwarn javax.naming.directory.Attributes
-dontwarn javax.naming.ldap.LdapName
-dontwarn javax.naming.ldap.Rdn
-dontwarn javax.servlet.ServletContainerInitializer
-dontwarn org.apache.bsf.BSFManager
-dontwarn org.codehaus.groovy.reflection.ClassInfo
-dontwarn org.codehaus.groovy.runtime.BytecodeInterface8
-dontwarn org.codehaus.groovy.runtime.ScriptBytecodeAdapter
-dontwarn org.codehaus.groovy.runtime.callsite.CallSite
-dontwarn org.codehaus.groovy.runtime.callsite.CallSiteArray
-dontwarn org.codehaus.janino.ClassBodyEvaluator
-dontwarn org.codehaus.janino.ScriptEvaluator
-dontwarn org.ietf.jgss.GSSContext
-dontwarn org.ietf.jgss.GSSCredential
-dontwarn org.ietf.jgss.GSSException
-dontwarn org.ietf.jgss.GSSManager
-dontwarn org.ietf.jgss.GSSName
-dontwarn org.ietf.jgss.Oid
-dontwarn sun.reflect.Reflection
# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn java.awt.AWTEvent
-dontwarn java.awt.CardLayout
-dontwarn java.awt.Color
-dontwarn java.awt.Component
-dontwarn java.awt.Container
-dontwarn java.awt.Dialog
-dontwarn java.awt.Dimension
-dontwarn java.awt.FlowLayout
-dontwarn java.awt.Frame
-dontwarn java.awt.Graphics
-dontwarn java.awt.GridBagConstraints
-dontwarn java.awt.GridBagLayout
-dontwarn java.awt.GridLayout
-dontwarn java.awt.Insets
-dontwarn java.awt.Label
-dontwarn java.awt.LayoutManager
-dontwarn java.awt.SystemColor
-dontwarn java.awt.TextArea
-dontwarn java.awt.TextComponent
-dontwarn java.awt.TextField
-dontwarn java.awt.Toolkit
-dontwarn java.awt.Window
-dontwarn java.awt.event.ActionEvent
-dontwarn java.awt.event.ActionListener
-dontwarn java.awt.event.TextEvent
-dontwarn java.awt.event.TextListener
-dontwarn java.awt.event.WindowAdapter
-dontwarn java.awt.event.WindowEvent
-dontwarn java.awt.event.WindowListener
-dontwarn javax.mail.Address
-dontwarn javax.mail.Authenticator
-dontwarn javax.mail.BodyPart
-dontwarn javax.mail.Message$RecipientType
-dontwarn javax.mail.Message
-dontwarn javax.mail.Multipart
-dontwarn javax.mail.Session
-dontwarn javax.mail.Transport
-dontwarn javax.mail.internet.AddressException
-dontwarn javax.mail.internet.InternetAddress
-dontwarn javax.mail.internet.MimeBodyPart
-dontwarn javax.mail.internet.MimeMessage
-dontwarn javax.mail.internet.MimeMultipart
-dontwarn javax.script.ScriptEngine
-dontwarn javax.script.ScriptEngineManager
-dontwarn javax.servlet.ServletContextEvent
-dontwarn javax.servlet.ServletContextListener
-dontwarn javax.servlet.http.HttpServlet
-dontwarn javax.swing.AbstractButton
-dontwarn javax.swing.BorderFactory
-dontwarn javax.swing.JButton
-dontwarn javax.swing.JComponent
-dontwarn javax.swing.JDialog
-dontwarn javax.swing.JFrame
-dontwarn javax.swing.JList
-dontwarn javax.swing.JMenu
-dontwarn javax.swing.JMenuBar
-dontwarn javax.swing.JMenuItem
-dontwarn javax.swing.JPanel
-dontwarn javax.swing.JScrollPane
-dontwarn javax.swing.JSplitPane
-dontwarn javax.swing.JTextPane
-dontwarn javax.swing.JViewport
-dontwarn javax.swing.KeyStroke
-dontwarn javax.swing.ListModel
-dontwarn javax.swing.UIManager
-dontwarn javax.swing.border.Border
-dontwarn javax.swing.event.ListDataEvent
-dontwarn javax.swing.event.ListDataListener
-dontwarn javax.swing.event.ListSelectionListener
-dontwarn javax.swing.text.JTextComponent
-dontwarn org.apache.avalon.framework.logger.Logger
-dontwarn org.apache.log.Hierarchy
-dontwarn org.apache.log.Logger
-dontwarn org.apache.xml.resolver.Catalog
-dontwarn org.apache.xml.resolver.CatalogManager
-dontwarn org.apache.xml.resolver.readers.CatalogReader
-dontwarn org.apache.xml.resolver.readers.SAXCatalogReader