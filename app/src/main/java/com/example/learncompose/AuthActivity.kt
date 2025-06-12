package com.example.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.* // Import all layout components
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.* // Import all runtime components
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learncompose.ui.theme.LearnComposeTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import android.content.Intent
import androidx.compose.ui.platform.LocalContext
import android.app.Application
import android.util.Log

class AuthActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                AuthScreen()
            }
        }
    }
}

@Composable
fun AuthScreen(authViewModel: AuthViewModel = viewModel()) {
    val email by authViewModel.account.collectAsState()
    val password by authViewModel.password.collectAsState()
    val verificationCode by authViewModel.verificationCode.collectAsState()
    val isLoading by authViewModel.isLoading.collectAsState()
    val message by authViewModel.message.collectAsState()
    val navigateToMain by authViewModel.navigateToMain.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(navigateToMain) {
        if (navigateToMain) {
            context.startActivity(Intent(context, HomeActivity::class.java))
            authViewModel.resetNavigation()
            (context as? ComponentActivity)?.finish()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { authViewModel.onAccountChange(it) },
            label = { Text("Account") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { authViewModel.onPasswordChange(it) },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = verificationCode,
            onValueChange = { authViewModel.onVerificationCodeChange(it) },
            label = { Text("Verification Code") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(CenterHorizontally))
        } else {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                Button(onClick = { authViewModel.sendVerificationCode() }, enabled = !isLoading) {
                    Text("Send Verification Code")
                }
                Button(onClick = { authViewModel.register() }, enabled = !isLoading) {
                    Text("Register")
                }
                Button(onClick = { authViewModel.login() }, enabled = !isLoading) {
                    Text("Login")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = message)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAuthScreen() {
    LearnComposeTheme {
        AuthScreen()
    }
} 