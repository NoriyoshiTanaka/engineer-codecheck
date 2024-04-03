package jp.co.yumemi.android.code_check.view.searchBarAndListFragment

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentSearchBarBinding
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
            actionId != EditorInfo.IME_ACTION_SEARCH -> {
                false
            }

            connectivityManager.activeNetwork == null -> {
                showSnackBar(v, "ネットに接続していません")
                true
            }

            query.isNullOrEmpty() -> {
                showSnackBar(v, "何か入力してください")
                true
            }

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
    }

    /**
     * 注意喚起のSnackBarを表示する
     */
    private fun showSnackBar(v: View?, text: String) {
        if (v != null) {
            Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentSearchBarBinding.bind(view)
        // EditTextにlistenerをセット
        binding.searchInputText.setOnEditorActionListener(editorActionListener)
    }

}