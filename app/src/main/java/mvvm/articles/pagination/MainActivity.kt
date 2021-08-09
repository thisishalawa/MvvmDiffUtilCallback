package mvvm.articles.pagination

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import mvvm.articles.pagination.data.db.ArticleDatabase
import mvvm.articles.pagination.databinding.ActivityMainBinding
import mvvm.articles.pagination.repository.NewsRepository
import mvvm.articles.pagination.ui.viewmodel.NewsViewModel
import mvvm.articles.pagination.ui.viewmodel.NewsViewModelProviderFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // viewModel
    lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // viewModel
        val newsRepository = NewsRepository(ArticleDatabase(this))
        val viewModelProviderFactory =
            NewsViewModelProviderFactory(application, newsRepository)

        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(
            NewsViewModel::class.java
        )


    }
}