package edu.rosehulman.automaticchecklist

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.rosehulman.automaticchecklist.databinding.ActivityMainBinding
import edu.rosehulman.automaticchecklist.models.UserViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener
    val signinLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { /* empty since the auth listener already responds .*/ }


    override fun onStart() {
        super.onStart()
        Firebase.auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        Firebase.auth.removeAuthStateListener(authStateListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeAuthListener()

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_inbox, R.id.navigation_category, R.id.navigation_user
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupAuthUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            //AuthUI.IdpConfig.GoogleBuilder().build()
        )
        val signinIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.Theme_AutomaticChecklist)
            .setLogo(R.drawable.ic_baseline_check_circle_24)
            .build()
        signinLauncher.launch(signinIntent)
    }


    private fun initializeAuthListener() {
        authStateListener = FirebaseAuth.AuthStateListener { auth: FirebaseAuth ->
            val user = auth.currentUser
            // logged out
            if (user == null) {
                setupAuthUI()
            } else {
                with(user) {
                    Log.d(
                        Helpers.TAG,
                        "User: $uid, $email, $displayName, $photoUrl, ${Firebase.auth.uid}"
                    )
                }
                val userModel = ViewModelProvider(this).get(UserViewModel::class.java)
                userModel.getOrMakeUser {
                    if (userModel.hasCompletedSetup()) {
                        val id = findNavController(
                            R.id.nav_host_fragment_activity_main
                        ).currentDestination!!.id
                        if (id == R.id.navigation_welcome) {
                            findNavController(R.id.nav_host_fragment_activity_main)
                                .navigate(R.id.navigation_inbox)
                        }
                    } else {
                        navController.navigate(R.id.navigation_user_edit)
                    }
                }
            }
        }
    }
}