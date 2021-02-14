package com.inces.incesclient.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.inces.incesclient.R
import com.inces.incesclient.models.Image
import com.inces.incesclient.util.Constants
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.image_slider_layout.view.*


class ImageSliderAdapter(private val images: List<String>) :
    SliderViewAdapter<ImageSliderAdapter.SliderViewHolder>() {
    class SliderViewHolder(itemView: View) : SliderViewAdapter.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder = SliderViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.image_slider_layout, parent, false)
    )

    override fun getCount() = images.size

    override fun onBindViewHolder(viewHolder: SliderViewHolder, position: Int) {
        viewHolder.itemView.apply {
            if (!images[position].contains("https"))
                Glide.with(context)
                    .load(Constants.IMAGE_URL + images[position])
                    .into(sliderImage)
            else
                Glide.with(context)
                    .load(images[position])
                    .into(sliderImage)

        }
    }


}