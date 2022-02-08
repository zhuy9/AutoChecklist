package edu.rosehulman.automaticchecklist.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.automaticchecklist.Helpers
import java.util.*
import kotlin.collections.ArrayList

class UserViewModel : ViewModel() {
    var ref = Firebase.firestore.collection(User.COLLECTION_PATH)
        .document(Firebase.auth.currentUser!!.uid!!)
    var user: User? = null
    private var oldVal = ArrayList<String>()

    fun hasCompletedSetup() = user?.hasCompletedSetup ?: false

    fun deleteLabel(lb: String) {
        ref = Firebase.firestore.collection(User.COLLECTION_PATH)
            .document(Firebase.auth.uid!!) // for safe
        val labels = user!!.labels
        if (labels.remove(lb)) {
            oldVal.add(lb)
            ref.update(User.LABELS_COLLECTION_PATH, labels)
        }
    }

    fun resetUndoMemory() {
        oldVal = ArrayList<String>()
    }

    fun removedLabelsToString() = oldVal.joinToString(",")

    fun addLabel(lb: String) {
        val lb = lb.lowercase(Locale.getDefault())
        val labels = user!!.labels
        if (lb.isBlank() || labels.contains(lb))
            return
        ref = Firebase.firestore.collection(User.COLLECTION_PATH)
            .document(Firebase.auth.uid!!) // for safe
        labels.add(lb)
        ref.update(User.LABELS_COLLECTION_PATH, labels)
    }

    fun undoDelete() {
        oldVal.forEach { addLabel(it) }
        oldVal = ArrayList<String>()
    }

    fun update(newName: String, newAge: Int, newHasCompletedSetup: Boolean) {
        ref = Firebase.firestore.collection(User.COLLECTION_PATH)
            .document(Firebase.auth.uid!!) // add this line
        if (user != null) {
            with(user!!) {
                name = newName
                age = newAge
                hasCompletedSetup = newHasCompletedSetup
                ref.set(this)
            }
        }
    }

    fun getOrMakeUser(observer: () -> Unit) {
        Log.d(
            Helpers.TAG,
            "GetOrMakeUser: uid: ${Firebase.auth.uid}}"
        )
        ref = Firebase.firestore.collection(User.COLLECTION_PATH)
            .document(Firebase.auth.uid!!) // add this line
        if (user != null) {
            // get
            observer()
        } else {
            // make
            ref.get().addOnSuccessListener { snapShot: DocumentSnapshot ->

                if (snapShot.exists()) {
                    user = snapShot.toObject(User::class.java)
                } else {
                    user = User(name = Firebase.auth.currentUser!!.displayName!!)
                    ref.set(user!!)
                    val sampleEntry = Entry("Welcome!!!")
                    Firebase.firestore.collection(User.COLLECTION_PATH)
                        .document(Firebase.auth.uid!!)
                        .collection(Entry.COLLECTION_PATH).add(sampleEntry)
                }
                observer()
            }
        }
    }
}