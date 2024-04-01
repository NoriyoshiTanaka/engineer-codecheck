/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.viewModel.RepositorySearchViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoryDetailBinding
import jp.co.yumemi.android.code_check.view.NavHostActivity.Companion.lastSearchDate

/**
 * リポジトリの詳細を表示するフラグメント。
 * 現状の機能は表示のみ
 */
class RepositoryDetailFragment : Fragment(R.layout.fragment_repository_detail) {

    private val viewModel by activityViewModels<RepositorySearchViewModel>()

    private val args: RepositoryDetailFragmentArgs by navArgs()

    private var binding: FragmentRepositoryDetailBinding? = null
    private val _binding get() = binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("検索した日時", lastSearchDate.toString())

        // データの割り付けは dataBinding に委ねているので、ここではデータをbindingへ渡すだけで終了
        binding = FragmentRepositoryDetailBinding.bind(view)
        _binding.item = viewModel.findRepository(args.name)
    }
}
