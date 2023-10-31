package com.example.capstone_android.ui.onboarding

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.capstone_android.R
import com.example.capstone_android.database.PreferencesManager
import com.example.capstone_android.ui.Home
import com.example.capstone_android.ui.theme.accent
import com.example.capstone_android.ui.theme.darkGreen
import com.example.capstone_android.ui.theme.primary
import com.example.capstone_android.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController?) {
    val firstName = remember { mutableStateOf("") }
    val lastName = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val context = LocalContext.current
    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        Column {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(160.dp, 80.dp)
                    .align(CenterHorizontally)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(primary),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Let's get to know you",
                    color = white,
                    style = TextStyle(fontSize = 24.sp)
                )
            }

            Column(
                modifier = Modifier
                        .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .padding(24.dp),
            ) {
                Text(
                    text = "Personal Information",
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                OutlinedTextField(
                    value = firstName.value,
                    onValueChange = { firstName.value = it },
                    label = { Text("First name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp),
                    shape = RoundedCornerShape(8.dp),
                )

                OutlinedTextField(
                    value = lastName.value,
                    onValueChange = { lastName.value = it },
                    label = { Text("Last Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                )

                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape = RoundedCornerShape(8.dp),
                )

                Button(
                    onClick = {
                        if (validateInput(firstName.value, lastName.value, email.value)) {
                            val preferencesManager = PreferencesManager(context)
                            preferencesManager.saveData(PreferencesManager.KEY_FIRSTNAME, firstName.value)
                            preferencesManager.saveData(PreferencesManager.KEY_LASTNAME, lastName.value)
                            preferencesManager.saveData(PreferencesManager.KEY_EMAIL, email.value)
                            preferencesManager.setLoggedIn(true)
                            navController?.popBackStack()
                            navController?.navigate(Home.route)
                        } else {
                            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 60.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accent
                    )
                ) {
                    Text(text = "Register", color = darkGreen)
                }
            }
        }

    }
}

fun validateInput(firstName: String, lastName: String, email: String): Boolean {
    return firstName.isNotEmpty() && lastName.isNotEmpty() && email.isNotEmpty()
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    LoginScreen(null)
}