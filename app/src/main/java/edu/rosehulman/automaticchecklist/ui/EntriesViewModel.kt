package edu.rosehulman.automaticchecklist.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.rosehulman.automaticchecklist.Entry
import kotlin.random.Random

class EntriesViewModel : ViewModel() {

    private var entries = ArrayList<Entry>()
    var currentPos = 0
    private var oldVal: Entry? = null

    fun getEntryAt(pos: Int) = entries[pos]
    fun getCurrentEntry() = getEntryAt(currentPos)

    fun addEntry(entry: Entry?) {
        entries.add(Entry("${getRandom()}RANDOM"))
        // TODO set labels, recurring state, etc.
    }

    fun getRandom() = Random.nextInt(100)
    fun updateCurrentEntry(content: String) {
        entries[currentPos].updateContent(content)
        // TODO more elements to udpate
    }

    fun deleteCurrentEntry() {
        oldVal = entries.removeAt(currentPos)
        currentPos = 0
    }

    fun undoLastDelete() {
        if (oldVal != null)
            entries.add(oldVal!!)
    }

    fun updatePos(pos: Int) {
        currentPos = pos
    }

    fun toggleCurrentEntryState() {
        entries[currentPos].toggleCheckStatus()
    }

    fun size() = entries.size


}