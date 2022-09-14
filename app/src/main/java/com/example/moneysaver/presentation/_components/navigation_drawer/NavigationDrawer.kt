package com.example.moneysaver.presentation._components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.presentation._components.navigation_drawer.MenuBlock
import com.example.moneysaver.presentation._components.navigation_drawer.MenuItem

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = stringResource(id = R.string.app_name), fontSize = 50.sp)
    }
}

@Composable
fun DrawerBody(
    blocks: List<MenuBlock>,
    modifier: Modifier = Modifier,
    onItemClick: (MenuItem) -> Unit,
    onSwitchClick: (MenuItem) -> Unit = {},

) {
    LazyColumn(modifier) {
        items(blocks) { block ->

            Column(modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp)) {
                Text(
                    modifier = Modifier.padding(16.dp, 4.dp),
                    text = block.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff4c4ab0)
                )
                for(i in 0 until block.items.size) {
                    val item = block.items[i]

                    Column() {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .clickable {
                                    onItemClick(item)
                                }
                                .padding(16.dp, 4.dp, 4.dp, 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f).padding(0.dp, 8.dp)) {
                                Text(
                                    text = item.title,
                                    fontSize = 16.sp,

                                )
                                Text(
                                    text = item.description,
                                    fontSize = 15.sp,
                                    color = Color(0xff4c4ab0)
                                )
                            }
                            if(item.hasSwitch) {
                                val checkedState = remember { mutableStateOf(item.switchIsActive) }
                                Switch(
                                    checked = checkedState.value,
                                    onCheckedChange = {
                                        onSwitchClick(item)
                                        checkedState.value = it
                                        item.switchIsActive = it
                                    },
                                    colors = SwitchDefaults.colors(
                                        checkedThumbColor = Color(0xff4c4ab0),
                                        uncheckedThumbColor = Color.DarkGray,
                                        checkedTrackColor = Color(0xff4c4ab0),
                                        uncheckedTrackColor = Color.DarkGray,
                                    )
                                )
                            }
                        }
                        if(i<block.items.size-1) {
                            Row {
                                Spacer(modifier = Modifier.width(56.dp))
                                Divider()
                            }
                        }
                    }
                }
                Divider()

            }

        }
    }
}