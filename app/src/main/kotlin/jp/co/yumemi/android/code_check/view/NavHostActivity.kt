/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R

/**
 * このアプリの唯一のアクティビティ。
 * 全面にNavHostFragmentを貼り付けている。
 * Fragmentの差し替えはNavigationComponentを使っている。
 */
@AndroidEntryPoint
class NavHostActivity : AppCompatActivity(R.layout.activity_nav_host)
