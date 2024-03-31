/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.content.Context
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.parcelize.Parcelize
import org.json.JSONObject
import java.util.*

/**
 * TwoFragment で使う
 */
class OneViewModel : ViewModel() {

    /**
     * updateRepositoriesListFlow()を使って更新する。直接更新は避けること。
     */
    private val _repositoriesListFlow = MutableStateFlow<List<item>>(listOf())

    /**
     * UIからcollectする
     */
    val repositoriesListFlow = _repositoriesListFlow.asStateFlow()

    /**
     * _repositoriesListFlowを更新する
     * - 更新内容はrepositoriesListFlowを通じてUIへ伝わる
     */
    private fun updateRepositoriesListFlow(repositories: List<item>) {
        _repositoriesListFlow.update { repositories }
    }

    /**
     * 検索を実行する。
     * 検索結果はViewModel内に保管しておき、アクティビティ再構築時に利用する。
     * - 検索結果の取得方法：repositoriesListFlowをcollectする
     */
    fun searchRepository(context: Context, inputText: String) {
        val client = HttpClient(Android)

        viewModelScope.launch {
            val response: HttpResponse = client?.get("https://api.github.com/search/repositories") {
                header("Accept", "application/vnd.github.v3+json")
                parameter("q", inputText)
            }

            val jsonBody = JSONObject(response.receive<String>())

            val jsonItems = jsonBody.optJSONArray("items")!!

            val items = mutableListOf<item>()

            /**
             * アイテムの個数分ループする
             */

            /**
             * アイテムの個数分ループする
             */
            for (i in 0 until jsonItems.length()) {
                val jsonItem = jsonItems.optJSONObject(i)!!
                val name = jsonItem.optString("full_name")
                val ownerIconUrl = jsonItem.optJSONObject("owner")!!.optString("avatar_url")
                val language = jsonItem.optString("language")
                val stargazersCount = jsonItem.optLong("stargazers_count")
                val watchersCount = jsonItem.optLong("watchers_count")
                val forksCount = jsonItem.optLong("forks_conut")
                val openIssuesCount = jsonItem.optLong("open_issues_count")

                items.add(
                    item(
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

            lastSearchDate = Date()

            updateRepositoriesListFlow(items)
        }
    }
}

@Parcelize
data class item(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable