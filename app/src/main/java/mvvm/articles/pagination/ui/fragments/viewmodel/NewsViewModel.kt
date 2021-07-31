package mvvm.articles.pagination.ui.fragments.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import mvvm.articles.pagination.data.entity.ArticlesResponse
import mvvm.articles.pagination.repository.NewsRepository
import mvvm.articles.pagination.utils.Resource
import retrofit2.Response
import java.io.IOException

class NewsViewModel(
    val newsRepository: NewsRepository
) : ViewModel() {

    // Fetching breaking News .. firstFragment
    val breakingNews: MutableLiveData<Resource<ArticlesResponse>> = MutableLiveData()
    var breakingNewsPage = 1


    init {
        getBreakingNews("us")
    }


    /*
    *
    * Fetching breaking News .. firstFragment
    * */
    fun getBreakingNews(countryCode: String) {
        viewModelScope.launch {
            safeBreakingNewsCall(countryCode)
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNews.postValue(Resource.Loading())
        try {
            val response = newsRepository.getBreakingNews(countryCode, breakingNewsPage)
            breakingNews.postValue(handleBreakingNewsResponse(response))

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNews.postValue(Resource.Error("Network Failure"))
                else -> breakingNews.postValue(Resource.Error("Conversion Error"))
            }

        }
    }

    private fun handleBreakingNewsResponse(response: Response<ArticlesResponse>): Resource<ArticlesResponse> {
        if (response.isSuccessful) {
            // notNull?
            response.body()?.let { resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }
}