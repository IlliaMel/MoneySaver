package com.example.moneysaver.presentation.accounts.additional_composes
import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.TextFieldDefaults.indicatorLine
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.R
import com.example.moneysaver.presentation._components.dividerForTopBar
import com.example.moneysaver.ui.theme.*

@Composable
fun EditAccount(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(gray)
    ) {
        TopBarAccounts(typeOfAccount= "Edit Account" , onAddAccountAction = { }, onCancelIconClick = {})
        dividerForTopBar()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(whiteSurface),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {


            Column(modifier = Modifier.shadow(elevation = 1.dp)) {

                Text(
                    modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),
                    text = "Account",
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )

                accountEditInfoText(
                    upperText = "Type",
                    bottomText = "Normal",
                    onAction = {},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
                accountEditInfoText(
                    upperText = "Currency Type",
                    bottomText = "USA - $",
                    onAction = {},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
                accountEditInfoText(
                    upperText = "Description",
                    bottomText = "None   ",
                    onAction = {},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
            }

            Box(modifier = Modifier.background(gray).height(15.dp).fillMaxWidth())

            Column(modifier = Modifier.shadow(elevation = 1.dp)){

                Text(
                    modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),
                    text = "Balance",
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )
                Row(modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .clickable {},
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(
                        modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 16.dp),
                        text = "Account Balance",
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )

                    Text(
                        modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp),
                        text = "0 $",
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )

                }
                Divider()
                Row(modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .clickable {},
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(
                        modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 16.dp),
                        text = "Account Balance",
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )

                    Text(
                        modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp),
                        text = "0 $",
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )
                }
                Divider()
                Row(modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .clickable {},
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(
                        modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 16.dp),
                        text = "Add to whole Balance amount",
                        color = Color.Black,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )
                    val checkedState = remember { mutableStateOf(true) }
                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it }
                    )

                }
            }

            Box(modifier = Modifier.background(gray).height(20.dp).fillMaxWidth())

            Row(modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .clickable {}
                .shadow(elevation = 1.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically){
                Icon(modifier = Modifier
                    .padding(16.dp, 16.dp, 0.dp, 16.dp),
                    imageVector = Icons.Default.Delete,
                    tint = Color.Red,
                    contentDescription = "Delete button"
                )

                Text(
                    modifier = Modifier.padding(24.dp, 16.dp, 0.dp, 16.dp),
                    text = "Delete Account",
                    color = Color.Red,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp
                )


            }


        }

    }

}
@Composable
fun accountEditInfoText(
    startPadding : Dp = 8.dp,
    topPadding : Dp = 2.dp,
    endPadding : Dp = 0.dp,
    bottomPadding : Dp = 4.dp,
    upperText: String,
    bottomText: String,
    onAction: () -> Unit
){

    Column (modifier = Modifier
        .fillMaxWidth()
        .clickable { onAction() },
    verticalArrangement = Arrangement.SpaceEvenly,
    horizontalAlignment = Alignment.Start){
            Text(
                modifier = Modifier.padding(startPadding, topPadding,endPadding, 0.dp),
                text = upperText,
                color = Color.Black,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier.padding(startPadding, topPadding/4,endPadding, bottomPadding),
                text = bottomText,
                color = currencyColorZero,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp
            )
        Divider()
    }


}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBarAccounts(
    typeOfAccount: String,
    onAddAccountAction: () -> Unit,
    onCancelIconClick: () -> Unit){
    var text by rememberSaveable { mutableStateOf("Cash") }
    val interactionSource = remember { MutableInteractionSource() }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Image(
            painter = painterResource(R.drawable.bg5),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            Column(modifier = Modifier
                .fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally){
                Row(modifier = Modifier
                    .padding(0.dp, 48.dp, 0.dp, 0.dp)
                    .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top){
                    IconButton(modifier = Modifier
                        .padding(8.dp, 0.dp, 0.dp, 0.dp)
                        .size(40.dp, 40.dp)
                        .weight(1f), onClick = {onCancelIconClick()}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = whiteSurface,
                            contentDescription = "Delete button"
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .weight(4.5f)
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween) {

                        Text(modifier = Modifier.padding(16.dp, 4.dp, 0.dp, 0.dp) ,text = typeOfAccount, color = whiteSurface, fontWeight = FontWeight.W400 , fontSize = 22.sp)

                        Column(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                        Text(modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),text = "Name", color = whiteSurfaceTransparent, fontWeight = FontWeight.W400 , fontSize = 16.sp)
                        TextField(
                                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 18.dp),
                                textStyle = TextStyle(color = whiteSurface, fontWeight = FontWeight.W400 , fontSize = 24.sp),
                                value = text,
                                onValueChange = {
                                    text = it
                                },
                                maxLines = 1,
                                colors = TextFieldDefaults.textFieldColors(
                                textColor = whiteSurface,
                                disabledTextColor = Color.Transparent,
                                backgroundColor = Color.Transparent,
                                focusedIndicatorColor = whiteSurface,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            )
                        }
                    }



                    IconButton(modifier = Modifier
                        .padding(0.dp, 0.dp, 8.dp, 0.dp)
                        .size(40.dp, 40.dp)
                        .weight(1f), onClick = { onAddAccountAction }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            tint = whiteSurface,
                            contentDescription = "Apply button"
                        )
                    }

                }

           }

        }
    }
}