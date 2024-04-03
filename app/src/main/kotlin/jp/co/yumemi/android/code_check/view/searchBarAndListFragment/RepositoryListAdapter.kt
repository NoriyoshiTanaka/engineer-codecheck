package jp.co.yumemi.android.code_check.view.searchBarAndListFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import jp.co.yumemi.android.code_check.databinding.LayoutItemBinding
import jp.co.yumemi.android.code_check.model.dataClass.Item

/**
 * RepositoryListFragmentで使うアダプター
 */
class RepositoryListAdapter(
    private val itemClickListener: OnItemClickListener,
) : ListAdapter<Item, RepositoryListAdapter.ViewHolder>(diffUtilCallbackImpl) {

    private lateinit var binding: LayoutItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = LayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // DataBindingを使うので、データをbindingに渡すだけ
        val item = getItem(position)
        binding.fullName = item.fullName
        binding.onItemClickListener = itemClickListener
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun interface OnItemClickListener {
        fun itemClick(name: String)
    }

    companion object {
        val diffUtilCallbackImpl = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }
        }
    }
}

