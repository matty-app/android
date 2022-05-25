package com.matryoshka.projectx.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import org.junit.Rule
import org.junit.Test

class ListItemTest {
    @get: Rule
    val composeRule = createComposeRule()

    private val someText = "Some text"

    @Test
    fun shouldDisplayIcon() {
        val testTag = "icon"
        val iconDescription = "Info"

        composeRule.setContent {
            ListItem(
                icon = {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = iconDescription,
                        modifier = Modifier.testTag(testTag)
                    )
                }
            ) {
                Text(text = someText)
            }
        }

        composeRule.onNodeWithTag(testTag)
            .assertIsDisplayed()
            .assertContentDescriptionEquals(iconDescription)
    }

    @Test
    fun shouldDisplayText() {
        val testTag = "text"

        composeRule.setContent {
            ListItem {
                Text(text = someText, modifier = Modifier.testTag(testTag))
            }
        }

        composeRule.onNodeWithTag(testTag)
            .assertIsDisplayed()
            .assertTextEquals(someText)
    }

    @Test
    fun shouldDisplayTrailing() {
        val testTag = "trailing"
        val checked = true

        composeRule.setContent {
            ListItem(
                trailing = {
                    Switch(
                        checked = checked,
                        onCheckedChange = {},
                        modifier = Modifier.testTag(testTag)
                    )
                }
            ) {
                Text(text = someText)
            }
        }

        composeRule.onNodeWithTag(testTag).assertIsDisplayed()
            .assertHasClickAction()
    }

    @Test
    fun shouldSetMinHeight() {
        val testTag = "listItem"
        val minHeightOfMaterialListItem = 48.dp //see https://material.io/components/lists#specs

        composeRule.setContent {
            ListItem(
                Modifier
                    .testTag(testTag)
            ) {
                Text(text = someText)
            }
        }

        composeRule.onNodeWithTag(testTag)
            .assertHeightIsAtLeast(minHeightOfMaterialListItem)
    }

    @Test
    fun shouldUseModifierFromArgs() {
        val testTag = "listItem"

        composeRule.setContent {
            ListItem(
                Modifier
                    .testTag(testTag)
                    .clickable { }
            ) {
                Text(text = someText)
            }
        }

        composeRule.onNodeWithTag(testTag).assertHasClickAction()
    }
}