package mvvm.articles.pagination.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import mvvm.articles.pagination.MainActivity
import mvvm.articles.pagination.R
import mvvm.articles.pagination.adapters.NewsAdapter
import mvvm.articles.pagination.databinding.FragmentFirstBinding
import mvvm.articles.pagination.ui.viewmodel.NewsViewModel
import mvvm.articles.pagination.utils.Constant
import mvvm.articles.pagination.utils.Constant.Companion.QUERY_PAGE_SIZE
import mvvm.articles.pagination.utils.Resource

class FirstFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!

    // viewModel
    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onClick(p0: View?) {
        if (p0 != null)
            if (p0.id == R.id.search_fab) {
                findNavController().navigate(
                    R.id.action_to_searchNewsFragment, null
                )
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        // click
        binding.searchFab.setOnClickListener(this)
        // recycler
        setupRecyclerView()
        loadData()

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
            // pagination
            addOnScrollListener(this@FirstFragment.scrollListener)

        }

        // other!
        onItemClicked()
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
        isLoading = true
    }

    private fun loadData() {
        viewModel.breakingNews.observe(viewLifecycleOwner, { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "loadData:Loading.. ")
                    showProgressBar()
                }
                is Resource.Error -> {
                    Log.d(TAG, "loadData:Error.. ")
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(
                            activity,
                            "an error occurred : $message ",
                            Toast.LENGTH_SHORT
                        ).show()

                    }
                }
                is Resource.Success -> {
                    Log.d(TAG, "loadData:Success.. ")
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        /*
                        * newsAdapter.differ.submitList(newsResponse.articles)
                        *
                        * */
                        newsAdapter.differ.submitList(newsResponse.articles.toList())

                        /*
                        *
                        * Pagination check if last page
                        * could be is Last Page
                        *
                        * */

                        val totalPages = newsResponse.totalResults / QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.breakingNewsPage == totalPages

                        if (isLastPage)
                            binding.rvBreakingNews.setPadding(0, 0, 0, 0)

                    }
                }


            }
        })
    }

    private fun onItemClicked() {
        newsAdapter.setOnItemClickListener {
            Toast.makeText(activity, "Source : ${it.source?.name}", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val TAG = Constant.DEBUG_TAG
    }

    /*
    *
    * Pagination
    * */

    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)

            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount


            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.getBreakingNews("us")
                isScrolling = false
            }


        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)

            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }
}
