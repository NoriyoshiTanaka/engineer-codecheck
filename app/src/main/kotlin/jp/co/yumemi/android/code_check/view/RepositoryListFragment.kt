/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.viewModel.RepositorySearchViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoryListBinding
import jp.co.yumemi.android.code_check.databinding.LayoutItemBinding
import jp.co.yumemi.android.code_check.model.dataClass.Item
import kotlinx.coroutines.launch

/**
 * 初期したときに表示される画面
 */
class RepositoryListFragment: Fragment(R.layout.fragment_repository_list){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val _binding= FragmentRepositoryListBinding.bind(view)

        val _viewModel by activityViewModels<RepositorySearchViewModel>()

        val _layoutManager= LinearLayoutManager(context!!)
        val _dividerItemDecoration=
            DividerItemDecoration(context!!, _layoutManager.orientation)
        val _adapter = RepositoryListAdapter(object : RepositoryListAdapter.OnItemClickListener {
            override fun itemClick(name: String) {
                gotoRepositoryFragment(name)
            }
        })

        _binding.searchInputText
            .setOnEditorActionListener{ editText, action, _ ->
                if (action== EditorInfo.IME_ACTION_SEARCH){
                    editText.text.toString().let {
                        _viewModel.searchRepository(it)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        _binding.recyclerView.also {
            it.layoutManager = _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter = _adapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                _viewModel.repositoriesListFlow.collect {
                    _adapter.submitList(it)
                }
            }
        }
    }

    fun gotoRepositoryFragment(name: String)
    {
        val _action= RepositoryListFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(name = name)
        findNavController().navigate(_action)
    }
}

val diffUtilCallbackImpl= object: DiffUtil.ItemCallback<Item>(){
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

}

class RepositoryListAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<Item, RepositoryListAdapter.ViewHolder>(diffUtilCallbackImpl) {

    private lateinit var binding: LayoutItemBinding

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    interface OnItemClickListener{
    	fun itemClick(name: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
    	val _item= getItem(position)
        binding.fullName = _item.fullName
        binding.onItemClickListener = itemClickListener
    }
}
