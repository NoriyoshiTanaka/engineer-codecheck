package jp.co.yumemi.android.code_check.dataClasses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("forks_count")//
    val forksCount: Int,
    @SerialName("full_name")//
    val fullName: String,
    @SerialName("language")//
    val language: String?,
    @SerialName("open_issues_count")//
    val openIssuesCount: Int,
    @SerialName("owner")//
    val owner: Owner,
    @SerialName("stargazers_count")//
    val stargazersCount: Int,
    @SerialName("watchers_count")//
    val watchersCount: Int,
    /*
                val name = jsonItem.optString("full_name")
                val ownerIconUrl = jsonItem.optJSONObject("owner")!!.optString("avatar_url")
                val language = jsonItem.optString("language")
                val stargazersCount = jsonItem.optLong("stargazers_count")
                val watchersCount = jsonItem.optLong("watchers_count")
                val forksCount = jsonItem.optLong("forks_conut")
                val openIssuesCount = jsonItem.optLong("open_issues_count")

                items.add(
                    Item(
                        name = name,
                        ownerIconUrl = ownerIconUrl,
                        language = context.getString(R.string.written_language, language),
                        stargazersCount = stargazersCount,
                        watchersCount = watchersCount,
                        forksCount = forksCount,
                        openIssuesCount = openIssuesCount
                    )
                )
            }

     */
)