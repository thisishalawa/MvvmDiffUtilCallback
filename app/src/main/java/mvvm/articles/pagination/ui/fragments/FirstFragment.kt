package mvvm.articles.pagination.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import mvvm.articles.pagination.MainActivity
import mvvm.articles.pagination.adapter.NewsAdapter
import mvvm.articles.pagination.databinding.FragmentFirstBinding
import mvvm.articles.pagination.ui.fragments.viewmodel.NewsViewModel
import mvvm.articles.pagination.utils.Constant
import mvvm.articles.pagination.utils.Resource

class FirstFragment : Fragment() {

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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        viewModel = (activity as MainActivity).viewModel
        setupRecyclerView()
        loadData()

    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.rvBreakingNews.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loadData() {
        viewModel.breakingNews.observe(viewLifecycleOwner, Observer { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.d(TAG, "loadData:Loading.. ")
                    showProgressBar()
                }
                is Resource.Error -> {
                    Log.d(TAG, "loadData:Error.. ")
                    hideProgressBar()
                    response.message?.let { message ->
                        Toast.makeText(activity, "an error occured : $message ", Toast.LENGTH_SHORT)
                            .show()

                    }
                }
                is Resource.Success -> {
                    Log.d(TAG, "loadData:Success.. ")
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles)
                    }
                }


            }

        })
    }

    companion object {
        private const val TAG = Constant.DEBUG_TAG
    }
}
