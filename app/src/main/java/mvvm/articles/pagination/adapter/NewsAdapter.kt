package mvvm.articles.pagination.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import mvvm.articles.pagination.data.entity.Article
import mvvm.articles.pagination.databinding.ItemArticleBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root)


    /* DiffUtil !
    * Avoid Normal Way to load data and notify data set changes
    * RecyclerView Will always update its item Sol -> DiffUtil
    * Diff calculate the difference between two lists and only
    * update these item are difference -> AsyncListDiffer
    *
    *
    *
    * */
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url // url is unique
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    /*
    *
    * RecyclerView.Adapter
    *
    * */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding = ItemArticleBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]
        with(holder) {
            with(article) {
                binding.mainText.text = this.title
                binding.subText.text = this.content
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}