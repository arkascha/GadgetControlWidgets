package org.rustygnome.gadgetcontrolwidgets.extension.string

fun String.firstCharToUpperCase(): String =
    this.replaceFirstChar { // it: Char
        if (it.isLowerCase())
            it.uppercaseChar()
        else
            it
    }