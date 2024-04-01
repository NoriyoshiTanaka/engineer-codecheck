/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.view

import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.R
import java.util.*

@AndroidEntryPoint
class TopActivity : AppCompatActivity(R.layout.activity_top) {

    companion object {
        lateinit var lastSearchDate: Date
    }
}
