package com.example.moneysaver.presentation._components

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneysaver.data.data_base._test_data.VectorImg
import com.example.moneysaver.domain.model.Category
import com.example.moneysaver.domain.model.Currency
import com.example.moneysaver.presentation.accounts.additional_composes.SetAccountCurrencyType
import com.example.moneysaver.ui.theme.*
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun Calculator(
    vectorImg:  VectorImg,
    sumText: MutableState<String>,
    isSubmitted: MutableState<Boolean>,
    openDatePickerDialog: MutableState<Boolean>,
    focusManager: FocusManager,
    selectedCurrencyType: MutableState<Currency>,
    targetCurrencyType: Currency,
    returnCurrencyValue: (which : String , to : String) -> Double
) {
    var openSelectCurrencyDialog = remember { mutableStateOf(false) }
    Row(modifier = Modifier
        //  .padding(6.dp, 0.dp)
        .fillMaxWidth()
        .height(320.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.canBeEvaluatedAsMathExpr())
                        evaluateSumValue(sumText)
                    sumText.value+='÷'
                },
                bgColor = calculatorButton,
                focusManager = focusManager
            ) {
                Text(text = "÷", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.canBeEvaluatedAsMathExpr())
                        evaluateSumValue(sumText)
                    sumText.value+='×'
                },
                bgColor = calculatorButton,
                focusManager = focusManager
            ) {
                Text(text = "×", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.canBeEvaluatedAsMathExpr())
                        evaluateSumValue(sumText)
                    sumText.value+='-'
                },
                bgColor = calculatorButton,
                focusManager = focusManager
            ) {
                Text(text = "-", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.last().isMathOperator())
                        sumText.value=sumText.value.dropLast(1)
                    if(sumText.value.canBeEvaluatedAsMathExpr())
                        evaluateSumValue(sumText)
                    sumText.value+='+'
                },
                bgColor = calculatorButton,
                focusManager = focusManager
            ) {
                Text(text = "+", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='7'
                },
                focusManager = focusManager
            ) {
                Text(text = "7", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='4'
                },
                focusManager = focusManager
            ) {
                Text(text = "4", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='1'
                },
                focusManager = focusManager
            ) {
                Text(text = "1", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    openSelectCurrencyDialog.value=true
                },
                focusManager = focusManager
            ) {
                Text(text = selectedCurrencyType.value.currency, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='8'
                },
                focusManager = focusManager
            ) {
                Text(text = "8", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='5'
                },
                focusManager = focusManager
            ) {
                Text(text = "5", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='2'
                },
                focusManager = focusManager
            ) {
                Text(text = "2", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='0'
                },
                focusManager = focusManager
            ) {
                Text(text = "0", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='9'
                },
                focusManager = focusManager
            ) {
                Text(text = "9", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='6'
                },
                focusManager = focusManager
            ) {
                Text(text = "6", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {
                    if(sumText.value=="0") sumText.value=""
                    sumText.value+='3'
                },
                focusManager = focusManager
            ) {
                Text(text = "3", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            CalculatorButton(
                modifier = Modifier.weight(1f),
                onClick = {sumText.value+='.'},
                focusManager = focusManager
            ) {
                Text(text = ".", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            CalculatorButton(
                modifier = Modifier
                    .weight(1f),
                onClick = {
                    if(sumText.value.length>1) sumText.value=sumText.value.dropLast(1) else sumText.value="0"
                    if(sumText.value=="-") sumText.value="0"
                },
                bgColor = calculatorButton,
                focusManager = focusManager
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = null)
            }
            CalculatorButton(modifier = Modifier
                .weight(1f),
                onClick = {openDatePickerDialog.value = true},
                bgColor = calculatorButton,
                focusManager = focusManager
            ) {
                Icon(imageVector = Icons.Default.DateRange, contentDescription = null)
            }
            if(sumText.value.canBeEvaluatedAsMathExpr() || selectedCurrencyType.value!=targetCurrencyType) {
                CalculatorButton(
                    modifier = Modifier
                        .weight(2f),
                    onClick = {
                        if(sumText.value.canBeEvaluatedAsMathExpr()) {
                            evaluateSumValue(sumText)
                        }  else {
                            convertSum(sumText, selectedCurrencyType.value, targetCurrencyType, returnCurrencyValue)
                            selectedCurrencyType.value=targetCurrencyType
                        }
                    },
                    bgColor = vectorImg.externalColor,
                    focusManager = focusManager
                ) {
                    Text(text = "=", fontSize = 22.sp, fontWeight = FontWeight.Bold, color= Color.White)
                }
            }
            else {
                CalculatorButton(
                    modifier = Modifier
                        .weight(2f),
                    onClick = {isSubmitted.value=true},
                    bgColor = vectorImg.externalColor,
                    focusManager = focusManager
                ) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint= Color.White)
                }
            }
        }
    }

    SetAccountCurrencyType(openDialog = openSelectCurrencyDialog) { returnType ->
        openSelectCurrencyDialog.value = false
        convertSum(sumText, selectedCurrencyType.value, returnType, returnCurrencyValue)
        selectedCurrencyType.value = returnType
    }
}

