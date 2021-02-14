package com.inces.incesclient.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.inces.incesclient.R
import com.inces.incesclient.models.Image
import com.inces.incesclient.util.Constants
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.image_slider_layout.view.*
import kotlinx.android.synthetic.main.image_view.*

class ProductDetailImageAdapter(private val list: List<Image>) :
    SliderViewAdapter<ProductDetailImageAdapter.SliderViewHolder>() {
    class SliderViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView)

    private val images: List<Image> = this.list

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder =
        SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout, parent, false)
        )

    override fun getCount() = images.size

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        viewHolder.itemView.apply {
            if (!images[position].img.contains("https"))
                Glide.with(context)
                    .asBitmap()
                    .load(Constants.IMAGE_URL + images[position].img)
                    .into(sliderImage)
            else
                Glide.with(context)
                    .load(images[position].img)
                    .into(sliderImage)

            setOnClickListener {
                val url: MutableList<String> = ArrayList()
                images.forEach {
                    url.add(it.img)
                }
                showFullImage(context, url)
            }
        }
    }

    private fun showFullImage(context: Context, url: List<String>) {
        val builder = BottomSheetDialog(context)
        builder.setContentView(R.layout.image_view)
        builder.setCancelable(true)
        builder.show()
        builder.apply {
            imageSlider.setSliderAdapter(ImageSliderAdapter(url))
        }
    }
}

