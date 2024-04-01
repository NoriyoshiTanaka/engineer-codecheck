package jp.co.yumemi.android.code_check.dataClasses


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    @SerialName("incomplete_results")
    val incompleteResults: Boolean,
    @SerialName("items")
    val items: List<Item>,
    @SerialName("total_count")
    val totalCount: Int
)