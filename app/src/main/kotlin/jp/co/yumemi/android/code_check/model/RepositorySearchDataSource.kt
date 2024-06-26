package jp.co.yumemi.android.code_check.model

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import jp.co.yumemi.android.code_check.model.dataClass.Item
import jp.co.yumemi.android.code_check.model.dataClass.SearchResult
import javax.inject.Inject

interface IRepositorySearchDataSource {
    /**
     * ktor + json serializationで抽出する。
     * 必要のないデータは読み捨てている
     * @param query 検索する文字列
     */
    suspend fun searchRepository(query: CharSequence): List<Item>?
}

class RepositorySearchDataSource @Inject constructor(
    private val client: HttpClient
) : IRepositorySearchDataSource {
    /**
     * ktor + json serializationで抽出する。
     * 必要のないデータは読み捨てている
     * @param query 検索する文字列
     */
    override suspend fun searchRepository(query: CharSequence): List<Item>? {
        val searchResult = client.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", query)
        }

        searchResult.runCatching {
            val result = searchResult.body<SearchResult>()
            return result.items
        }

        return null
    }
}