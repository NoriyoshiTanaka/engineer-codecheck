package jp.co.yumemi.android.code_check.view

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

/**
 * イメージをDataBindingで表示するためのアダプター。
 * 現状では、アバター表示のみで使っている。
 */
object ImageBingingAdapter {
    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(
        imageView: ImageView,
        imageUrl: String?
    ){
        imageView.load(imageUrl)
    }
}