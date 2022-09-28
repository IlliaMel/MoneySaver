package com.example.moneysaver.presentation._components

import android.media.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.presentation._components.navigation_drawer.MenuBlock
import com.example.moneysaver.presentation._components.navigation_drawer.MenuItem
import com.example.moneysaver.ui.theme.*

@Composable
fun DrawerHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.25f)
            .background(backgroundPrimaryColor)
            .padding(vertical = 0.dp),
        contentAlignment = Alignment.Center
    ) {
        //Text(text = stringResource(id = R.string.app_name), fontSize = 50.sp)
        Image(
            painter = painterResource(id = R.drawable.logo_transparent),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            colorFilter = ColorFilter.tint(textSecondaryColor)
        )
    }
}

@Composable
fun DrawerBody(
    blocks: List<MenuBlock>,
    modifier: Modifier = Modifier,
    onItemClick: (MenuItem) -> Unit,
    onSwitchClick: (MenuItem) -> Unit = {},

) {
    LazyColumn(modifier.background(backgroundPrimaryColor)) {
        items(blocks) { block ->

            Column(modifier = Modifier.background(backgroundPrimaryColor).padding(0.dp, 16.dp, 0.dp, 0.dp)) {
                Text(
                    modifier = Modifier.padding(16.dp, 4.dp),
                    text = block.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xff4c4ab0)
                )
                for(i in 0 until block.items.size) {
                    val item = block.items[i]

                    Column(modifier = Modifier.background(backgroundPrimaryColor)) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                                .background(backgroundPrimaryColor)
                                .clickable {
                                    onItemClick(item)
                                }
                                .padding(16.dp, 4.dp, 4.dp, 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = textPrimaryColor
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f).background(backgroundPrimaryColor).padding(0.dp, 8.dp)) {
                                Text(
                                    text = item.title,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    color = textPrimaryColor
                                )
                                Text(
                                    text = item.description,
                                    fontSize = 15.sp,
                                    color = Color(0xff4c4ab0),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            if(item.hasSwitch) {

                                Switch(
                                    checked = item.switchIsActive,
                                    onCheckedChange = {
                                        onSwitchClick(item)
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
                            Row (modifier = Modifier.background(backgroundPrimaryColor)){
                                Spacer(modifier = Modifier.background(backgroundPrimaryColor).width(56.dp))
                                Divider(modifier = Modifier.background(bordersPrimaryColor))
                            }
                        }
                    }
                }
                Divider(modifier = Modifier.background(bordersPrimaryColor))

            }

        }
    }
}