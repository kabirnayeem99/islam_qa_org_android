package io.github.kabirnayeem99.islamqaorg.ui.search


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton
import io.github.kabirnayeem99.islamqaorg.ui.destinations.QuestionDetailsScreenDestination
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionItemCard
import kotlinx.coroutines.launch

@Composable
@Destination
fun SearchScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = hiltViewModel(),
) {

    val uiState = viewModel.uiState

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        topBar = { SearchScreenTopBar(navigator) },
        content = { contentPadding ->
            Column {
                SearchField(uiState, viewModel, contentPadding)

                SearchResults(uiState, navigator)
            }
        },
    )
}

@Composable
private fun SearchResults(uiState: SearchUiState, navigator: DestinationsNavigator) {

    val scope = rememberCoroutineScope()

    fun onQuestionClick(url: String) {
        scope.launch {
            if (url.isNotBlank()) navigator.navigate(QuestionDetailsScreenDestination(url))
        }
    }

    LazyColumn(
        contentPadding = PaddingValues(top = 24.dp),
        content = {
            itemsIndexed(
                uiState.searchQuestionResults,
                key = { i, q -> q.url.plus(i) },
            ) { _, question ->
                QuestionItemCard(
                    question = question,
                    onClick = { onQuestionClick(question.url) },
                )
            }
        },
    )
}

@Composable
private fun SearchField(
    uiState: SearchUiState,
    viewModel: SearchViewModel,
    contentPadding: PaddingValues,
) {

    val transparentTextFieldColors = TextFieldDefaults.colors(
//        focusedLabelColor = MaterialTheme.colorScheme.onBackground,
        disabledTextColor = Color.Transparent,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 12.dp + contentPadding.calculateTopPadding(),
                start = 24.dp,
                end = 24.dp
            )
            .height(58.dp)
    ) {
        TextField(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .weight(fill = true, weight = 0.8F)
                .height(58.dp),

            keyboardActions = KeyboardActions(
                onDone = {
                    viewModel.fetchSearchResults()
                },
                onSearch = {
                    viewModel.fetchSearchResults()
                },
            ),

            value = uiState.query,
            onValueChange = { q -> viewModel.changeQueryText(q) },
            colors = transparentTextFieldColors,
            singleLine = true,
            readOnly = uiState.isSearchResultLoading,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
            )
        )

        Spacer(modifier = Modifier.weight(fill = true, weight = 0.04F))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxHeight()
                .weight(fill = true, weight = 0.16F)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(0.8F))
                .clickable { viewModel.fetchSearchResults() },
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Outlined.Search,
                contentDescription = stringResource(id = R.string.content_desc_search),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        }
    }

}

@Composable
private fun SearchScreenTopBar(navigator: DestinationsNavigator) {

    val scope = rememberCoroutineScope()

    TopAppBar(
        title = {},
        navigationIcon = {
            TopBarActionButton(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(id = R.string.content_desc_go_back)
            ) {
                scope.launch { navigator.navigateUp() }
            }
        },
        backgroundColor = MaterialTheme.colorScheme.background.copy(alpha = 0.6F),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background.copy(alpha = 0.6F)),
        elevation = 0.dp,
    )
}