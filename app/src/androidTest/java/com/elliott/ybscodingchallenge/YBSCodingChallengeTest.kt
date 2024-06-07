package com.elliott.ybscodingchallenge

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class YBSCodingChallengeEspressoTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private fun waitForPageLoad() {
        composeRule.apply {
            waitUntil {
                onAllNodesWithTag("loadingSpinner")
                    .fetchSemanticsNodes().size == 1
            }
            waitUntil {
                onAllNodesWithTag("image0")
                    .fetchSemanticsNodes().size == 1
            }
        }
    }


    @Test
    fun testAddingAndRemovingTagViaSearch() {
        waitForPageLoad()
        composeRule.apply {
            onNodeWithTag("searchBox").performClick().performTextInput("Leeds")
            onNodeWithTag("searchButton").performClick()
            onNodeWithTag("tag1").assertExists().performClick()
            waitUntil {
                onAllNodesWithTag("tag1")
                    .fetchSemanticsNodes().isEmpty()
            }
        }
    }

    @Test
    fun testAddingAndRemovingUserIdViaSearch() {
        waitForPageLoad()
        composeRule.apply {
            onNodeWithTag("searchBox").performClick().performTextInput("1234567@N04")
            onNodeWithTag("searchButton").performClick()
            onNodeWithTag("usernameSearchText").assertExists().performClick()
            waitUntil {
                onAllNodesWithTag("usernameSearchText")
                    .fetchSemanticsNodes().isEmpty()
            }
        }
    }

    @Test
    fun testAddingAndRemovingTagViaTagTap() {
        waitForPageLoad()
        composeRule.apply {
            waitUntil {
                onAllNodesWithTag("imageTag0")
                    .fetchSemanticsNodes().size == 1
            }
            onNodeWithTag("imageTag0").performClick()
            onNodeWithTag("tag1").assertExists().performClick()
            waitUntil {
                onAllNodesWithTag("tag1")
                    .fetchSemanticsNodes().isEmpty()
            }
        }
    }

    @Test
    fun testAddingAndRemovingUserIdViaTagTap() {
        waitForPageLoad()
        composeRule.apply {
            waitUntil {
                onAllNodesWithTag("imageUserId0")
                    .fetchSemanticsNodes().size == 1
            }
            onNodeWithTag("imageUserId0").performClick()
            onNodeWithTag("usernameSearchText").assertExists().performClick()
            waitUntil {
                onAllNodesWithTag("usernameSearchText")
                    .fetchSemanticsNodes().isEmpty()
            }
        }
    }

    @Test
    fun testNavigatingToDetailScreen() {
        waitForPageLoad()
        composeRule.apply {
            waitUntil {
                onAllNodesWithTag("image0")
                    .fetchSemanticsNodes().size == 1
            }
            onNodeWithTag("image0").performClick()
            waitUntil {
                onAllNodesWithTag("detailImage")
                    .fetchSemanticsNodes().size == 1
            }
            onNodeWithTag("detailDate").assertExists()
            onNodeWithTag("detailViews").assertExists()
            onNodeWithTag("detailDescription").assertExists()
        }
    }

    @Test
    fun testDetailsDoNotExistOnHomeScreenImages() {
        waitForPageLoad()
        composeRule.apply {
            waitUntil {
                onAllNodesWithTag("image0")
                    .fetchSemanticsNodes().size == 1
            }
            onNodeWithTag("detailDate").assertDoesNotExist()
            onNodeWithTag("detailViews").assertDoesNotExist()
            onNodeWithTag("detailDescription").assertDoesNotExist()
        }
    }

    @Test
    fun testEmptyResponseResultsInSomethingWentWrongScreen() {
        waitForPageLoad()
        composeRule.apply {
            onNodeWithTag("searchBox").performClick().performTextInput("failResponse")
            onNodeWithTag("searchButton").performClick()
            waitUntil {
                onAllNodesWithText("No pictures found matching search terms. Please try again")
                    .fetchSemanticsNodes().size == 1
            }
        }
    }

    @Test
    fun testErrorResponseResultsInTryAgain() {
        waitForPageLoad()
        composeRule.apply {
            onNodeWithTag("searchBox").performClick().performTextInput("throwError")
            onNodeWithTag("searchButton").performClick()
            waitUntil {
                onAllNodesWithTag("tryAgain")
                    .fetchSemanticsNodes().size == 1
            }
            onNodeWithTag("tryAgain").performClick()
        }
    }
}