package edu.rosehulman.automaticchecklist.models

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.rosehulman.automaticchecklist.Helpers

class UserViewModel : ViewModel() {
    var ref = Firebase.firestore.collection(User.COLLECTION_PATH)
        .document(Firebase.auth.currentUser!!.uid!!)
    var user: User? = null

    fun hasCompletedSetup() = user?.hasCompletedSetup ?: false

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