private fun convertSum(
    sumText: MutableState<String>,
    startCurrencyType: Currency,
    targetCurrencyType: Currency,
    returnCurrencyValue: (which : String , to : String) -> Double
) {
    var sum = sumText.value.toDouble()
    sum = round(100.0 * sum * returnCurrencyValue(startCurrencyType.currencyName, targetCurrencyType.currencyName)) / 100.0
    sumText.value = sum.toCalculatorString()
}

private fun Char.isMathOperator(): Boolean {
    return this=='÷' || this == '×' || this == '-' || this == '+'
}

fun Double.toCalculatorString(): String {
    var str = ((this * 100.0).roundToInt() / 100.0).toString()
    while(str.length>1 && str.contains('.') && (str.last()=='0' || str.last()=='.'))
        str = str.dropLast(1)
    return str
}

@Composable
private fun CalculatorButton(modifier: Modifier = Modifier, onClick: ()->Unit = {}, bgColor: Color = calculatorButtonNumbers, focusManager: FocusManager, content: @Composable ()-> Unit) {
    Box(
        modifier = modifier
            .padding(0.1.dp)
            //  .clip(CircleShape)
            .border(0.3.dp, calculatorBorderColor)
            .fillMaxSize()
            .background(bgColor)
            .clickable {
                focusManager.clearFocus()
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

private fun evaluateSumValue(sumText: MutableState<String>) {
    val numbers = sumText.value.split("÷|×|-|\\+".toRegex())
    val secondNumber = if(numbers.size>1 && !numbers[1].isNullOrBlank()) numbers[1].toDouble() else numbers[0].toDouble()
    if(secondNumber!=0.0)
        sumText.value = evaluateSimpleMathExpr(sumText.value).toCalculatorString()
}

private fun String.canBeEvaluatedAsMathExpr(): Boolean {
    if(this.isNullOrEmpty() || !this.contains("÷|×|-|\\+".toRegex())) return false

    if(this.contains("÷|×|-|\\+".toRegex()) && this[0]!='-') return true
    return this.drop(1).contains("÷|×|-|\\+".toRegex())
}


private fun evaluateSimpleMathExpr(mathExprStr: String): Double {
    val n0IsNegative = mathExprStr[0]=='-'

    val numbers = if(n0IsNegative) mathExprStr.drop(1).split("÷|×|-|\\+".toRegex())
    else mathExprStr.split("÷|×|-|\\+".toRegex())

    val n0 = if(n0IsNegative) -numbers[0].toDouble() else numbers[0].toDouble()
    val n1 = if(numbers.size>1 && !numbers[1].isNullOrBlank()) numbers[1].toDouble() else n0

    return if(mathExprStr.contains('÷'))
        n0/n1
    else if(mathExprStr.contains('×'))
        n0*n1
    else if(mathExprStr.contains('-'))
        n0-n1
    else
        n0+n1
}