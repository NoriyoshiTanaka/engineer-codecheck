package jp.co.yumemi.android.code_check.view.searchBarAndListFragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentListBinding
import jp.co.yumemi.android.code_check.viewModel.RepositorySearchViewModel
import kotlinx.coroutines.launch

/**
 * リスト部分のフラグメント。
 * サーチバーのフラグメントとリストのフラグメントで検索画面を構成する。
 */
class ListFragment: Fragment(R.layout.fragment_list) {

    private val repositorySearchViewModel by activityViewModels<RepositorySearchViewModel>()

    private val repositoryListAdapter by lazy {
        RepositoryListAdapter(itemClickListener)
    }

    private val layoutManager: LinearLayoutManager
        get() = LinearLayoutManager(requireContext())

    private val dividerItemDecoration: DividerItemDecoration
        get() = DividerItemDecoration(requireContext(), layoutManager.orientation)

    private val itemClickListener: RepositoryListAdapter.OnItemClickListener =
        RepositoryListAdapter.OnItemClickListener { name ->
            gotoRepositoryDetailFragment(name)
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
            SearchBarAndListFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(name = name)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentListBinding.bind(view)

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