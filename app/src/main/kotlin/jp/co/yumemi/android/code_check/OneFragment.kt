/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.databinding.FragmentOneBinding
import jp.co.yumemi.android.code_check.databinding.LayoutItemBinding

@AndroidEntryPoint
class OneFragment: Fragment(R.layout.fragment_one){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val _binding= FragmentOneBinding.bind(view)

        val _viewModel by activityViewModels<OneViewModel>()

        val _layoutManager= LinearLayoutManager(context!!)
        val _dividerItemDecoration=
            DividerItemDecoration(context!!, _layoutManager.orientation)
        val _adapter= CustomAdapter(object : CustomAdapter.OnItemClickListener{
            override fun itemClick(name: String){
                gotoRepositoryFragment(name)
            }
        })

        _binding.searchInputText
            .setOnEditorActionListener{ editText, action, _ ->
                if (action== EditorInfo.IME_ACTION_SEARCH){
                    editText.text.toString().let {
                        _viewModel.searchRepository(requireContext(), it)
                    }
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }

        _binding.recyclerView.also{
            it.layoutManager= _layoutManager
            it.addItemDecoration(_dividerItemDecoration)
            it.adapter= _adapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            _viewModel.repositoriesListFlow.collect(){
                _adapter.submitList(it)
            }
        }
    }

    fun gotoRepositoryFragment(name: String)
    {
        val _action= OneFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(name = name)
        findNavController().navigate(_action)
    }
}

val diff_util= object: DiffUtil.ItemCallback<Item>(){
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean
    {
        return oldItem.name== newItem.name
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean
    {
        return oldItem== newItem
    }

}

class CustomAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<Item, CustomAdapter.ViewHolder>(diff_util){

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
        binding.name = _item.name
        binding.onItemClickListener = itemClickListener
    }
}
