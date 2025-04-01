package com.navarro.spotifygold.services

import android.util.Log
import com.navarro.spotifygold.entities.DtoLogIn
import com.navarro.spotifygold.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

suspend fun logIn(
    usernameEmail: String,
    password: String
): String? {
    val isEmail = usernameEmail.contains('@')
    val dto = DtoLogIn(
        username = if (!isEmail) usernameEmail else "",
        password = password,
        email = if (isEmail) usernameEmail else ""
    )

    Log.d("LoginHandler", "Logging in with $dto")

    val jsonString = Json.encodeToString(dto)
    Log.d("LoginHandler", "Logging in with $jsonString")
    val client = OkHttpClient()
    val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaType())

    val request = Request.Builder()
        .url("${Constants.url}user/login")
        .post(requestBody)
        .build()

    return withContext(Dispatchers.IO) {
        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                response.body?.string()
            } else {
                Log.d("LoginHandler", response.code.toString())
                Log.d("LoginHandler", response.body?.string() ?: "Error logging in.")
                null
            }
        }
    }
}

suspend fun signUp(
    username: String,
    email: String,
    password: String
): Boolean {
    val dto = DtoLogIn(
        username = username,
        password = password,
        email = email
    )

    val jsonString = Json.encodeToString(dto)
    val client = OkHttpClient()
    val requestBody = jsonString.toRequestBody("application/json; charset=utf-8".toMediaType())

    Log.d("LoginHandler", "Signing up with $dto")
    Log.d("LoginHandler", "Signing up with $jsonString")
    Log.d("LoginHandler", "Signing up with $requestBody")

    val request = Request.Builder()
        .url("${Constants.url}user/register")
        .post(requestBody)
        .build()

    return withContext(Dispatchers.IO) {
        try {
            client.newCall(request).execute().use { response ->
                StaticToast.showToast(response.body?.string() ?: "Error signing up.")
                response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e("LoginHandler", "Error signing up.", e)
            false
        }
    }
}
