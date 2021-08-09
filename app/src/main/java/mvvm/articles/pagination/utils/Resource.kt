package mvvm.articles.pagination.utils


/*
* generic class <T> to handle loading state
* generic type (Network response)
*
* */
sealed class Resource<T>(
    /* sealed class just abstract but we can define which class allowed to
    *   inherit -
    * Success & Error & Loading
    * */
    val data: T? = null,/*constructor take data as first parameter & message*/
    val message: String? = null
) {
    class Success<T>/*constructor*/(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}