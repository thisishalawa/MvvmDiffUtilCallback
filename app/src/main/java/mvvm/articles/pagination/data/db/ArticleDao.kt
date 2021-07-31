package mvvm.articles.pagination.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import mvvm.articles.pagination.data.entity.Article

@Dao
interface ArticleDao {

    // DAO DataAccessObject
    // update or insert
    // return long : the id was inserted
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(article: Article): Long

    //won't be suspense coz it return liveData Object which does not work with suspend fun
    @Query("SELECT * FROM articles")
    fun getAllArticles(): LiveData<List<Article>>

    @Delete
    suspend fun deleteArticle(article: Article)

}