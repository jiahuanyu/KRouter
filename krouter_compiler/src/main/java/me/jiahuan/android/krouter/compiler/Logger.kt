package me.jiahuan.android.krouter.compiler

import javax.annotation.processing.Messager
import javax.tools.Diagnostic

class Logger(val mMsg: Messager) {

    private val PREFIX_OF_LOGGER = "KRouter::Compiler::"

    private fun print(kind: Diagnostic.Kind, info: CharSequence?) {
        if (!info.isNullOrBlank()) {
            mMsg.printMessage(kind, "$PREFIX_OF_LOGGER $info")
        }
    }

    fun info(info: CharSequence?) {
        print(Diagnostic.Kind.NOTE, "info ============================= $info")
    }

    fun warning(waring: CharSequence?) {
        print(Diagnostic.Kind.WARNING, "warn ============================= $waring")
    }

    fun error(error: CharSequence?) {
        print(Diagnostic.Kind.ERROR, "erro ============================= $error")
    }


    private fun formatStackTrace(stackTrace: Array<StackTraceElement>): String {
        val sb = StringBuilder()
        for (element in stackTrace) {
            sb.append("    at ").append(element.toString()).append("\n")
        }
        return sb.toString()
    }
}