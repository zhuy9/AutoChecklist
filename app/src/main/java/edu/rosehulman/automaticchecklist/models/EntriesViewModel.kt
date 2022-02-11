package edu.rosehulman.automaticchecklist.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.automaticchecklist.Constants
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.random.Random

class EntriesViewModel : ViewModel() {

    private var entries = ArrayList<Entry>()
    var currentPos = 0
    var oldPos = -14
    private var oldVal: Entry? = null
    private var currentDocId: String = ""
    private lateinit var ref: CollectionReference
    private val subscriptions = HashMap<String, ListenerRegistration>()
    var onCreate = false

    fun addNew() {
        onCreate = true
    }

    fun getEntriesByTag(tag: String): List<Entry> {
        if (tag == null || tag == "NONE")
            return entries

        return entries.filter { it.tags.contains(tag.lowercase(Locale.getDefault())) }
    }

    fun getEntryAt(pos: Int): Entry {
        Log.d(Constants.TAG, "getEntryAt: $pos, curPos: $currentPos, length = ${entries.size}")
        if (onCreate || entries.size == 0 || pos >= entries.size || pos < 0) {
            return Entry()
        }
        return entries[pos]
    }

    fun getCurrentEntry() = getEntryAt(currentPos)

    fun removeListener(fragmentName: String) {
        subscriptions[fragmentName]?.remove()
        subscriptions.remove(fragmentName)
    }

    fun addListener(fragmentName: String, observer: () -> Unit) {
        val uid = Firebase.auth.currentUser!!.uid
        ref = Firebase.firestore.collection(User.COLLECTION_PATH).document(uid)
            .collection(Entry.COLLECTION_PATH)
        val subscription = ref.orderBy(Entry.CREATED_KEY, Query.Direction.ASCENDING)
            .addSnapshotListener { snapShot: QuerySnapshot?, error: FirebaseFirestoreException? ->
                error?.let {
                    Log.d(Constants.TAG, "Error : $error")
                    return@addSnapshotListener
                }
                entries.clear()
                snapShot?.documents?.forEach {
                    entries.add(Entry.from(it))
                }
                observer()
            }
        subscriptions[fragmentName] = subscription
    }

    fun addEntry(entry: Entry?) {
        if (entry != null) {
            ref.add(entry)
        } else {
            val e = Entry("${getRandom()}Edit me!")
            ref.add(e)
            entries.add(e)
            Log.d(
                Constants.TAG,
                "currentPos($currentPos) entrySize(${entries.size}): ${entries.joinToString(" : ")}"
            )
            currentPos = entries.size - 1

        }
    }

    fun getRandom() = Random.nextInt(100)

    fun updateCurrentEntry(entry: Entry?) {
        if (onCreate) onCreate = false
        // Log.d(Helpers.TAG, "---------------------ERROR LOCATION: ${entry!!.id}")
        if (entry!!.id.isNotBlank())
            ref.document(entry.id).set(entry)
        else
            addEntry(entry)
    }

    fun deleteCurrentEntry() {
        oldVal = getCurrentEntry()
        oldPos = currentPos
        ref.document(getCurrentEntry().id).delete()
        currentPos = 0

    }

    fun undoLastDelete() {
        if (oldVal != null) {
            //entries.add(oldVal!!)
            ref.add(oldVal!!)
        }
    }

    fun updatePos(pos: Int) {
        currentPos = pos
    }

    fun toggleCurrentEntryState() {
        entries[currentPos].isChecked = !entries[currentPos].isChecked
        ref.document(getCurrentEntry().id).set(getCurrentEntry())
    }

    fun size() = entries.size


}