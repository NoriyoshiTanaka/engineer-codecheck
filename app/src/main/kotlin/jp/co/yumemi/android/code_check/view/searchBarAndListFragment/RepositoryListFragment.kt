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
import jp.co.yumemi.android.code_check.uitl.showSnackBar
import jp.co.yumemi.android.code_check.viewModel.RepositorySearchViewModel
import jp.co.yumemi.android.code_check.viewModel.RepositorySearchViewModel.Errors.Parse
import jp.co.yumemi.android.code_check.viewModel.RepositorySearchViewModel.Errors.TimeOut
import kotlinx.coroutines.launch

/**
 * リスト部分のフラグメント。
 * サーチバーのフラグメントとリストのフラグメントで検索画面を構成する。
 */
class RepositoryListFragment: Fragment(R.layout.fragment_list) {

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
                launch {
                    // リストの更新をcollectする
                    repositorySearchViewModel.repositoryListFlow.collect {
                        if (it != repositoryListAdapter.currentList) {
                            // 更新されました、を出してリストをアップデートする
                            showSnackBar(view, getString(R.string.list_updated))
                            repositoryListAdapter.submitList(it)
                        }
                    }
                }

                launch {
                    // エラーをcollectする
                    repositorySearchViewModel.errorFlow.collect{
                        val string = when (it){
                            TimeOut -> getString(R.string.timeout)
                            Parse -> getString(R.string.error)
                        }
                        showSnackBar(view, string)
                    }
                }
            }
        }
    }

    /**
     * @param name Jsonの"full_name"を渡す
     */
    private fun gotoRepositoryDetailFragment(name: String) {
        val action =
            SearchBarAndRepositoryListFragmentDirections.actionRepositoriesFragmentToRepositoryFragment(name = name)
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

        //beginCollectErrorFlow()
    }
}