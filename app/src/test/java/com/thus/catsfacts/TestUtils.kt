package com.thus.catsfacts

import android.os.Build
import java.lang.reflect.Field
import java.lang.reflect.Modifier
import kotlin.reflect.full.declaredMemberFunctions
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

object TestUtils {
    fun useSDK(sdk: Int) {
        setStaticField(
            Build.VERSION::class.java,
            "SDK_INT",
            sdk
        )
    }

    fun setStaticField(clazz: Class<*>, fieldName: String, fieldNewValue: Any) {
        try {
            setStaticField(clazz.getDeclaredField(fieldName), fieldNewValue)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun setStaticField(field: Field, fieldNewValue: Any) {
        try {
            makeFieldVeryAccessible(field)
            field.set(null, fieldNewValue)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }

    private fun makeFieldVeryAccessible(field: Field) {
        field.isAccessible = true

        try {
            val modifiersField = Field::class.java.getDeclaredField("modifiers")
            modifiersField.isAccessible = true
            try {
                modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
            } catch (e: IllegalAccessException) {
                throw RuntimeException(e)
            }

        } catch (e: NoSuchFieldException) {
            // ignore missing fields
        }

    }
}

inline fun <reified T : Any> T.setProperty(propertyName: String, value: Any?) =
        T::class.java.getDeclaredField(propertyName)
                .apply { isAccessible = true }
                .set(this, value)

/**
 * Ex: val param: String = instance.getProperty("test")
 */
inline fun <reified T : Any, S> T.getProperty(propertyName: String): S =
        T::class.declaredMemberProperties
                .firstOrNull { it.name == propertyName }
                ?.apply { isAccessible = true }
                ?.get(this) as S

/**
 * Ex: val param: String = instance.getSuperProperty("test")
 */
inline fun <reified T : Any, S> T.getSuperProperty(propertyName: String): S =
        T::class.declaredMemberProperties
                .firstOrNull { it.name == propertyName }
                ?.apply { isAccessible = true }
                ?.get(this) as S

/**
 * Ex: instance.invokeMethod("test")
 * Ex: instance.invokeMethod("test", 1L, "asd", listOf(Author()))
 *
 * Invoke Method with return values
 * Ex: instance.invokeMethod("getList") as List<String>
 */
inline fun <reified T> T.invokeMethod(methodName: String, vararg args: Any?): Any? =
        T::class.declaredMemberFunctions
                .firstOrNull { it.name == methodName }
                ?.apply { isAccessible = true }
                ?.call(this, *args)