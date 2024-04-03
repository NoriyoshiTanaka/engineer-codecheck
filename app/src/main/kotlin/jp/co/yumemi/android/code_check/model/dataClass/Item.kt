package jp.co.yumemi.android.code_check.model.dataClass


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Item(
    @SerialName("forks_count")
    val forksCount: Int,
    @SerialName("full_name")
    val fullName: String,
    @SerialName("language")
    val language: String?,
    @SerialName("open_issues_count")
    val openIssuesCount: Int,
    @SerialName("owner")
    val owner: Owner,
    @SerialName("stargazers_count")
    val stargazersCount: Int,
    @SerialName("watchers_count")
    val watchersCount: Int,
)