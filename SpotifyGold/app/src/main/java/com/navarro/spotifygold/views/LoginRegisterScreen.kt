package com.navarro.spotifygold.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.navarro.spotifygold.R
import com.navarro.spotifygold.components.login.LabelTextField
import com.navarro.spotifygold.services.StaticToast
import com.navarro.spotifygold.services.logIn
import com.navarro.spotifygold.services.preferences.PreferencesKeys
import com.navarro.spotifygold.services.preferences.PreferencesService
import com.navarro.spotifygold.services.signUp
import com.navarro.spotifygold.ui.theme.Black0
import com.navarro.spotifygold.ui.theme.Black100
import com.navarro.spotifygold.ui.theme.Black30
import com.navarro.spotifygold.ui.theme.Gold50
import kotlinx.coroutines.runBlocking

@Composable
fun LoginRegisterScreen(
    isLogged: MutableState<Boolean>
) {
    val context = LocalContext.current

    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }

    var isLogin by remember { mutableStateOf(true) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize(0.7f)
        //.background(Gold80Transparent)
    ) {
        Text(
            text = stringResource(
                id = if (isLogin) R.string.login_title else R.string.login_register_title
            ),
            modifier = Modifier.padding(16.dp),
            color = Black0,
            fontSize = if (isLogin) 24.sp else 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            LabelTextField(
                label = stringResource(
                    id = if (isLogin) R.string.login_username_or_email else R.string.login_username
                ),
                value = username
            )
            LabelTextField(
                label = stringResource(id = R.string.login_password),
                value = password,
                isPassword = true
            )
            if (!isLogin) {
                LabelTextField(
                    label = stringResource(id = R.string.login_email),
                    value = email
                )
            }
        }
        Spacer(modifier = Modifier.height(30.dp))
        Button(
            onClick = {
                runBlocking {
                    if (isLogin) {
                        val ticket = logIn(
                            usernameEmail = username.value.trim(),
                            password = password.value.trim()
                        )

                        if (ticket != null) {
                            isLogged.value = true
                            PreferencesService.setProperty(
                                context = context,
                                key = PreferencesKeys.TOKEN.key,
                                value = ticket
                            )
                        } else {
                            StaticToast.showToast(
                                context.getString(
                                    R.string.login_inavlid_credentials
                                )
                            )
                        }
                    } else {
                        val success = signUp(
                            username = username.value.trim(),
                            email = email.value.trim(),
                            password = password.value.trim()
                        )

                        if (success) {
                            StaticToast.showToast(
                                context.getString(
                                    R.string.login_check_email
                                )
                            )
                        } else {
                            // Nothing
                        }
                    }
                }
            }
        ) {
            Text(
                text = stringResource(
                    id = if (isLogin) R.string.login_log_in else R.string.login_sign_up
                ),
                color = Black100
            )
        }
        Spacer(modifier = Modifier.height(30.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val textSize: TextUnit = 11.sp
            Text(
                text = stringResource(
                    id = if (isLogin) R.string.login_question_sign_up
                    else R.string.login_question_log_in
                ),
                color = Black30,
                fontSize = textSize
            )
            Text(
                text = stringResource(
                    id = if (isLogin) R.string.login_sign_up else R.string.login_log_in
                ),
                color = Gold50,
                fontSize = textSize,
                modifier = Modifier
                    .clickable {
                        isLogin = !isLogin
                    }
            )
        }
    }
}