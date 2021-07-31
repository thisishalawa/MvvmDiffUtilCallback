package mvvm.articles.pagination.repository

import android.util.Log
import mvvm.articles.pagination.data.db.ArticleDatabase
import mvvm.articles.pagination.data.entity.ArticlesResponse
import mvvm.articles.pagination.network.RetrofitInstance
import retrofit2.Response

class NewsRepository(
    val db: ArticleDatabase
) {

    /* get data from db and remote data source api
    *
    * */

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int): Response<ArticlesResponse> {
        return RetrofitInstance.api.getBreakingNews(countryCode, pageNumber)
    }



}