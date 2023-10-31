package com.example.capstone_android.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.capstone_android.ui.theme.accent
import com.example.capstone_android.ui.theme.darkGreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavHostController?) {
    val preferencesManager = PreferencesManager(LocalContext.current)
    Scaffold(
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
        ) {

            Text(
                text = "Personal Information",
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )

            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Logo",
                modifier = Modifier
                    .padding(24.dp)
                    .size(160.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
            )

            ProfileItem(
                "First Name",
                preferencesManager.getData(PreferencesManager.KEY_FIRSTNAME, "")
            )
            ProfileItem(
                "Last Name",
                preferencesManager.getData(PreferencesManager.KEY_LASTNAME, "")
            )
            ProfileItem("Email", preferencesManager.getData(PreferencesManager.KEY_EMAIL, ""))

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    preferencesManager.clearData()
                    navController?.navigate(route = Login.route) {
                        popUpTo(Home.route) {
                            inclusive = true
                        }
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
                Text(text = "Logout", color = darkGreen)
            }
        }
    }
}

@Composable
fun ProfileItem(title: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 8.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.LightGray, RoundedCornerShape(12.dp))
                .padding(12.dp, 8.dp),
        ) {
            Text(
                text = value,
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    Surface {
        ProfileScreen(null)
    }
}