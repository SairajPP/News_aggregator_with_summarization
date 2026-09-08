package com.example.ainews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ainews.data.models.Article
import com.example.ainews.ui.MainViewModel
import com.example.ainews.ui.screens.ArticleDetailScreen
import com.example.ainews.ui.screens.HomeScreen
import com.example.ainews.ui.theme.AiNewsAggregatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AiNewsAggregatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AiNewsApp()
                }
            }
        }
    }
}

@Composable
fun AiNewsApp() {
    val viewModel: MainViewModel = viewModel()
    var selectedArticle by remember { mutableStateOf<Article?>(null) }

    if (selectedArticle == null) {
        HomeScreen(
            viewModel = viewModel,
            onArticleClick = { selectedArticle = it }
        )
    } else {
        ArticleDetailScreen(
            article = selectedArticle!!,
            viewModel = viewModel,
            onBackClick = { selectedArticle = null }
        )
    }
}
