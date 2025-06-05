package com.ohuo.application.pages.manager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ohuo.application.decompose.ManagementsComponent

@Composable
fun ManagementsScreen(component: ManagementsComponent, snackBarHostState: SnackbarHostState) {
    val state by component.viewModel.stateFlow.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            val statusBarPadding = WindowInsets.statusBars.asPaddingValues()

            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        start = 4.dp,
                        end = 4.dp,
                        top = statusBarPadding.calculateTopPadding(),
                        bottom = 32.dp
                    )
            ) {
                item {
                    CardList() {
                        Item {
                            CardItem("Apps",component::navigateApps)
                        }
                        Item {
                            CardItem("Websites") {}
                        }
                        Item {
                            CardItem("Databases") {}
                        }
                        Item {
                            CardItem("Docker") {}
                        }
                    }
                }
                item {
                    CardList() {
                        Item {
                            CardItem("Monitoring") {}
                        }
                        Item {
                            CardItem("Files") {}
                        }
                        Item {
                            CardItem("SSH") {}
                        }
                        Item {
                            CardItem("Processes") {}
                        }
                    }
                }
                item {
                    CardList() {
                        Item {
                            CardItem("Logs") {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CardList(
    modifier: Modifier = Modifier.padding(10.dp).fillMaxWidth(),
    content: ItemGroup.() -> Unit
) {
    val color = MaterialTheme.colorScheme.surface
    Card(modifier) {
        val group = ItemGroup()
        content(group)
        val items = group.items
        val length = items.size
        items.forEachIndexed { i, it ->
            val showDivider = length > 1 && length != i + 1
            Row(
                Modifier.fillMaxWidth().height(60.dp)
                    .drawBehind {
                        if (showDivider) {
                            val borderWidth = 1.dp.toPx()
                            drawLine(
                                color = color,
                                start = Offset(10.dp.toPx(), size.height - borderWidth / 2),
                                end = Offset(size.width - 10.dp.toPx(), size.height - borderWidth / 2),
                                strokeWidth = borderWidth
                            )
                        }
                    }) {
                it()
            }
        }
    }
}

class ItemGroup {
    val items: MutableList<@Composable () -> Unit> = mutableListOf()
    fun addItem(item: @Composable () -> Unit) {
        items.add(item)
    }
}

fun ItemGroup.Item(item: @Composable () -> Unit) {
    addItem(item)
}

@Composable
private fun CardItem(text: String, onNavigateTo: () -> Unit) {
    Row(
        Modifier.fillMaxWidth().height(60.dp)
            .clickable {
                onNavigateTo()
            }) {
        val style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 60.sp)
        Text(text, style = style, modifier = Modifier.padding(10.dp, 0.dp))
    }
}



