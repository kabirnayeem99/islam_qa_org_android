package io.github.kabirnayeem99.islamqaorg.ui.search


import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
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
import io.github.kabirnayeem99.islamqaorg.ui.common.PageTransitionAnimation
import io.github.kabirnayeem99.islamqaorg.ui.common.TopBarActionButton
import io.github.kabirnayeem99.islamqaorg.ui.destinations.QuestionDetailsScreenDestination
import io.github.kabirnayeem99.islamqaorg.ui.home.QuestionItemCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
@Destination(style = PageTransitionAnimation::class)
fun SearchScreen(
    navigator: DestinationsNavigator,
    viewModel: SearchViewModel = hiltViewModel(),
) {

    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState

    fun onQuestionClick(url: String) {
        scope.launch {
            if (url.isNotBlank()) navigator.navigate(QuestionDetailsScreenDestination(url))
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 12.dp),
        topBar = { SearchScreenTopBar(navigator) },
        content = { contentPadding ->
            Column {
                SearchField(uiState, viewModel, contentPadding)
                AnimatedContent(targetState = uiState.isSearchResultLoading) { isLoading ->

                    if (!isLoading)
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
                    else LinearProgressIndicator(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp)
                            .align(Alignment.CenterHorizontally)
                            .clip(CircleShape)
                    )
                }

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

    val transparentTextFieldColors = TextFieldDefaults.textFieldColors(
        textColor = MaterialTheme.colorScheme.onBackground,
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