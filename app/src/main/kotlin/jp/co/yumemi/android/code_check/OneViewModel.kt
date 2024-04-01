/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.dataClasses.Item
import jp.co.yumemi.android.code_check.dataSources.RepositorySearchDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * TwoFragment で使う
 */
@HiltViewModel
class OneViewModel @Inject constructor(
    private val repositorySearchDataSource: RepositorySearchDataSource
) : ViewModel() {

    /**
     * updateRepositoriesListFlow()を使って更新する。直接更新は避けること。
     */
    private val _repositoriesListFlow = MutableStateFlow<List<Item>>(listOf())

    /**
     * _repositoriesListFlowを更新する
     * - 更新内容はrepositoriesListFlowを通じてUIへ伝わる
     */
    private fun updateRepositoriesListFlow(repositories: List<Item>) {
        _repositoriesListFlow.update { repositories }
    }

    /**
     * UIからcollectする。
     * これをSingle source of Truthとする。
     */
    val repositoriesListFlow = _repositoriesListFlow.asStateFlow()

    /**
     * 検索を実行する。
     * 検索結果はViewModel内に保管しておき、アクティビティ再構築時に利用する。
     * - 検索結果の取得方法：repositoriesListFlowをcollectする
     */
    fun searchRepository(inputText: String) {

        viewModelScope.launch {
            val result = repositorySearchDataSource.searchRepository(inputText)
            updateRepositoriesListFlow(result)

            lastSearchDate = Date()

        }
    }

    /**
     * nameをキーに、リポジトリを探す。
     * 見つからない場合はnullを返す。
     * @param fullName リポジトリの名前
     */
    fun findRepository(fullName: String): Item? {
        val repositories = repositoriesListFlow.value
        return repositories.find {
            it.fullName == fullName
        }
    }
}