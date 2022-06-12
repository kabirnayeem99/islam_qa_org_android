package io.github.kabirnayeem99.islamqaorg.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.ui.common.PageTransitionAnimation
import io.github.kabirnayeem99.islamqaorg.ui.common.ScreenTitle
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton
import timber.log.Timber

@OptIn(ExperimentalMaterial3Api::class)
@Destination(style = PageTransitionAnimation::class)
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {


    LaunchedEffect(true) {
        settingsViewModel.getFiqh()
    }

    val selectedFiqh = settingsViewModel.uiState.selectedFiqh

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        topBar = { SettingsTopAppBar(navigator) },
    ) {


        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(62.dp))
                val settingsLabel = stringResource(id = R.string.label_settings)
                ScreenTitle(homePageTitle = settingsLabel)
                Spacer(modifier = Modifier.height(52.dp))
                FiqhSelectorDropDownMenu(selectedFiqh, settingsViewModel)
            }
        }
    }
}

@Composable
private fun FiqhSelectorDropDownMenu(
    selectedFiqh: Fiqh,
    settingsViewModel: SettingsViewModel
) {
    val fiqhList = Fiqh.values().filterNot { it.displayName.isBlank() }
    var expanded by remember { mutableStateOf(false) }

    Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Row(
            Modifier
                .padding(24.dp)
                .clickable {
                    expanded = !expanded
                }
                .padding(8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedFiqh.displayName,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 8.dp)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.content_desc_select_fiqh)
            )
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                fiqhList.forEach { fiqh ->
                    DropdownMenuItem(onClick = {
                        Timber.d(fiqh.toString())
                        expanded = false
                        settingsViewModel.saveFiqh(fiqh)
                    }) {
                        Row {
                            Icon(
                                imageVector = Icons.Filled.FavoriteBorder,
                                contentDescription = stringResource(id = R.string.hint_fiqh)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(text = fiqh.displayName)

                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsTopAppBar(navigator: DestinationsNavigator) {
    TopAppBar(
        title = {},
        navigationIcon = {
            TopBarActionButton(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(id = R.string.content_desc_go_back)
            ) {
                navigator.navigateUp()
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.6F)),
        elevation = 0.dp,
    )
}