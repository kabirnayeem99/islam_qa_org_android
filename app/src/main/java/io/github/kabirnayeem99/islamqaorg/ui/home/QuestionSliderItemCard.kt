package io.github.kabirnayeem99.islamqaorg.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import io.github.kabirnayeem99.islamqaorg.R
import io.github.kabirnayeem99.islamqaorg.domain.entity.Question

@Composable
fun QuestionSliderItemCard(question: Question, index: Int, onClick: () -> Unit) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(start = if (index == 0) 24.dp else 12.dp, end = 12.dp, top = 8.dp)
            .width(300.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .border(
                border = BorderStroke(
                    width = 0.8.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {


        Image(
            painter = painterResource(id = R.drawable.bg_banner),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .padding(0.8.dp)
                .clip(RoundedCornerShape(12.dp)),
            alignment = Alignment.Center,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary, BlendMode.Color),
            contentDescription = null,
        )

        Text(
            text = question.question,
            style = MaterialTheme.typography.headlineMedium.copy(
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .padding(24.dp),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Box(
            modifier = Modifier
                .padding(18.dp)
                .height(22.dp)
                .width(22.dp)
                .border(
                    0.8.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4F),
                    RoundedCornerShape(12.dp)
                )
                .clip(CircleShape)
                .align(Alignment.BottomEnd),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = stringResource(id = R.string.content_desc_go_to_details),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White),
                alignment = Alignment.Center,
                modifier = Modifier

                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
                    .border(
                        0.8.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4F),
                        RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth()
                    .fillMaxWidth()
                    .padding(5.dp)

            )
        }
    }
}

@Composable
fun QuestionSliderItemCardPlaceholder(index: Int) {
    BoxWithConstraints(
        modifier = Modifier
            .padding(start = if (index == 0) 24.dp else 12.dp, end = 12.dp, top = 8.dp)
            .width(300.dp)
            .height(200.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5F))
            .border(
                border = BorderStroke(
                    width = 0.8.dp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {


        Box(
            modifier = Modifier
                .padding(0.8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)),
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .border(
                    border = BorderStroke(
                        width = 0.8.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5F)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.secondary)
                .width(250.dp)
                .height(12.dp)
                .padding(24.dp),
        )

        Box(
            modifier = Modifier
                .padding(18.dp)
                .height(22.dp)
                .width(22.dp)
                .border(
                    0.8.dp,
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.4F),
                    RoundedCornerShape(12.dp)
                )
                .clip(CircleShape)
                .align(Alignment.BottomEnd),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_arrow_forward),
                contentDescription = stringResource(id = R.string.content_desc_go_to_details),
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(Color.White.copy(alpha = 0.8F)),
                alignment = Alignment.Center,
                modifier = Modifier

                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8F)
                    )
                    .border(
                        0.8.dp,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.4F),
                        RoundedCornerShape(12.dp)
                    )
                    .fillMaxWidth()
                    .fillMaxWidth()
                    .padding(5.dp)

            )
        }
    }
}