package com.andriyilliaandroidgeeks.moneysaver.presentation.categories.additional_composes

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.andriyilliaandroidgeeks.moneysaver.R
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.AccountsData.accountBgImg
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.CategoriesData
import com.andriyilliaandroidgeeks.moneysaver.data.data_base._test_data.VectorImg
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Category
import com.andriyilliaandroidgeeks.moneysaver.domain.model.Currency
import com.andriyilliaandroidgeeks.moneysaver.presentation.MainActivityViewModel
import com.andriyilliaandroidgeeks.moneysaver.presentation.accounts.additional_composes.*
import com.andriyilliaandroidgeeks.moneysaver.presentation.categories.CategoriesViewModel
import com.andriyilliaandroidgeeks.moneysaver.ui.theme.*

@Composable
fun EditCategory(
    isForSpendings: Boolean,
    isEditing: Boolean,
    category: Category = CategoriesData.defaultCategory,
    viewModel: CategoriesViewModel = hiltViewModel(),
    onAddCategoryAction: (Category) -> Unit,
    onDeleteCategory: (Category) -> Unit,
    onCancelIconClick: () -> Unit,
    baseCurrency: Currency,
){


    var setCurrencyTypeChange = remember { mutableStateOf(false) }


    var currencyType =  remember { mutableStateOf(viewModel.state.currenciesList.find { it.currencyName == baseCurrency.currencyName } ?: Currency()) }
    var title =  remember { mutableStateOf(category.title) }
    var img =  remember { mutableStateOf(category.categoryImg) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(backgroundSecondaryColor)
    ) {
        /*
            title: String,
    typeOfCategory: String,
    accountImg: AccountImg,
    onChangeImg: (AccountImg) -> Unit,
    onAddCategoryAction: (String) -> Unit,
    onCancelIconClick: () -> Unit){

      TopBarAccounts(vectorImg =  img.value ,title = title.value,onChangeImg = {img.value = it}, typeOfAccount = if(isEditing)  "Edit Account" else  "New Account",
            onAddAccountAction = {onAddAccountAction(

         */
        val checkedState = remember { mutableStateOf(isForSpendings) }
        TopBarCategoryEdit(vectorImg =  img.value ,title = title.value,onChangeImg = {img.value = it}, typeOfCategory = if(isEditing)  stringResource(
                    R.string.edit_category) else  stringResource(R.string.new_category),
            onAddCategoryAction = {

                onAddCategoryAction(
                    if(isEditing)
                        Category(uuid = category.uuid,categoryImg = img.value,currencyType = currencyType.value , title = it, isForSpendings = category.isForSpendings, creationDate = category.creationDate)
                    else
                        Category(categoryImg = img.value,currencyType = currencyType.value , title = it, isForSpendings = checkedState.value)

                )}, onCancelIconClick = {onCancelIconClick()},)

        Divider(modifier = Modifier.background(bordersSecondaryColor).height(2.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundPrimaryColor),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {


            Column(modifier = Modifier.shadow(elevation = 0.dp)) {

                Text(
                    modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),
                    text = stringResource(R.string.settings),
                    color = textPrimaryColor,
                    fontWeight = FontWeight.W500,
                    fontSize = 18.sp
                )

                accountEditInfoText(
                    upperText = stringResource(R.string.currency_type),
                    bottomText = currencyType.value.description + " " + currencyType.value.currencyName + " " +  "(" + currencyType.value.currency + ")" ,
                    onAction = {setCurrencyTypeChange.value = true},
                    startPadding = 16.dp,
                    topPadding = 8.dp)
            }

            Box(modifier = Modifier
                .background(backgroundSecondaryColor)
                .height(15.dp)
                .fillMaxWidth())

            if(!isEditing){
                Divider(modifier = Modifier.background(bordersSecondaryColor))


                Row(modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 0.dp)
                    .fillMaxWidth()
                    .shadow(elevation = 0.dp)
                    .clickable {},
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically){
                    Text(
                        modifier = Modifier.padding(16.dp, 8.dp, 0.dp, 16.dp),
                        text = stringResource(R.string.is_for_spending),
                        color = textPrimaryColor,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp
                    )

                    Switch(
                        checked = checkedState.value,
                        onCheckedChange = { checkedState.value = it }
                    )

                }
                Divider(modifier = Modifier.background(bordersSecondaryColor))

                Box(modifier = Modifier
                    .background(backgroundSecondaryColor)
                    .height(15.dp)
                    .fillMaxWidth())
            }


            Divider(modifier = Modifier.background(bordersSecondaryColor))
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
                    contentDescription = stringResource(R.string.delete_button)
                )

                Text(
                    modifier = Modifier.padding(24.dp, 16.dp, 0.dp, 16.dp),
                    text = stringResource(R.string.delete_category),
                    color = Color.Red,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp
                )

            }
            Divider(modifier = Modifier.background(bordersSecondaryColor))


        }

    }


    SetAccountCurrencyType(openDialog = setCurrencyTypeChange, returnType = { returnType -> setCurrencyTypeChange.value = false;   currencyType.value = returnType  /* currencyType.value = viewModel.state.currenciesList.find { currency ->  returnType == currency.currencyName }!!*/ })
}

