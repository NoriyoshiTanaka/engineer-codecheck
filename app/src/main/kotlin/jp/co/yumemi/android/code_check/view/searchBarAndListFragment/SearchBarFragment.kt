package jp.co.yumemi.android.code_check.view.searchBarAndListFragment

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentSearchBarBinding
import jp.co.yumemi.android.code_check.uitl.showSnackBar
import jp.co.yumemi.android.code_check.viewModel.RepositorySearchViewModel

/**
 * サーチバー部分のフラグメント。
 * サーチバーのフラグメントとリストのフラグメントで検索画面を構成する。
 */
class SearchBarFragment: Fragment(R.layout.fragment_search_bar) {
    private val repositorySearchViewModel: RepositorySearchViewModel by activityViewModels<RepositorySearchViewModel>()

    private val connectivityManager: ConnectivityManager by lazy {
        requireContext().getSystemService(ConnectivityManager::class.java)
    }

    private val editorActionListener = TextView.OnEditorActionListener { v, actionId, _ ->
        val query = v?.text
        when {
            // サーチ以外ならスルー
            actionId != EditorInfo.IME_ACTION_SEARCH -> {
                false
            }

            // ネットワーク接続がないなら、SnackBarを出して戻る
            connectivityManager.activeNetwork == null -> {
                showSnackBar(v, getString(R.string.no_internet_connection))
                true
            }

            // 何も入力せずに検索しようとしたなら、SnackBarを出して戻る
            query.isNullOrEmpty() -> {
                showSnackBar(v, getString(R.string.input_something_please))
                true
            }

            // 全部のチェックがOKなら検索実行
            else -> {
                searchRepository(query)
                true
            }
        }
    }

    /**
     * 検索を実行する。検索結果はrepositoryListFlowをcollectして取得する
     */
    private fun searchRepository(query: CharSequence) {
        repositorySearchViewModel.searchRepository(query)
        showSnackBar(view, getString(R.string.go_search))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchBarBinding.bind(view)
        // EditTextにlistenerをセット
        binding.searchInputText.setOnEditorActionListener(editorActionListener)
    }
}