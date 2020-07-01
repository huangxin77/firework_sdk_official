package com.loopnow.fireworkdemo.adapters

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

object ImageAdapter {
    @BindingAdapter("contentUrl")
    @JvmStatic
    fun setImage(view: ImageView, imageUrl: String) {

            Picasso.get().load(imageUrl).into(view)

    }
}