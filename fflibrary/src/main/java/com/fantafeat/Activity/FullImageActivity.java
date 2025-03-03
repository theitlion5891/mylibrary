package com.fantafeat.Activity;

import androidx.annotation.Nullable;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.ApiManager;
import com.fantafeat.util.ConstantUtil;
import com.fantafeat.util.CustomUtil;
import com.fantafeat.util.LogUtil;


import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class FullImageActivity extends BaseActivity {

    ImageView back_btn, full_img;
    //PhotoViewAttacher attacher;
    String image, imageBase, imageName;
    boolean isProfile = false;

    @Override
    public void initClick() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_full_image);
        Intent intent = getIntent();
        LogUtil.e("FullImageActivity", "Intent received");

        if (intent.hasExtra(ConstantUtil.FULL_IMAGE_PATH)) {
            image = intent.getStringExtra(ConstantUtil.FULL_IMAGE_PATH);
            LogUtil.e("FullImageActivity", "ConstantUtil.FULL_IMAGE_PATH: " + image);
        } else {
            imageBase = intent.getStringExtra("imageBase");
            imageName = intent.getStringExtra("imageName");
            LogUtil.e("FullImageActivity", "imageBase: " + imageBase);
            LogUtil.e("FullImageActivity", "imageName: " + imageName);

            if (imageBase != null && imageName != null) {
                image = imageBase + imageName;
            }
        }
        if (getIntent().hasExtra("pageFrom")) {
            if (getIntent().getStringExtra("pageFrom").equals("profile")) {
                isProfile = true;
            }
        }

        initData();
        initClic();
    }

    private void initClic() {
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void initData() {
        back_btn = findViewById(R.id.back_btn);
        full_img = findViewById(R.id.full_img);

        if (isProfile) {
            int age = 18;
            if (!TextUtils.isEmpty(preferences.getUserModel().getDob()) && !preferences.getUserModel().getDob().equals("0000-00-00")) {
                age = CustomUtil.getAge(preferences.getUserModel().getDob());
            }
            LogUtil.d("agereap", age + " ");
            if (TextUtils.isEmpty(preferences.getUserModel().getUserImg())) {
                if (preferences.getUserModel().getGender().equals("Male")) {
                    if (age >= 18 && age <= 25) {
                        full_img.setImageResource(R.drawable.male_18_below);
                    } else if (age > 25 && age <= 40) {
                        full_img.setImageResource(R.drawable.male_25_above);
                    } else {
                        full_img.setImageResource(R.drawable.male_40_above);
                    }
                } else {
                    if (age >= 18 && age <= 25) {
                        full_img.setImageResource(R.drawable.female_18_below);
                    } else if (age > 25 && age <= 40) {
                        full_img.setImageResource(R.drawable.female_25_above);
                    } else {
                        full_img.setImageResource(R.drawable.female_40_above);
                    }
                }
            } else {
                LogUtil.e(TAG, "onCreate: " + ApiManager.PROFILE_IMG + preferences.getUserModel().getUserImg());
                if (preferences.getUserModel().getGender().equals("Male")) {
                    if (age >= 18 && age <= 25) {
                        Glide.with(mContext)
                                .load(image)
                                .error(R.drawable.male_18_below)
                                .placeholder(R.drawable.male_18_below)
                                .into(full_img);
                    } else if (age > 25 && age <= 40) {
                        Glide.with(mContext)
                                .load(image)
                                .error(R.drawable.male_25_above)
                                .placeholder(R.drawable.male_25_above)
                                .into(full_img);
                    } else {
                        Glide.with(mContext)
                                .load(image)
                                .error(R.drawable.male_40_above)
                                .placeholder(R.drawable.male_40_above)
                                .into(full_img);
                    }
                } else {
                    if (age >= 18 && age <= 25) {
                        Glide.with(mContext)
                                .load(image)
                                .error(R.drawable.female_18_below)
                                .placeholder(R.drawable.female_18_below)
                                .into(full_img);
                    } else if (age > 25 && age <= 40) {
                        Glide.with(mContext)
                                .load(image)
                                .error(R.drawable.female_25_above)
                                .placeholder(R.drawable.female_25_above)
                                .into(full_img);
                    } else {
                        Glide.with(mContext)
                                .load(image)
                                .error(R.drawable.female_40_above)
                                .placeholder(R.drawable.female_40_above)
                                .into(full_img);
                    }
                }
            }
        } else {
            if (image != null &&
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
                                /*attacher = new PhotoViewAttacher(full_img);
                                attacher.update();*/
                                return false;
                            }
                        })
                        .transition(withCrossFade())
                        .into(full_img);
            }
        }


    }
}