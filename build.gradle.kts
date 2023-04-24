@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.kotlin) apply false
//    alias(libs.plugins.android.library) apply false
}
println("") // workaround for the suppression above making gradle stumble without
