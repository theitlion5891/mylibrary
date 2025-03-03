package com.fantafeat.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fantafeat.R;
import com.fantafeat.Session.BaseFragment;



import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FullImageFragment extends BaseFragment {


    ImageView back_btn,full_img;
    String image;

    public FullImageFragment(String image){
        this.image = image;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_image, container, false);
        initFragment(view);
        return view;
    }

    @Override
    public void initControl(View view) {
        back_btn = view.findViewById(R.id.back_btn);
        full_img = view.findViewById(R.id.full_img);


        if(image != null &&
                !image.equals("") &&
                !image.equals("null")) {
            Glide.with(mContext)
                    .load(image)
                    .apply(
                            new RequestOptions()
                                    .error(R.drawable.user_image)
                                    .centerInside()
                    )
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            //on load failed
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            //on load success
                           /* attacher = new PhotoViewAttacher(full_img);
                            attacher.update();*/
                            return false;
                        }
                    })
                    .transition(withCrossFade())
                    .into(full_img);
        }

    }

    @Override
    public void initClick() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemoveFragment();
            }
        });
    }
}