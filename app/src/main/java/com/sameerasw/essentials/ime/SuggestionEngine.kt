package com.sameerasw.essentials.ime

import android.content.Context
import com.darkrockstudios.symspellkt.api.SpellChecker
import com.darkrockstudios.symspellkt.impl.SymSpell
import com.darkrockstudios.symspellkt.common.Verbosity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class SuggestionEngine(private val context: Context) {
    private var symSpell: SpellChecker? = null
    private val _suggestions = MutableStateFlow<List<String>>(emptyList())
    val suggestions: StateFlow<List<String>> = _suggestions

    fun initialize(scope: CoroutineScope) {
        scope.launch(Dispatchers.IO) {
            try {
                val file = File(context.filesDir, "freq_dict.txt")
                if (!file.exists()) {
                    context.assets.open("frequency_dictionary_en_82_765.txt").use { input ->
                        FileOutputStream(file).use { output ->
                            input.copyTo(output)
                        }
                    }
                }

                // Initialize SymSpell
                val checker = SymSpell()
                
                // Manual dictionary loading to avoid extension function issues
                file.forEachLine { line ->
                    val parts = line.split(" ")
                    if (parts.size >= 2) {
                        try {
                            val word = parts[0]
                            val count = parts[1].toLongOrNull() ?: 0L
                            checker.createDictionaryEntry(word, count.toDouble())
                        } catch (e: Exception) {
                            // Ignore line errors
                        }
                    }
                }
                
                symSpell = checker
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun lookup(word: String) {
        val checker = symSpell ?: return
        if (word.isBlank()) {
            _suggestions.value = emptyList()
            return
        }

        try {
            // Using MAX_EDIT_DISTANCE 2.0
            val results = checker.lookup(word, Verbosity.Closest, 2.0)
            
            val list = results.map { it.term }.distinct().take(3)
            _suggestions.value = list
        } catch (e: Exception) {
            _suggestions.value = emptyList()
        }
    }
    
    fun clearSuggestions() {
        _suggestions.value = emptyList()
    }
}
