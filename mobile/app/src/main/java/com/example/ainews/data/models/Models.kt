package com.example.ainews.data.models

data class Article(
    val title: String,
    val description: String?,
    val content: String?,
    val url: String,
    val image: String?,
    val source: Source?,
    val publishedAt: String
)

data class Source(
    val name: String
)

data class NewsResponse(
    val articles: List<Article>
)

data class AskRequest(
    val question: String,
    val article: Article,
    val history: List<ChatMessage>
)

data class ChatMessage(
    val type: String, // "user" or "ai"
    val text: String
)

data class AskResponse(
    val answer: String
)

data class SummarizeRequest(
    val article: Article
)

data class SummarizeResponse(
    val summary: String,
    val socials: List<String>
)
