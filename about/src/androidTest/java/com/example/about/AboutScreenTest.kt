package com.example.about

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class AboutScreenTest {
    @Rule
    @JvmField
    val composeTestRule = createAndroidComposeRule<AboutActivity>()

    @Test
    fun testGreeting() {
        composeTestRule.setContent {
            AboutScreen()
        }
        composeTestRule.onNodeWithTag("web").assertIsDisplayed()
    }
}