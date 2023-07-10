package io.github.kabirnayeem99.islamqaorg.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.ui.common.ScreenTitle
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton
import kotlinx.coroutines.launch
import timber.log.Timber


@Destination
@Composable
fun SettingsScreen(
    settingsViewModel: SettingsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator
) {

    LaunchedEffect(true) {
        settingsViewModel.getFiqh()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        topBar = { SettingsTopAppBar(navigator) },
    ) {
        it.toString()
        LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
            item {
                Spacer(modifier = Modifier.height(62.dp))
                val settingsLabel = stringResource(id = R.string.label_settings)
                ScreenTitle(homePageTitle = settingsLabel)
                Spacer(modifier = Modifier.height(52.dp))
                MadhabSettingsItem(settingsViewModel.uiState.selectedFiqh) { fiqh ->
                    settingsViewModel.saveFiqh(fiqh)
                }
                Spacer(modifier = Modifier.height(22.dp))
                AboutSettingsItem(navigator)

            }
        }
    }
}


@Composable
private fun MadhabSettingsItem(selectedFiqh: Fiqh, onFiqhSelected: (Fiqh) -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2F)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Favorite,
                        stringResource(id = R.string.hint_fiqh)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.label_madhab),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontStyle = FontStyle.Normal
                        )
                    )
                }
                FiqhSelectorDropDownMenu(selectedFiqh) { fiqh ->
                    onFiqhSelected(fiqh)
                }
            }
            Text(text = stringResource(id = R.string.hint_fiqh))
        }
    }
}


@Composable
private fun AboutSettingsItem(navigator: DestinationsNavigator) {
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2F)
            )
            .clickable {
//                scope.launch { navigator.navigate(AboutScreenDestination()) }
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        stringResource(id = R.string.hint_fiqh)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = stringResource(id = R.string.label_about),
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontStyle = FontStyle.Normal
                        )
                    )
                }
                Icon(
                    Icons.Outlined.ArrowForward,
                    stringResource(id = R.string.hint_fiqh)
                )
            }
            Text(text = stringResource(id = R.string.hint_about))
        }
    }
}

@Composable
private fun FiqhSelectorDropDownMenu(selectedFiqh: Fiqh, onFiqhSelected: (Fiqh) -> Unit) {
    val fiqhList = Fiqh.values().filterNot { it.displayName.isBlank() }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(top = 4.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            Modifier
                .clickable { expanded = !expanded }
                .align(Alignment.Center),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = selectedFiqh.displayName,
                modifier = Modifier.padding(end = 8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onBackground)
            )
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(id = R.string.content_desc_select_fiqh)
            )
            DropdownMenu(
                modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                expanded = expanded,
                onDismissRequest = { expanded = false }) {
                fiqhList.forEach { fiqh ->
                    val thisItemSelected = fiqh.paramName == selectedFiqh.paramName
                    DropdownMenuItem(onClick = {
                        Timber.d(fiqh.toString())
                        expanded = false
                        onFiqhSelected(fiqh)
                    }) {
                        Row {
                            Icon(
                                imageVector = Icons.Outlined.Favorite,
                                contentDescription = stringResource(id = R.string.hint_fiqh),
                                tint = if (thisItemSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = fiqh.displayName,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = if (thisItemSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingsTopAppBar(navigator: DestinationsNavigator) {
    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {},
        navigationIcon = {
            TopBarActionButton(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(id = R.string.content_desc_go_back)
            ) {
                scope.launch {
                    navigator.navigateUp()
                }
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.6F)),
        elevation = 0.dp,
    )
}