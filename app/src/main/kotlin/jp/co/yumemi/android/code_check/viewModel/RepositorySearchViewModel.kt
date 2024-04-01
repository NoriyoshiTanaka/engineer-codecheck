/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.model.RepositorySearchDataSource
import jp.co.yumemi.android.code_check.model.dataClass.Item
import jp.co.yumemi.android.code_check.view.NavHostActivity.Companion.lastSearchDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

/**
 * github.comへの接続と、検索結果の広報を担う
 */
@HiltViewModel
class RepositorySearchViewModel @Inject constructor(
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
     */
    val repositoriesListFlow = _repositoriesListFlow.asStateFlow()

    /**
     * github.comへ接続して検索を実行する。
     * 検索結果はViewModel内に保管しておき、アクティビティ再構築時に利用する。
     * 検索結果はrepositoriesListFlowをcollectして取得する。
     */
    fun searchRepository(inputText: String) {

        viewModelScope.launch {
            val result = repositorySearchDataSource.searchRepository(inputText)
            updateRepositoriesListFlow(result)

            lastSearchDate = Date()

        }
    }

    /**
     * ViewModel内に保管してあるデータから、nameをキーに、リポジトリを探す。
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