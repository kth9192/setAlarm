package com.noname.setalarm;

import androidx.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by kth919 on 2017-06-06.
 */

public class ImageBinder {

    @BindingAdapter("imageUrl")
    public static void loadImage(ImageView imageView, String resID){
        Glide.with(imageView.getContext()).load(resID).into(imageView);
    }

    @BindingAdapter("imageInt")
    public static void loadImage(ImageView imageView, int resID){
        RequestOptions cropOptions = new RequestOptions().circleCrop();

        Glide.with(imageView.getContext()).load(resID)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .into(imageView);

    }

}
