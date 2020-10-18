package com.novyapp.test.fasttypingtraining.ui.game

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.novyapp.test.fasttypingtraining.R
import com.novyapp.test.fasttypingtraining.ResourcesProvider
import com.novyapp.test.fasttypingtraining.WordsApplication
import com.novyapp.test.fasttypingtraining.databinding.GameFragmentBinding
import com.novyapp.test.fasttypingtraining.toolbar


class GameFragment : Fragment() {

    lateinit var binding: GameFragmentBinding
    val viewModel by viewModels<GameViewModel> {
        GameViewModelFactory(
            (requireContext().applicationContext as WordsApplication).wordsRepository,
            (requireContext().applicationContext as WordsApplication).resourcesProvider
        )
    }
    lateinit var resProvider: ResourcesProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = GameFragmentBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        resProvider = (requireContext().applicationContext as WordsApplication).resourcesProvider

        setObservers()
        setListeners()

        toolbar?.title = getString(R.string.gameFragmentTitle)
        return binding.root
    }


    private fun setListeners() {
        binding.gameEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (viewModel.gameState.value == GameState.NOT_STARTED) viewModel.startGame()
                viewModel.computeIsCorrect(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })


    }

    private fun setObservers() {
        viewModel.score.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.scoreTextView.text = getString(R.string.score, it)
            }
        })

        viewModel.gameState.observe(viewLifecycleOwner, Observer {
            if (it == GameState.FINISHED) {
                hideKeyboardFrom(context!!, view!!)
                this.findNavController().navigate(
                    GameFragmentDirections
                        .actionGameFragmentToResultsFragment(
                            viewModel.score.value ?: 0,
                            COUNTDOWN_TIME
                        )
                )
            }
        })

        viewModel.textColor.observe(viewLifecycleOwner, Observer {
            binding.gameEditText.setTextColor(it)
        })

    }

    private fun hideKeyboardFrom(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

}