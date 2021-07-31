package mvvm.articles.pagination.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mvvm.articles.pagination.data.entity.Article


// Room only handle primitive dataType
@Database(entities = [Article::class], version = 1)
@TypeConverters(Converters::class)


abstract class ArticleDatabase : RoomDatabase() {

    abstract fun getArticleDao(): ArticleDao

    companion object {

        @Volatile
        /*  Volatile ->
        *  other thread can immediately see when thread change that instance
        *
        * */
        private var instance: ArticleDatabase? = null

        /* sync -> make sure that only single instance at once
        *  operator fun
        * */
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        // create DB
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ArticleDatabase::class.java,
                "mvvm_article_db.db"
            ).build()
    }
}