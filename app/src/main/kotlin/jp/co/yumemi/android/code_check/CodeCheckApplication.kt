package jp.co.yumemi.android.code_check

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Hiltを使うためにはApplicationが必要
 */
@HiltAndroidApp
class CodeCheckApplication: Application()