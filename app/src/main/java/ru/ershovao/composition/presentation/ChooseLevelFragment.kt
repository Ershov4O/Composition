package ru.ershovao.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.ershovao.composition.R
import ru.ershovao.composition.databinding.FragmentChooseLevelBinding
import ru.ershovao.composition.domain.entity.GameSettings
import ru.ershovao.composition.domain.entity.Level

class ChooseLevelFragment : Fragment() {

    companion object {

        fun newInstance(): ChooseLevelFragment {
            return ChooseLevelFragment()
        }
    }

    private lateinit var binding: FragmentChooseLevelBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnLevelTest.setOnClickListener {
            navigateToGameFragment(Level.TEST)
        }
        binding.btnLevelEasy.setOnClickListener {
            navigateToGameFragment(Level.EASY)
        }
        binding.btnLevelNormal.setOnClickListener {
            navigateToGameFragment(Level.NORMAL)
        }
        binding.btnLevelHard.setOnClickListener {
            navigateToGameFragment(Level.HARD)
        }
    }

    private fun navigateToGameFragment(level: Level) {
        findNavController().navigate(ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level))
    }
}