@Composable
fun TopBarCategoryEdit(
    title: String,
    typeOfCategory: String,
    vectorImg: VectorImg,
    onChangeImg: (VectorImg) -> Unit,
    mainActivityViewModel: MainActivityViewModel = hiltViewModel(),
    onAddCategoryAction: (String) -> Unit,
    onCancelIconClick: () -> Unit){
    var text by rememberSaveable { mutableStateOf(title) }
    val focusManager = LocalFocusManager.current


    var icn = remember {
        mutableStateOf(vectorImg)
    }

    var icnTopBar by remember {
        mutableStateOf(vectorImg)
    }

    var setAccountImg = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Image(
            painter = if (mainActivityViewModel.state.accountBgImgBitmap != null)
                BitmapPainter(mainActivityViewModel.state.accountBgImgBitmap!!.asImageBitmap())
            else painterResource(accountBgImg),
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
                        .weight(1.2f), onClick = {onCancelIconClick()}) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            tint = whiteSurface,
                            contentDescription = stringResource(R.string.close_button)
                        )
                    }

                    Column(modifier = Modifier
                        .fillMaxHeight()
                        .weight(3.8f)
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.SpaceBetween) {

                        Text(modifier = Modifier.padding(16.dp, 4.dp, 0.dp, 0.dp) ,text = typeOfCategory, color = whiteSurface, fontWeight = FontWeight.W400 , fontSize = 22.sp)

                        Column(modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 0.dp)) {
                            Text(modifier = Modifier.padding(16.dp, 16.dp, 0.dp, 0.dp),text = stringResource(
                                                            R.string.name), color = whiteSurfaceTransparent, fontWeight = FontWeight.W400 , fontSize = 16.sp)
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


                    Column(modifier = Modifier
                        .weight(1.2f)
                        .fillMaxHeight()
                        .padding(0.dp, 0.dp, 13.dp, 0.dp),verticalArrangement = Arrangement.SpaceBetween, horizontalAlignment = Alignment.CenterHorizontally){
                        IconButton(modifier = Modifier
                            .size(35.dp, 35.dp),
                            onClick = { onAddCategoryAction(text) }) {
                            Icon(
                                imageVector = Icons.Default.Done,
                                tint = whiteSurface,
                                contentDescription = stringResource(R.string.apply_button)
                            )
                        }
                        if(!setAccountImg.value)
                            VectorIcon(Modifier.padding(0.dp,0.dp,0.dp,8.dp),Modifier.padding(8.dp) , vectorImg = icnTopBar, onClick = {setAccountImg.value = true},width = 50.dp , height = 50.dp, cornerSize = 50.dp)
                        else
                            VectorIcon(Modifier.padding(0.dp,0.dp,0.dp,8.dp),Modifier.padding(8.dp) , vectorImg = icnTopBar, onClick = {setAccountImg.value = true},width = 50.dp , height = 50.dp, cornerSize = 50.dp)
                    }

                }

            }

        }
    }
    SetImg(openDialog = setAccountImg, returnType={setAccountImg.value = false; onChangeImg(it) ; icn.value = it; icnTopBar = it } , listOfVectors = CategoriesData.categoryImges, chosenVectorImg = icn, idForCategory = true )

}

