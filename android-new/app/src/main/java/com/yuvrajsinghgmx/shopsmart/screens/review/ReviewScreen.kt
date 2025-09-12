package com.yuvrajsinghgmx.shopsmart.screens.review

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.request.ImageRequest
import coil3.compose.AsyncImage
import com.yuvrajsinghgmx.shopsmart.R
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.RatingSummary
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.Review
import com.yuvrajsinghgmx.shopsmart.data.modelClasses.ReviewTarget
import com.yuvrajsinghgmx.shopsmart.screens.productDetailsScreen.components.StarRating
import com.yuvrajsinghgmx.shopsmart.ui.theme.GreenPrimary
import com.yuvrajsinghgmx.shopsmart.ui.theme.TextPrimary

@Composable
fun ReviewScreen(
    target: ReviewTarget,
    viewModel: ReviewViewModel = hiltViewModel(),
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(target) {
        viewModel.loadReviews(
            target
        )
    }
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(top = 13.dp, start = 8.dp, end = 8.dp, bottom = 8.dp)
    ) {
        TopBar(navController)
        LazyColumn(
            modifier =
                Modifier.fillMaxSize(), contentPadding = PaddingValues(vertical = 10.dp)
        ) {
            when (uiState) {
                is ReviewUiState.Loading -> {
                    item {
                        Text(
                            text = "Loading reviews",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is ReviewUiState.Error -> {
                    item {
                        Text(
                            (uiState as ReviewUiState.Error).message,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is ReviewUiState.Success -> {
                    val summary = (uiState as ReviewUiState.Success).ratingSummary
                    val reviews = (uiState as ReviewUiState.Success).reviews
                    summary?.let {
                        item {
                            RatingSummaryView(it)
                        }
                    }
                    item {
                        WriteReview(
                            viewModel,
                            target
                        )
                    }
                    item { ReviewTab(viewModel) }
                    if (reviews.isEmpty()) {
                        item {
                            Text(
                                text = "No Reviews yet",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        items(reviews, key = { it.id }) { review ->
                            Card(
                                modifier = Modifier
                                    .padding(
                                        horizontal = 12.dp,
                                        vertical = 6.dp
                                    )
                                    .fillMaxWidth(),
                                elevation = CardDefaults.cardElevation(4.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                            ) {
                                ReviewItem(
                                    review = review,
                                    onToggle = { viewModel.toggleHelpful(target, review.id) })
                            }
                        }
                    }
                }
            }

        }
    }
}

@Composable
fun TopBar(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Reviews",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        IconButton(
            onClick =
                { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowBackIosNew,
                contentDescription = "Back"
            )
        }
    }
}

@Composable
fun RatingSummaryView(summary: RatingSummary) {
    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(
                modifier = Modifier
                    .weight(0.3f)
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = String.format("%.1f", summary.average),
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                )
                StarRating(summary.average.toFloat(), 5, 18.dp)
                Text(
                    text = "${summary.totalRatings} ratings", color = Color.Gray
                )
            }
            Column(
                modifier = Modifier
                    .weight(0.7f)
                    .padding(5.dp)
            ) {
                summary.distribution.forEach { (star, percentage) ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(vertical = 2.dp)
                    ) {
                        Text(
                            "$star★",
                            color = TextPrimary,
                            modifier = Modifier.width(25.dp)
                        )
                        LinearProgressIndicator(
                            progress =
                                percentage / 100f,
                            modifier = Modifier
                                .weight(1f)
                                .padding(horizontal = 8.dp)
                                .height(8.dp),
                            color = GreenPrimary,
                            trackColor = Color.LightGray
                        )
                        Text(text = "$percentage%", modifier = Modifier.width(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WriteReview(viewModel: ReviewViewModel, target: ReviewTarget) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Write a Review",
                style = MaterialTheme.typography.bodyLarge
            )
            RatingSelector(viewModel.rating, viewModel::onRatingSelected)
            OutlinedTextField(
                value = viewModel.reviewText, onValueChange = {
                    viewModel.onReviewTextChanged(
                        it
                    )
                }, placeholder = {
                    Text(
                        "share your experience",
                        color = Color.Gray
                    )
                }, modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary, unfocusedBorderColor = Color.Gray
                )
            )
            Button(
                onClick = {
                    viewModel.onSubmitReview(
                        target
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text(
                    "Post Review"
                )
            }
        }
    }
}

@Composable
fun ReviewTab(viewModel: ReviewViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "All Reviews", style = MaterialTheme.typography.titleSmall)
        Spacer(
            modifier =
                Modifier.width(10.dp)
        )
        Text(
            text =
                "Most Recent",
            style = MaterialTheme.typography.bodySmall,
            color = if (viewModel.selectedTab == "Most Recent") GreenPrimary else Color.Gray,
            modifier = Modifier.clickable {
                viewModel.onTabSelected(
                    "Most Recent"
                )
            })
        Text(
            text = "Most Helpful",
            color = if (viewModel.selectedTab == "Most Helpful") GreenPrimary else Color.Gray,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.clickable {
                viewModel.onTabSelected(
                    "Most Helpful"
                )
            })
    }
}

@Composable
fun ReviewItem(
    review: Review,
    onToggle: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = ImageRequest
                    .Builder(LocalContext.current)
                    .data(review.userImage)
                    .crossfade(true)
                    .build(),
                contentDescription = "profile picture",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.user_image_placeholder),
                error = painterResource(R.drawable.user_image_placeholder)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = review.username
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StarRating(
                        review.rating.toFloat(), 5, 12.dp
                    )
                    Text(
                        text =
                            review.timeAgo,
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }
        Text(text = review.comment)
        Spacer(
            modifier =
                Modifier.height(2.dp)
        )
        Row(
            horizontalArrangement =
                Arrangement.spacedBy(5.dp), verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onToggle() },
                modifier = Modifier.size(24.dp)
            ) {
                Icon(
                    imageVector = if (review.isHelpful)
                        Icons.Filled.ThumbUp
                    else Icons.Outlined.ThumbUp,
                    contentDescription = "helpful",
                    tint = Color.Gray
                )
            }
            Text(
                text =
                    "Helpful (${review.helpfulCount})",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
