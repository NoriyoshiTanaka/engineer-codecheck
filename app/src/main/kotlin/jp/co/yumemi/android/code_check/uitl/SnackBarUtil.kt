package jp.co.yumemi.android.code_check.uitl

import android.view.View
import com.google.android.material.snackbar.Snackbar

/**
 * 注意喚起のSnackBarを出す
 */
fun showSnackBar(v: View?, text: String) {
    if (v != null) {
        Snackbar.make(v, text, Snackbar.LENGTH_SHORT).show()
    }
}