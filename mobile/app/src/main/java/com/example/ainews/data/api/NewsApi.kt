package com.example.ainews.data.api

import com.example.ainews.data.models.AskRequest
import com.example.ainews.data.models.AskResponse
import com.example.ainews.data.models.NewsResponse
import com.example.ainews.data.models.SummarizeRequest
import com.example.ainews.data.models.SummarizeResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface NewsApi {
    @GET("api/news")
    suspend fun getNews(@Query("category") category: String = "general"): NewsResponse

    @POST("api/news/ask-article")
    suspend fun askArticleQuestion(@Body request: AskRequest): AskResponse

    @POST("api/news/summarize-single-article")
    suspend fun summarizeArticle(@Body request: SummarizeRequest): SummarizeResponse
}
