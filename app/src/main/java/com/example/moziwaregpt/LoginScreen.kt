package com.example.moziwaregpt

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(navController: NavHostController) {
    var isConfirmed by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hasAttemptedLogin by remember {
        mutableStateOf(false)
    }
    var isUsernameError by remember {
        mutableStateOf(false)
    }
    var isPasswordError by remember {
        mutableStateOf(false)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xff356bfd).copy(alpha = 0.1F),
                        Color(0xff77ddf7).copy(alpha = 0F)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(113.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.height(36.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.waving_hand_svgrepo_com),
                        contentDescription = "Wave"
                    )

                    Spacer(modifier = Modifier.height(21.dp))

                    Text(text = "欢迎使用", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(text = "GPT私域大模型", fontSize = 20.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(32.dp))

                    TextField(
                        value = username,
                        onValueChange = {
                            username = it
                            if(hasAttemptedLogin) isUsernameError = !it.isEmailValid()
                        },
                        placeholder = {Text(text = "请输入帐号")},
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (isUsernameError) Color.Red else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.login_email),
                                contentDescription = "Login Info",
                                tint = if (isUsernameError) Color.Red else Color.Unspecified
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xffeef0f6),
                            focusedContainerColor = Color(0xffeef0f6),
                            errorIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedPlaceholderColor = if (isUsernameError) Color.Red else Color(
                                0xff999999
                            ),
                            focusedPlaceholderColor = if (isUsernameError) Color.Red else Color(0xff999999)
                        ),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TextField(
                        value = password,
                        onValueChange = {
                            password = it
                            if(hasAttemptedLogin) isPasswordError = !it.isPasswordValid()
                        },
                        placeholder = { Text(text = "请输入密码") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 1.dp,
                                color = if (isPasswordError) Color.Red else Color.Transparent,
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp),
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.login_secret),
                                contentDescription = "Login Password",
                                tint = if (isPasswordError) Color.Red else Color.Unspecified
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xffeef0f6),
                            focusedContainerColor = Color(0xffeef0f6),
                            errorIndicatorColor = Color.Transparent,
                            disabledIndicatorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            unfocusedPlaceholderColor = if (isPasswordError) Color.Red else Color(
                                0xff999999
                            ),
                            focusedPlaceholderColor = if (isPasswordError) Color.Red else Color(0xff999999)
                        ),
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                    )

                    Spacer(modifier = Modifier.height(35.dp))

                    Button(
                        onClick = {
                            // TODO: Check if credentials are accurate
                            hasAttemptedLogin = true
                            isUsernameError = !username.isEmailValid()
                            isPasswordError = !password.isPasswordValid()

                            if (!isUsernameError && !isPasswordError && isConfirmed) {
                                navController.navigate(Screens.HOME.name)
                            }

                        },
                        modifier = Modifier.padding(horizontal = 48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff1b5cb6)
                        )
                    ) {
                        Text(
                            text = "开始使用",
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                        )
                    }

                    TextButton(
                        onClick = { /*TODO*/ }, colors = ButtonDefaults.textButtonColors(
                            contentColor = Color(0xff999999)
                        )
                    ) {
                        Text(text = "切换登录方式")
                    }

                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = {
                    // TODO: Enables Terms and Conditions
                    isConfirmed = !isConfirmed
                }) {
                    Icon(
                        painter = painterResource(id = if (isConfirmed) R.drawable.ic_login_sel_y else R.drawable.ic_login_sel_n),
                        tint = Color.Unspecified,
                        contentDescription = "Terms and Conditions"
                    )
                }
                Text(text = "使用该功能需注册/登录平台账号,")
            }

            Text(text = "    阅读并同意《用户协议》、《隐私政策》")
        }
    }

}

fun String.isPasswordValid(): Boolean {
    val passwordRegex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"
    return passwordRegex.toRegex().matches(this)
}

fun String.isEmailValid(): Boolean {
    val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
    return emailRegex.toRegex().matches(this)
}

