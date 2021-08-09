package mvvm.articles.pagination.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable


@Entity(tableName = "articles")
data class Article(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    /* stuff to display */
    val title: String?,
    val content: String?,
    val url: String?,
    val source: Source?,
) : Serializable
