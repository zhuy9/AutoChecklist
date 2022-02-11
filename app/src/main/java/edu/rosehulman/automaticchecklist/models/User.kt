package edu.rosehulman.automaticchecklist.models

import edu.rosehulman.automaticchecklist.Constants.defaultLabelStrings

data class User(
    var name: String = "",
    var age: Int = -1,
    var hasCompletedSetup: Boolean = false,
    var labels: ArrayList<String> = defaultLabelStrings
) {
    companion object {
        const val COLLECTION_PATH = "users"
        const val LABELS_COLLECTION_PATH = "labels"
    }
}