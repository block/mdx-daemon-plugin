package locator

import com.intellij.diagnostic.ActivityCategory
import com.intellij.openapi.extensions.ExtensionsArea
import com.intellij.openapi.extensions.PluginDescriptor
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Key
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.messages.MessageBus

class FakeProject(
    private val basePathValue: String,
    private val nameValue: String = "FakeProject",
) : Project {
    override fun getBasePath(): String = basePathValue

    override fun getName(): String = nameValue

    // Everything else is stubbed with safe fallbacks

    override fun isDisposed(): Boolean = false

    override fun dispose() {}

    override fun <T : Any?> getUserData(key: Key<T>): T? = null

    override fun <T : Any?> putUserData(
        key: Key<T>,
        value: T?,
    ) {}

    override fun getBaseDir(): VirtualFile? = null

    override fun getProjectFile(): VirtualFile? = null

    override fun getProjectFilePath(): String? = null

    override fun getWorkspaceFile(): VirtualFile? = null

    override fun getLocationHash(): String = ""

    override fun isOpen(): Boolean = true

    override fun isInitialized(): Boolean = true

    override fun getDisposed(): Condition<*> = Condition<Boolean> { false }

    override fun getMessageBus(): MessageBus {
        throw UnsupportedOperationException("Not needed for this test")
    }

    override fun getExtensionArea(): ExtensionsArea {
        throw UnsupportedOperationException("Not needed for this test")
    }

    @Deprecated("getComponent(Class) is deprecated and scheduled for removal. Use getService(Class) instead.")
    override fun <T : Any?> getComponent(interfaceClass: Class<T>): T {
        throw UnsupportedOperationException("FakeProject does not support getComponent(Class<T>). Use getService(Class<T>) if needed.")
    }

    override fun isInjectionForExtensionSupported(): Boolean = false

    override fun hasComponent(interfaceClass: Class<*>): Boolean = false

    override fun <T : Any?> getService(serviceClass: Class<T>): T {
        throw UnsupportedOperationException("Not needed for this test")
    }

    override fun <T : Any?> instantiateClass(
        className: String,
        pluginDescriptor: PluginDescriptor,
    ): T & Any {
        throw UnsupportedOperationException("Not needed for this test")
    }

    override fun <T : Any?> instantiateClass(
        clazz: Class<T>,
        pluginId: PluginId,
    ): T {
        throw UnsupportedOperationException("Not needed for this test")
    }

    override fun <T : Any?> instantiateClassWithConstructorInjection(
        clazz: Class<T>,
        constructorArg: Any,
        pluginId: PluginId,
    ): T {
        throw UnsupportedOperationException("Not needed for this test")
    }

    override fun <T : Any?> loadClass(
        className: String,
        pluginDescriptor: PluginDescriptor,
    ): Class<T> {
        throw UnsupportedOperationException("Not needed for this test")
    }

    override fun createError(
        message: String,
        pluginId: PluginId,
    ): RuntimeException = RuntimeException(message)

    override fun createError(
        t: Throwable,
        pluginId: PluginId,
    ): RuntimeException = RuntimeException(t)

    override fun createError(
        message: String,
        t: Throwable?,
        pluginId: PluginId,
        attachments: MutableMap<String, String>?,
    ): RuntimeException = RuntimeException(message, t)

    override fun getActivityCategory(isFromUI: Boolean): ActivityCategory = ActivityCategory.APP_SERVICE

    override fun save() {}
}
