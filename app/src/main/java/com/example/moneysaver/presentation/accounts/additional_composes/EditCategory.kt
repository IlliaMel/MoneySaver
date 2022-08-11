import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.data.data_base.test_data.AccountsData
import com.example.moneysaver.data.data_base.test_data.CategoriesData
import com.example.moneysaver.domain.account.Account
import com.example.moneysaver.domain.category.Category
import com.example.moneysaver.presentation.accounts.additional_composes.*
import com.example.moneysaver.ui.theme.currencyColor
import com.example.moneysaver.ui.theme.gray
import com.example.moneysaver.ui.theme.whiteSurface
import com.example.moneysaver.ui.theme.whiteSurfaceTransparent

@Composable
fun EditCategory(
    isEditing: Boolean,
    category: Category = CategoriesData.categoriesList[0],
    onAddCategoryAction: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onCancelIconClick: () -> Unit
){


    var setCurrencyTypeChange = remember { mutableStateOf(false) }


    var currencyType =  remember { mutableStateOf(category.currencyType) }
    var title =  remember { mutableStateOf(category.title) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(gray)
    ) {

        TopBarCategoryEdit(title = title.value, typeOfCategory = if(isEditing)  "Edit Category" else  "New Category",
            onAddCategoryAction = {onAddCategoryAction(Category(currencyType = currencyType.value , title = title.value))}, onCancelIconClick = {onCancelIconClick()})
        // dividerForTopBar()
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
                    text = "Settings",
                    color = Color.Black,
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )

                accountEditInfoText(
                    upperText = "Currency Type",
                    bottomText = currencyType.value,
                    onAction = {setCurrencyTypeChange.value = true},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
            }

            Box(modifier = Modifier
                .background(gray)
                .height(15.dp)
                .fillMaxWidth())


            Divider()


            Row(modifier = Modifier
                .padding(0.dp, 0.dp, 0.dp, 0.dp)
                .clickable { onDeleteCategory(category) }
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
                    text = "Delete Category",
                    color = Color.Red,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp
                )


            }


        }

    }


    SetAccountCurrencyType(openDialog = setCurrencyTypeChange, returnType = {setCurrencyTypeChange.value = false; currencyType.value = it })
}

@Composable
fun TopBarCategoryEdit(
    title: String,
    typeOfCategory: String,
    onAddCategoryAction: (String) -> Unit,
    onCancelIconClick: () -> Unit){
    var text by rememberSaveable { mutableStateOf(title) }
    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Image(
            painter = painterResource(com.example.moneysaver.R.drawable.bg5),
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
                            contentDescription = "Close button"
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .weight(4.5f)
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween) {

                        Text(modifier = Modifier.padding(16.dp, 4.dp, 0.dp, 0.dp) ,text = typeOfCategory, color = whiteSurface, fontWeight = FontWeight.W400 , fontSize = 22.sp)

                        Column(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                            Text(modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),text = "Name", color = whiteSurfaceTransparent, fontWeight = FontWeight.W400 , fontSize = 16.sp)
                            TextField(
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                                modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 8.dp),
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
                        .weight(1f), onClick = { onAddCategoryAction(text) }) {
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