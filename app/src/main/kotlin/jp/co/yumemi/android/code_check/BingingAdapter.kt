package jp.co.yumemi.android.code_check

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

object BingingAdapter {
    @BindingAdapter("loadImage")
    @JvmStatic
    fun loadImage(
        imageView: ImageView,
        imageUrl: String?
    ){
        imageView.load(imageUrl)
    }
}