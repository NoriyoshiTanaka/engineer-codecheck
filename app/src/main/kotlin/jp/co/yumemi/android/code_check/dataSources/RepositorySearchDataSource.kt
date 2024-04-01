package jp.co.yumemi.android.code_check.dataSources

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import jp.co.yumemi.android.code_check.dataClasses.Item
import jp.co.yumemi.android.code_check.dataClasses.SearchResult
import kotlinx.serialization.json.Json
import javax.inject.Inject

class RepositorySearchDataSource @Inject constructor() {
    /**
     * ktor + json serializationで抽出する。
     * 必要のないデータは読み捨てている
     * @param query 検索する文字列
     */
    suspend fun searchRepository(query: String): List<Item> {
        val client = HttpClient(Android) {
            install(ContentNegotiation){
                json(Json{
                    this.ignoreUnknownKeys = true
                })
            }
        }

        val searchResult: SearchResult = client.get("https://api.github.com/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", query)
        }.body()

        return searchResult.items
    }
}