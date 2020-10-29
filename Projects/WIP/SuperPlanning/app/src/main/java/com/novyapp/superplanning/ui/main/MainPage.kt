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
import com.novyapp.superplanning.databinding.MainPageFragmentBinding
import timber.log.Timber

private const val REQUEST_CODE = 12

class MainPage : Fragment() {

    private lateinit var binding: MainPageFragmentBinding
    private val viewModel by viewModels<MainPageViewModel> {
        MainPageViewModelFactory()
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = MainPageFragmentBinding.inflate(inflater, container, false)
        if (FirebaseAuth.getInstance().currentUser == null) launchSignInFlow()


        val adapter = ViewPagerAdapter()
        binding.viewPager.adapter = adapter



        viewModel.courses.observe(viewLifecycleOwner) {
            if(it.isNullOrEmpty()) Timber.i("Course list is Null or Empty")
            else {
                it.forEach { item -> Timber.i(item.toString()) }
                adapter.submitList(it)
//                adapter.submitList(it.map { course ->
//                    CourseListViews(CourseListAdapter.COURSE_VIEW, course)
//                } as ArrayList<CourseListViews>)

//                adapter.submitList(it)
                Timber.i("List submitted to adapter")
            }
        }

        return binding.root
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