package mvvm.articles.pagination.data.db

import androidx.room.TypeConverter
import mvvm.articles.pagination.data.entity.Source

class Converters {

    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }

    /* convert from Source Custom class to string
    *  to ->
    *  wherever we have string convert to source
    *
    * */
}