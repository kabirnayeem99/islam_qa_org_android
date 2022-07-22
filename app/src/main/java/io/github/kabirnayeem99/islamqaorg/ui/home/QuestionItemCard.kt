package io.github.kabirnayeem99.islamqaorg.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.shimmer
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.domain.entity.Fiqh
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question
import io.github.kabirnayeem99.islamqaorg.ui.theme.ArabicFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionItemCard(
    question: Question,
    modifier: Modifier = Modifier,
    shouldHavePadding: Boolean = true,
    onClick: () -> Unit = {}
) {

    Card(
        modifier = modifier
            .padding(vertical = 8.dp, horizontal = if (shouldHavePadding) 24.dp else 0.dp)
            .fillMaxWidth(),
        border = BorderStroke(
            width = 0.8.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4F)
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.10F),
        ),
        onClick = { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.9F)) {
                Text(
                    text = question.question,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = question.fiqh.ifBlank { Fiqh.HANAFI.displayName },
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontFamily = ArabicFontFamily,
                            )
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
            ForwardArrowIndicator()
        }
    }
}

@Composable
private fun ForwardArrowIndicator() {
    Box(
        modifier = Modifier
            .height(22.dp)
            .width(22.dp)
            .border(
                0.8.dp,
                MaterialTheme.colorScheme.primary,
                CircleShape
            )
            .clip(CircleShape),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = stringResource(id = R.string.content_desc_go_to_details),
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
            alignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.primary
                )
                .fillMaxWidth()
                .fillMaxWidth()
                .padding(5.dp)

        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionItemPlaceholder(shouldHavePadding: Boolean = true) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = if (shouldHavePadding) 24.dp else 0.dp)
            .fillMaxWidth()
            .shimmer(),
        border = BorderStroke(
            width = 0.8.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4F)
        ),
        colors = CardDefaults.outlinedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint.copy(
                alpha = 0.10F
            ),
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.9F)) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .width(250.dp)
                        .height(18.dp)
                        .background(color = MaterialTheme.colorScheme.onBackground)
                        .shimmer(),
                )
                Spacer(modifier = Modifier.padding(top = 4.dp))
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                    modifier = Modifier
                        .height(20.dp)
                        .width(80.dp)
                ) {
                    Box(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Box(
                            modifier = Modifier
                                .background(color = MaterialTheme.colorScheme.onSecondary)
                                .shimmer(),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(5.dp))
            Box(
                modifier = Modifier
                    .height(22.dp)
                    .width(22.dp)
                    .border(
                        0.8.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.5F),
                        CircleShape
                    )
                    .clip(CircleShape),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_arrow_forward),
                    contentDescription = stringResource(id = R.string.content_desc_go_to_details),
                    contentScale = ContentScale.Fit,
                    alignment = Alignment.Center,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
                        )
                        .fillMaxWidth()
                        .fillMaxWidth()
                        .padding(5.dp)

                )
            }
        }
    }
}