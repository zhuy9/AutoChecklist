package edu.rosehulman.automaticchecklist.ui

import edu.rosehulman.automaticchecklist.Helpers
import edu.rosehulman.automaticchecklist.Helpers.defaultLabelStrings
import edu.rosehulman.automaticchecklist.Label

data class User(
    var name: String = "",
    var age: Int = -1,
    var hasCompletedSetup: Boolean = false,
    var labels: ArrayList<String> = defaultLabelStrings
) {
    companion object {
        const val COLLECTION_PATH = "users"
    }
}