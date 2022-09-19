package com.example.moneysaver.presentation._components

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.MoneySaver
import com.example.moneysaver.R
import com.example.moneysaver.data.data_base._test_data.AccountsData
import com.example.moneysaver.presentation.MainActivity
import com.example.moneysaver.presentation.utils.showBiometricPrompt



@Composable
fun SecureCodeEntering(
    context: Context,
    isCorrectFunc: () -> Unit,
    inNewPassword: (String) -> Unit,
    correctCode: String,
    isCodeOpenFromNavBar: Boolean,
) {
    // I'm using the same duration for all animations.

    BackHandler(enabled = isCodeOpenFromNavBar) {
        isCorrectFunc()
    }
    
    Box(modifier = Modifier.fillMaxSize()){
        var isCorrect = false
        var code = remember{ mutableStateOf("") }
        if(code.value.length == correctCode.length && !isCodeOpenFromNavBar){
            isCorrect = code.value == correctCode
        }

        Image(
            painter = painterResource(R.drawable.bg5),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            alignment = Alignment.BottomEnd,
            modifier = Modifier.matchParentSize(),
            colorFilter = ColorFilter.tint(color = AccountsData.normalAccount.accountImg.externalColor, blendMode = BlendMode.Softlight)
        )

        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(2f)
                .background(Color.Transparent)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Image(
                    painter = painterResource(R.drawable.logo_transparent),
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    alignment = Alignment.Center,
                )
            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.Transparent)
                .padding(0.dp, 0.dp, 0.dp, 0.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally) {

                Text(fontSize = 18.sp, fontWeight = FontWeight.W500, text = if(isCodeOpenFromNavBar) "Enter New Password" else "Enter You Password", color = Color.White)
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp))
                val animVisibleState = remember { MutableTransitionState(false) }
                    .apply { targetState = true }

                secureCodeFilled(context = context,code.value.length, isCorrect = isCorrect, code,animVisibleState,isCorrectFunc = { isCorrectFunc()}, isCodeOpenFromNavBar)

            }

            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(2.5f)
                .background(Color.Transparent)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally) {

                var list = listOf("1","2","3","4","5","6","7","8","9","","0")

                LazyVerticalGrid(
                    modifier = Modifier
                        .padding(0.dp, 0.dp, 0.dp, 0.dp),
                    columns = GridCells.Fixed(3),
                    userScrollEnabled = false,
                    verticalArrangement = Arrangement.Center,
                    horizontalArrangement = Arrangement.Center,
                    // content padding

                    content = {
                        list.forEach {
                            item {
                                secureCodeButton(symbol = it, onClick = {if (it != "" && code.value.length < 6) {code.value += it ;
                                    vibratePhone(context)
                                } })
                            }
                        }

                        item {
                            secureCodeButton(imageVector = Icons.Filled.KeyboardArrowLeft , onClick = {code.value = code.value.dropLast(1); vibratePhone(context) })
                        }

                    })
            }
            if(!isCodeOpenFromNavBar){
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .background(Color.Transparent),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        onClick = { showBiometricPrompt(
                            onSucceeded = {code.value = correctCode},
                            activity = MainActivity.instance!!
                        ) }
                    ) {
                        Text(fontSize = 14.sp, fontWeight = FontWeight.W500, text = "Use Fingerprint", color = Color.White)
                    }
                }
            }else {

                Row(modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
                    .background(Color.Transparent),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically) {

                    Button(
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        onClick = { isCorrectFunc() }
                    ) {
                        Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.Center) {
                            Text(fontSize = 14.sp, fontWeight = FontWeight.W500, text = "Cancel", color = Color.White)
                        }

                    }

                    Button(
                        shape = RoundedCornerShape(30.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),
                        onClick = {
                            if(code.value.length == 6){
                                Toast.makeText(
                                    MoneySaver.applicationContext(), "New Password Added",
                                    Toast.LENGTH_SHORT
                                ).show()
                                inNewPassword(code.value)
                            }else {

                                Toast.makeText(
                                    MoneySaver.applicationContext(), "Password must have 6 digits",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            }
                    ) {
                        Box(modifier = Modifier.width(80.dp), contentAlignment = Alignment.Center) {
                            Text(fontSize = 14.sp, fontWeight = FontWeight.W500, text = "Save", color = Color.White)
                        }
                    }
                }

            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun secureCodeFilled(
    context: Context,
    filled: Int = 0,
    isCorrect: Boolean,
    code: MutableState<String>,
    animVisibleState: MutableTransitionState<Boolean>,
    isCorrectFunc: () -> Unit,
    isCodeOpenFromNavBar: Boolean, ){



    if (!animVisibleState.targetState &&
        !animVisibleState.currentState && !isCodeOpenFromNavBar
    ) {
        if(isCorrect)
            isCorrectFunc()
        else{
            vibratePhone(context, 400)
            Toast.makeText(
                MoneySaver.applicationContext(), "Password is not correct",
                Toast.LENGTH_SHORT
            ).show()
            code.value = ""
        }

    }

    Box(modifier = Modifier
        .fillMaxWidth(), contentAlignment = Alignment.Center){
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 0.dp,),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            for (i in 1..6) {
                var color = if((filled!= 6 && i <= filled) || isCorrect) {
                    Color(233, 233, 233, 236)
                }else {
                    Color(233, 233, 233, 53)
                }

                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(color)
                )
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 0.dp, 0.dp, 0.dp,),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center) {

            for (i in 1..6) {
                var color = if(filled == 6 && !isCorrect) {
                    Color(233, 233, 233, 236)
                }else {
                    Color(233, 233, 233, 53)
                }
                AnimatedVisibility(
                    visibleState = animVisibleState,
                    enter = fadeIn(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300))
                ) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .animateEnterExit(
                                // Slide in/out the inner box.
                                enter = slideInVertically(animationSpec = tween(500)),
                                exit = slideOutVertically(animationSpec = tween(500))
                            ), contentAlignment =  Alignment.Center
                    ){
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(4.dp)
                                .clip(CircleShape)
                                .background(color)){

                            if(filled == 6 && !isCodeOpenFromNavBar){
                                animVisibleState.targetState = false
                            }

                        }
                    }
                }
            }
        }
    }

}




@Composable
fun secureCodeButton(symbol : String = "", imageVector: ImageVector?= null, onClick : () -> Unit){

    var modifier = if(symbol != "" || imageVector!= null) Modifier
        .size(75.dp)
        .padding(24.dp, 4.dp, 24.dp, 4.dp)
        .clip(CircleShape)
        .clickable { onClick() }
        .background(Color.Transparent)
    else
        Modifier
            .background(Color.Transparent)
            .size(75.dp)
            .padding(24.dp, 4.dp, 24.dp, 4.dp)
            .clip(CircleShape)

    Box(modifier = modifier, contentAlignment = Alignment.Center)
    {

        if(imageVector == null)
            Text(fontSize = 27.sp, fontWeight = FontWeight.W400, text = symbol, color = Color.White)
        else {
            Icon(
                modifier = Modifier
                    .padding(0.dp, 2.dp, 0.dp, 0.dp)
                    .width(40.dp)
                    .height(40.dp),
                imageVector = imageVector,
                contentDescription = null,
                tint = Color.White
            )
        }
    }

}




fun vibratePhone(context: Context?, time : Long  = 80) {
    val vibrator = context?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(time, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(time)
    }
}
