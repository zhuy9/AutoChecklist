package edu.rosehulman.automaticchecklist.models

import edu.rosehulman.automaticchecklist.Helpers.defaultLabelStrings

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