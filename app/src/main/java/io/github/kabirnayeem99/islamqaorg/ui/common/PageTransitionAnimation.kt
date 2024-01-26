package io.github.kabirnayeem99.islamqaorg.ui.common

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.navigation.NavBackStackEntry
import com.ramcosta.composedestinations.spec.DestinationStyle


const val TRANSITION_TIME_IN_MILLIS = 400

object PageTransitionAnimation : DestinationStyle.Animated {
    override fun AnimatedContentTransitionScope<NavBackStackEntry>.enterTransition(): EnterTransition? {
        return slideInHorizontally(
            initialOffsetX = { 1000 }, animationSpec = tween(TRANSITION_TIME_IN_MILLIS)
        )
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.exitTransition(): ExitTransition? {
        return slideOutHorizontally(
            targetOffsetX = { -1000 }, animationSpec = tween(TRANSITION_TIME_IN_MILLIS)
        )
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popEnterTransition(): EnterTransition? {
        return slideInHorizontally(
            initialOffsetX = { -1000 }, animationSpec = tween(TRANSITION_TIME_IN_MILLIS)
        )
    }

    override fun AnimatedContentTransitionScope<NavBackStackEntry>.popExitTransition(): ExitTransition? {
        return slideOutHorizontally(
            targetOffsetX = { 1000 }, animationSpec = tween(TRANSITION_TIME_IN_MILLIS)
        )
    }
}

