package io.github.kabirnayeem99.islamqaorg.ui.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.ModalDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import dagger.hilt.android.AndroidEntryPoint
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.common.base.BaseFragment
import io.github.kabirnayeem99.islamqaorg.common.utility.ktx.showUserMessage
import io.github.kabirnayeem99.islamqaorg.databinding.FragmentSettingsBinding
import io.github.kabirnayeem99.islamqaorg.databinding.LayoutMadhabSelectorBinding
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import kotlinx.coroutines.launch
import timber.log.Timber

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
//                navController.navigate(R.id.action_settingsFragment_to_aboutFragment)
            }

            clMadhabButton.setOnClickListener {
                showFiqhSelectorDialog(BottomSheet(LayoutMode.WRAP_CONTENT)) { fiqh ->
                    when (fiqh) {
                        Fiqh.UNKNOWN -> binding.root.showUserMessage("You have not selected a madhab. The default is set to Hanafi Fiqh.")
                        else -> {
                            settingsViewModel.saveFiqh(fiqh)
                            binding.root.showUserMessage("From now on, you will see questions and answers based on ${fiqh.displayName} Fiqh.")
                        }
                    }
                }
            }
        }
    }

    private fun showFiqhSelectorDialog(
        dialogBehavior: DialogBehavior = ModalDialog,
        onFiqhSelected: (Fiqh) -> Unit,
    ) {
        MaterialDialog(requireContext(), dialogBehavior).show {
            title(R.string.label_madhab)

            val madhabBinding = LayoutMadhabSelectorBinding.inflate(layoutInflater)
            lifecycleOwner(viewLifecycleOwner)

            customView(
                R.layout.layout_madhab_selector,
                madhabBinding.root,
                scrollable = true,
                horizontalPadding = true
            )

            cornerRadius(24F)

            setPeekHeight(80)

            this.icon(R.drawable.ic_fiqh)

            positiveButton(R.string.hint_fiqh) {
                val selectedMadhab = when (madhabBinding.cgMadhab.checkedChipId) {
                    R.id.chip_hanafi -> Fiqh.HANAFI
                    R.id.chip_hanbali -> Fiqh.HANBALI
                    R.id.chip_shafii -> Fiqh.SHAFII
                    R.id.chip_maliki -> Fiqh.MALIKI
                    else -> Fiqh.UNKNOWN
                }
                onFiqhSelected(selectedMadhab)
            }

            negativeButton(android.R.string.cancel) {
                binding.root.showUserMessage(getString(R.string.warning_fiqh))
                onFiqhSelected(Fiqh.UNKNOWN)
            }
            cancelable(false)
        }
    }

    private val settingsViewModel: SettingsViewModel by viewModels()

    private fun subscribeToData() {
        settingsViewModel.apply {
            viewLifecycleOwner.lifecycleScope.launch {
                getFiqh()
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    uiState.collect(::handleUiState)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun handleUiState(uiState: SettingsUiState) {
        uiState.apply {
            val fiqhName = selectedFiqh.displayName.ifBlank { Fiqh.HANAFI.displayName }
            Timber.d(fiqhName)
            if (fiqhName.isNotBlank())
                binding.tvMadhabHint.text = "Your currently preferred madhab is $fiqhName."
//                getString(
//                    R.string.hint_selected_fiqh,
//                    selectedFiqh.displayName.ifBlank { Fiqh.HANAFI.displayName })
        }
    }

}