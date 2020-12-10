package com.novyapp.superplanning.ui.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.novyapp.superplanning.R
import com.novyapp.superplanning.data.Result
import com.novyapp.superplanning.databinding.MainPageFragmentBinding
import com.novyapp.superplanning.toSortedByWeeksMap
import timber.log.Timber

private const val REQUEST_CODE = 12


// TODO("display the current week and not this 1st week available")
class MainPage : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var binding: MainPageFragmentBinding
    var firstload = true
    private val viewModel by viewModels<MainPageViewModel> {
        MainPageViewModelFactory()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = MainPageFragmentBinding.inflate(inflater, container, false)
        db = Firebase.firestore
        if (FirebaseAuth.getInstance().currentUser == null) {
            Timber.i("Need to connect user")
            launchSignInFlow()
        } else {
            Timber.i("User is already connected")
        }

        val adapter = ViewPagerListAdapter()
        binding.viewPager.adapter = adapter



        viewModel.courses.observe(viewLifecycleOwner) {
            if (it is Result.Success) {
                Timber.i("newWeek: ${it.data}")
                Timber.i("newWeek: SHOULD SUBMIT")
                adapter.submitList(it.data.toSortedByWeeksMap().toList())
                if (firstload) {
                    Timber.i("newWeek: First Load occurring")
                    loadAdjacentWeeks()
                    firstload = false
                }
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.error_getting_data),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_IDLE) {
                    Timber.i("newWeek: PageSelected")
                    loadAdjacentWeeks()
                }
            }
        })

        return binding.root
    }

    private fun loadAdjacentWeeks() {
        if (!binding.viewPager.canScrollHorizontally(1)) {
            Timber.i("newWeek: CANT scroll to right")
            viewModel.askForNextWeek()
        } else {
            Timber.i("newWeek: Can scroll right")
        }
        if (!binding.viewPager.canScrollHorizontally(-1)) {
            Timber.i("newWeek: CANT scroll to left")
            viewModel.askForPreviousWeek()
        } else {
            Timber.i("newWeek: Can scroll left")
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
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Timber.i("Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

}

