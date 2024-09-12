package com.example.navegacao1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.navegacao1.ui.telas.TelaPrincipal
import com.example.navegacao1.ui.theme.Navegacao1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Navegacao1Theme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "principal") {
                    composable("principal") {
                        TelaPrincipal(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    Navegacao1Theme {
        TelaPrincipal(modifier = Modifier.fillMaxSize())
    }
}