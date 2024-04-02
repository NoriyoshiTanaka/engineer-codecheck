/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view.repositoryListFragment

import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoryListBinding
import jp.co.yumemi.android.code_check.viewModel.RepositorySearchViewModel
import kotlinx.coroutines.launch

/**
 * アプリが起動したときに表示される画面。
 * 文字列入力のEditTextとRecyclerViewで構成される。
 */
class RepositoryListFragment : Fragment(R.layout.fragment_repository_list) {

    private val repositorySearchViewModel by activityViewModels<RepositorySearchViewModel>()

    private val connectivityManager: ConnectivityManager by lazy {
        requireContext().getSystemService(ConnectivityManager::class.java)
    }

    private val layoutManager: LinearLayoutManager
        get() = LinearLayoutManager(requireContext())

    private val dividerItemDecoration: DividerItemDecoration
        get() = DividerItemDecoration(requireContext(), layoutManager.orientation)

    private val repositoryListAdapter by lazy {
        RepositoryListAdapter(itemClickListener)
    }

    private val itemClickListener: RepositoryListAdapter.OnItemClickListener =
        RepositoryListAdapter.OnItemClickListener { name ->
            gotoRepositoryDetailFragment(name)
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
     * 注意喚起のSnackBarを表示する
     */
    private fun showSnackBar(v: View?, text: String) {
        if (v != null) {
            Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * 検索を実行する。検索結果はrepositoryListFlowをcollectして取得する
     */
    private fun searchRepository(query: CharSequence) {
        repositorySearchViewModel.searchRepository(query)
    }

    /**
     * collectした結果はアダプターに渡す(= リストを更新する)
     */
    private fun beginCollectRepositoryListFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                repositorySearchViewModel.repositoryListFlow.collect {
                    repositoryListAdapter.submitList(it)
                }
            }
        }
    }

    /**
     * @param name Jsonの"full_name"を渡す
     */
    private fun gotoRepositoryDetailFragment(name: String) {
        val action =
            RepositoryListFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(name = name)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRepositoryListBinding.bind(view)

        binding.searchInputText.setOnEditorActionListener(editorActionListener)

        // recyclerViewのセットアップ
        binding.recyclerView.also {
            it.layoutManager = layoutManager
            it.addItemDecoration(dividerItemDecoration)
            it.adapter = repositoryListAdapter
        }

        // 検索結果のcollectを始める
        beginCollectRepositoryListFlow()
    }
}

