package io.github.kabirnayeem99.islamqaorg.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.ui.common.ScreenTitle
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen(navigator: DestinationsNavigator) {

    Scaffold(modifier = Modifier
        .fillMaxSize()
        .padding(top = 12.dp), topBar = {
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
    }) {
        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(62.dp))
                val settingsLabel = stringResource(id = R.string.label_settings)
                ScreenTitle(homePageTitle = settingsLabel)
                Spacer(modifier = Modifier.height(52.dp))
            }
        }
    }
}