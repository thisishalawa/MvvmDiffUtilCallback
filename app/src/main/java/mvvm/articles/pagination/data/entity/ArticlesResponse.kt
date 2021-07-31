package mvvm.articles.pagination.data.entity


data class ArticlesResponse(
    val articles: List<Article>,
    val status: String,
    val totalResults: Int
)



/*
* {
   "status":"ok",
   "totalResults":38,
   "articles":[
      {
         "source":{
            "id":"fox-news",
            "name":"Fox News"
         },
         "author":"Nate Day",
         "title":"Dolly Parton statue proposed on Tennessee Capitol grounds - Fox News",
         "description":"State Representative John Mark Windle has proposed building a statue of \"9 to 5\" singer Dolly Parton on the state's Capitol grounds.",
         "url":"https://www.foxnews.com/entertainment/dolly-parton-statue-proposed-tennessee-capitol-grounds-state-lawmaker",
         "urlToImage":"https://static.foxnews.com/foxnews.com/content/uploads/2021/01/Dolly-Parton.jpg",
         "publishedAt":"2021-01-14T01:50:41Z",
         "content":"A statue of Dolly Parton may be erected on the grounds of the Tennessee Capitol.\r\nState Rep. John Mark Windle proposed such a statue on the property to recognize all the country icon's contributions … [+3908 chars]"
      },
      {
         "source":{
            "id":"the-washington-post",
            "name":"The Washington Post"
         },
         "author":"Christian Davenport",
         "title":"Trump pushed for a moon landing in 2024. It’s not going to happen. - Washington Post",
         "description":"Biden will likely keep NASA’s Artemis program, but on a different timeline.",
         "url":"https://www.washingtonpost.com/technology/2021/01/13/trump-nasa-moon-2024/",
         "urlToImage":"https://www.washingtonpost.com/wp-apps/imrs.php?src=https://arc-anglerfish-washpost-prod-washpost.s3.amazonaws.com/public/KBKUMIB2NAI6XKWZRFMSE4UAYQ.jpg&w=1440",
         "publishedAt":"2021-01-13T17:57:00Z",
         "content":"Now, as the Trump administration departs in defeat, it is clear that the 2024 deadline will not be met, and was likely never an achievable goal, despite having the backing of the White House and a ma… [+6515 chars]"
      }
   ]
}*/