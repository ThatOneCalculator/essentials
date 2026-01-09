package com.sameerasw.essentials.domain.registry

import com.sameerasw.essentials.domain.StatusBarIconRegistry
import com.sameerasw.essentials.domain.model.SearchableItem

import android.content.Context
import com.sameerasw.essentials.R

object SearchRegistry {

    private val staticItems = mutableListOf<SearchableItem>()

    init {
        // --- Automate Feature and Sub-setting Indexing ---
        FeatureRegistry.ALL_FEATURES.forEach { feature ->
            // Index the feature itself
            addStaticItem(
                SearchableItem(
                    title = feature.title,
                    description = feature.description,
                    category = feature.category,
                    icon = feature.iconRes,
                    featureKey = feature.id,
                    keywords = listOf("feature", "settings")
                )
            )

            // Index sub-settings
            feature.searchableSettings.forEach { setting ->
                addStaticItem(
                    SearchableItem(
                        title = setting.title,
                        description = setting.description,
                        category = setting.category ?: feature.category,
                        icon = feature.iconRes,
                        featureKey = feature.id,
                        parentFeature = feature.title,
                        targetSettingHighlightKey = setting.targetSettingHighlightKey,
                        keywords = setting.keywords
                    )
                )
            }
        }
    }

    private fun addStaticItem(item: SearchableItem) {
        staticItems.add(item)
    }

    fun search(context: Context, query: String): List<SearchableItem> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()

        val allItems = ArrayList(staticItems)

        // --- Dynamic Status Bar Icons ---
        StatusBarIconRegistry.ALL_ICONS.forEach { icon ->
            val title = context.getString(icon.displayNameRes)
            allItems.add(
                SearchableItem(
                    title = title,
                    description = context.getString(R.string.search_desc_toggle_visibility, title),
                    category = "Statusbar icons",
                    icon = icon.iconRes,
                    featureKey = "Statusbar icons",
                    parentFeature = "Statusbar icons",
                    targetSettingHighlightKey = title,
                    keywords = icon.blacklistNames + listOf("hide", "show", "visibility")
                )
            )
        }

        return allItems.filter { item ->
            item.title.lowercase().contains(q) ||
            item.description.lowercase().contains(q) ||
            item.category.lowercase().contains(q) ||
            item.keywords.any { it.lowercase().contains(q) }
        }.sortedByDescending { it.title.lowercase().startsWith(q) }
    }
}