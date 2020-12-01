package com.novyapp.superplanning.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.novyapp.superplanning.data.CourseV2
import com.novyapp.superplanning.data.FirebaseDataSource
import com.novyapp.superplanning.data.toCourseV2
import com.novyapp.superplanning.databinding.MainPageFragmentBinding
import timber.log.Timber

private const val REQUEST_CODE = 12


// TODO("display the current week and not this 1st week available")
class MainPage : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var binding: MainPageFragmentBinding
    private val viewModel by viewModels<MainPageViewModel> {
        MainPageViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = MainPageFragmentBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        if (FirebaseAuth.getInstance().currentUser == null) {
            Timber.i("Need to connect user")
            launchSignInFlow()
        } else {
            Timber.i("User is already connected")
            testFireStore()
        }


//        val adapter = ViewPagerAdapter()
//        binding.viewPager.adapter = adapter
//
//
//
//        viewModel.courses.observe(viewLifecycleOwner) {
//            if (it.isNullOrEmpty()) Timber.i("Course list is Null or Empty")
//            else {
//                it.forEach { item -> Timber.i(item.toString()) }
//                adapter.submitList(it)
//                Timber.i("List submitted to adapter")
//            }
//        }

        return binding.root
    }

    private fun testFireStore() {
//        val user = hashMapOf(
//            "first" to "Ada",
//            "last" to "Lovelace",
//            "born" to 1815
//        )
//
//        // Add a new document with a generated ID
//        db.collection("users")
//            .add(user)
//            .addOnSuccessListener { documentReference ->
//                Timber.i("DocumentSnapshot added with ID: ${documentReference.id}")
//            }
//            .addOnFailureListener { e ->
//                Timber.i(e)
//            }

        val docRef = db
//            .collection("/Courses/B1-INFO-2020-2021/Weeks")
//            .document("40")
//            .collection("Weeks")
            .collection("/Courses/B1-INFO-2020-2021/Weeks/40/planning")
        docRef.get()
            .addOnSuccessListener { result ->
                val usableList = mutableListOf<CourseV2>()
                result.documents.forEach {
                    usableList.add(it.toCourseV2())
                }
                Timber.i("Usable list of data: \n $usableList")
            }
            .addOnFailureListener { e ->
                Timber.i(e)
            }
    }

    private fun launchSignInFlow() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Timber.i("Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
                testFireStore()
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Timber.i("Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

}