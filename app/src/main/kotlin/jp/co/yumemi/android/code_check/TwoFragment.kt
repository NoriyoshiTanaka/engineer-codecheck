/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import coil.load
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentTwoBinding

@AndroidEntryPoint
class TwoFragment : Fragment(R.layout.fragment_two) {

    private val viewModel by activityViewModels<OneViewModel>()

    private val args: TwoFragmentArgs by navArgs()

    private var binding: FragmentTwoBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        binding = FragmentTwoBinding.bind(view)

        val name = args.name
        val list = viewModel.repositoriesListFlow.value
        val item: Item? = list.find {
            it.name == name
        }

        _binding.ownerIconView.load(item?.ownerIconUrl);
        _binding.nameView.text = item?.name;
        _binding.languageView.text = item?.language;
        _binding.starsView.text = "${item?.stargazersCount} stars";
        _binding.watchersView.text = "${item?.watchersCount} watchers";
        _binding.forksView.text = "${item?.forksCount} forks";
        _binding.openIssuesView.text = "${item?.openIssuesCount} open issues";
    }
}
