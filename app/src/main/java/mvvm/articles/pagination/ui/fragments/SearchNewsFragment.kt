package mvvm.articles.pagination.ui.fragments

import android.R
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mvvm.articles.pagination.MainActivity
import mvvm.articles.pagination.adapters.NewsAdapter
import mvvm.articles.pagination.databinding.FragmentSearchNewsBinding
import mvvm.articles.pagination.ui.viewmodel.NewsViewModel
import mvvm.articles.pagination.utils.Constant
import mvvm.articles.pagination.utils.Constant.Companion.SEARCH_NEWS_TIME_DELAY
import mvvm.articles.pagination.utils.Resource


class SearchNewsFragment : Fragment() {

    private var _binding: FragmentSearchNewsBinding? = null
    private val binding get() = _binding!!

    // viewModel
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        setAutoComplete()
        setupRecyclerView()
        delayBeforeSearching()
        loadData()


    }


    private fun setAutoComplete() {
        val language = arrayOf("Egypt", "Usa")
        val adapter: ArrayAdapter<String>? =
            context?.let { ArrayAdapter<String>(it, R.layout.select_dialog_item, language) }
        binding.autoCompleteEditText.apply {
            threshold = 1 // start working from first character
            setAdapter(adapter)
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun delayBeforeSearching() {
        var job: Job? = null
        binding.autoCompleteEditText.addTextChangedListener { editable ->
            job?.cancel()//we did not type anything !null
            job = MainScope().launch {
                delay(SEARCH_NEWS_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }
    }

    private fun loadData() {
        viewModel.searchNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.d(TAG, "onViewCreated: an error occurred : $message ")
                        Toast.makeText(
                            activity,
                            "an error occurred : $message ",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    companion object {
        private const val TAG = Constant.DEBUG_TAG
    }
}
