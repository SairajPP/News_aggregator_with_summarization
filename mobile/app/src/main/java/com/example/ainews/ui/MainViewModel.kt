package com.example.ainews.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ainews.data.api.NetworkModule
import com.example.ainews.data.models.Article
import com.example.ainews.data.models.AskRequest
import com.example.ainews.data.models.ChatMessage
import com.example.ainews.data.models.SummarizeRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val api = NetworkModule.api

    private val _news = MutableStateFlow<List<Article>>(emptyList())
    val news: StateFlow<List<Article>> = _news

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading
    
    private val _selectedCategory = MutableStateFlow("general")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _currentSummary = MutableStateFlow<String?>(null)
    val currentSummary: StateFlow<String?> = _currentSummary
    
    private val _chatHistory = MutableStateFlow<List<ChatMessage>>(emptyList())
    val chatHistory: StateFlow<List<ChatMessage>> = _chatHistory

    init {
        fetchNews("general")
    }

    fun fetchNews(category: String) {
        _selectedCategory.value = category
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = api.getNews(category)
                _news.value = response.articles
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun summarizeArticle(article: Article) {
        _currentSummary.value = "Generating AI Summary..."
        viewModelScope.launch {
            try {
                val response = api.summarizeArticle(SummarizeRequest(article))
                _currentSummary.value = response.summary
            } catch (e: Exception) {
                _currentSummary.value = "Failed to generate summary."
                e.printStackTrace()
            }
        }
    }

    fun askQuestion(question: String, article: Article) {
        val currentHistory = _chatHistory.value.toMutableList()
        currentHistory.add(ChatMessage("user", question))
        currentHistory.add(ChatMessage("ai", "Thinking..."))
        _chatHistory.value = currentHistory

        viewModelScope.launch {
            try {
                val response = api.askArticleQuestion(
                    AskRequest(
                        question = question,
                        article = article,
                        history = currentHistory.dropLast(1) // exclude "Thinking..."
                    )
                )
                val updatedHistory = currentHistory.dropLast(1).toMutableList()
                updatedHistory.add(ChatMessage("ai", response.answer))
                _chatHistory.value = updatedHistory
            } catch (e: Exception) {
                val updatedHistory = currentHistory.dropLast(1).toMutableList()
                updatedHistory.add(ChatMessage("ai", "Sorry, an error occurred."))
                _chatHistory.value = updatedHistory
                e.printStackTrace()
            }
        }
    }
    
    fun clearChat() {
        _chatHistory.value = emptyList()
        _currentSummary.value = null
    }
}
