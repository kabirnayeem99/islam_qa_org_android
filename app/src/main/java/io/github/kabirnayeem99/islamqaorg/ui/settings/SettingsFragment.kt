package io.github.kabirnayeem99.islamqaorg.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentSettingsBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {
    override val layout: Int
        get() = R.layout.fragment_settings

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        subscribeToData()
    }

    private fun initViews() {
        binding.apply {
            clAboutButton.setOnClickListener {
                navController.navigate(R.id.action_settingsFragment_to_aboutFragment)
            }
            spinFiqh.prompt = getString(R.string.hint_fiqh)
            spinFiqh.setPopupBackgroundDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.bg_tappable_rounded
                )
            )
            val fiqhAdapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.fiqh_array,
                R.layout.spin_item_fiqh
            )
            spinFiqh.adapter = fiqhAdapter
        }
    }

    private val settingsViewModel: SettingsViewModel by viewModels()

    private fun subscribeToData() {
        settingsViewModel.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                getFiqh()
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {

                }
            }
        }
    }


}