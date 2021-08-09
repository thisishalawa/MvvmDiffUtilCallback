package mvvm.articles.pagination.ui.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_ETHERNET
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import android.provider.ContactsContract.CommonDataKinds.Email.TYPE_MOBILE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mvvm.articles.pagination.NewsApplication
import mvvm.articles.pagination.data.entity.ArticlesResponse
import mvvm.articles.pagination.repository.NewsRepository
import mvvm.articles.pagination.utils.Resource
import retrofit2.Response
import java.io.IOException


/* architecture Skeleton
*  viewModel
*  separate activity date from ui
*
*  Need Context For connectivity!
*  activity context  bad!bad!
*  destroy when activity destroy
*
*  application context lives as long application does!
*  so when will not use inherit : ViewModel()
*  -> AndroidViewModel Same! As ViewModel() but we can use context
* */

class NewsViewModel(
    app: Application,
    private val newsRepository: NewsRepository
) : AndroidViewModel(app) {

    /*
    * LifeData object
    * Fetching breaking News .. firstFragment
    * */
    val breakingNews: MutableLiveData<Resource<ArticlesResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    // Pagination
    var breakingNewsResponse: ArticlesResponse? = null


    /*
    * Search for news
    * */
    val searchNews: MutableLiveData<Resource<ArticlesResponse>> = MutableLiveData()
    var searchNewsPage = 1// manage paging
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null


    init {
        getBreakingNews("us")
    }


    /*
    *
    * Fetching breaking News .. firstFragment
    * Start a Coroutine
    *
    * */
    fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            safeBreakingNewsCall(countryCode)
        }
    }

    fun searchNews(searchQuery: String) /*=*/ {
        viewModelScope.launch {
            safeSearchNewsCall(searchQuery)
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        try {
            breakingNews.postValue(Resource.Loading())
            // make a response
            if (hasInternetConnection()) {
                val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
                breakingNews.postValue(handleBreakingNewsResponse(response))
            } else breakingNews.postValue(Resource.Error("No Internet Connection"))


        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        try {
            newSearchQuery = searchQuery
            searchNews.postValue(Resource.Loading())
            // response
            if (hasInternetConnection()) {
                val response = newsRepository.searchNews(searchQuery, searchNewsPage)
                searchNews.postValue(handleSearchNewsResponse(response))
            } else searchNews.postValue(Resource.Error("No Internet Connection"))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Network Failure"))
                else -> searchNews.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    /*
    *
    *
    *
    * */
    private fun handleBreakingNewsResponse(response: Response<ArticlesResponse>): Resource<ArticlesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                /*Pagination
                * Load The next Page
                * Increase page number everyTime we get a response
                *
                *
                *
                * */
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles

                    oldArticles?.addAll(newArticles)

                }
                /*  return Resource.Success(resultResponse)
                Pagination
              return Resource.Success(breakingNewsResponse ?: resultResponse)
             */
                return Resource.Success(breakingNewsResponse ?: resultResponse)

            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchNewsResponse(response: Response<ArticlesResponse>): Resource<ArticlesResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }


    /*
    *
    * connectivity
    * */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<NewsApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}