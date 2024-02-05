package com.kceylan.app.view.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.kceylan.app.R;
import com.kceylan.app.databinding.ItemContainerSliderImageBinding;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    private final String[]  sliderImages;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(String[] sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderAdapter.ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSliderImageBinding sliderImageBinding = DataBindingUtil.inflate(
                layoutInflater, R.layout.item_container_slider_image,parent,false
        );
        return new ImageSliderViewHolder(sliderImageBinding);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageSliderAdapter.ImageSliderViewHolder holder, int position) {
        holder.bindSliderImage(sliderImages[position]);
    }

    @Override
    public int getItemCount() {
        return sliderImages.length;
    }

    public static class ImageSliderViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerSliderImageBinding itemContainerSliderImageBinding;

        public ImageSliderViewHolder(@NonNull ItemContainerSliderImageBinding itemContainerSliderImageBinding) {
            super(itemContainerSliderImageBinding.getRoot());
            this.itemContainerSliderImageBinding = itemContainerSliderImageBinding;

        }
        public void bindSliderImage(String imageURL) {
            itemContainerSliderImageBinding.setImageURL(imageURL);
        }
    }
}
