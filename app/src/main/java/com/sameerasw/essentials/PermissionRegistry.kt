package com.sameerasw.essentials

// Simple registry that maps permission keys to features that depend on them.
// This is intentionally simple; you can later move this to a data source or preferences.
object PermissionRegistry {
    private val registry = mutableMapOf<String, MutableList<String>>()

    fun register(permissionKey: String, featureName: String) {
        val list = registry.getOrPut(permissionKey) { mutableListOf() }
        if (!list.contains(featureName)) list.add(featureName)
    }

    fun getFeatures(permissionKey: String): List<String> = registry[permissionKey]?.toList() ?: emptyList()
}

// Register existing dependencies (called at startup or file load)
fun initPermissionRegistry() {
    // Key for accessibility (use unique string)
    PermissionRegistry.register("ACCESSIBILITY", "Screen off widget")
    // add other registrations here if needed in future
}

