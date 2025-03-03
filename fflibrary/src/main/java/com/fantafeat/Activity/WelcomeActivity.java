package com.fantafeat.Activity;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fantafeat.R;
import com.fantafeat.Session.BaseActivity;
import com.fantafeat.util.PrefConstant;

public class WelcomeActivity extends BaseActivity {

    private ViewPager pager;
    private int[] images;
    private String[] title;
    private String[] subTitle;
    private MyViewPagerAdapter myViewPagerAdapter;
    private TextView txtTitle,txtSkip;
    //private Button btnSkip,btnNext;
    private Button btnLogin;
    private ImageView imgLogo;
    private LinearLayout dotsLayout;
    private boolean isSlide=false;
    private TextView[] dots;

    @Override
    public void initClick() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);

        initData();

    }

    private void initData() {
        pager=findViewById(R.id.pager);
        imgLogo=findViewById(R.id.imgLogo);
        txtTitle=findViewById(R.id.txtTitle);
        txtSkip=findViewById(R.id.txtSkip);
        //txtSubTitle=findViewById(R.id.txtSubTitle);
        // btnNext=findViewById(R.id.btnNext);
        //btnSkip=findViewById(R.id.btnSkip);
        btnLogin=findViewById(R.id.btnLogin);
        dotsLayout = findViewById(R.id.layoutDots);

        images=new int[]{
                R.drawable.slider1,
                R.drawable.slider2,
                R.drawable.slider3,
                R.drawable.slider4
        };

        title=new String[]{
                "Sports",
                "League",
                "Team",
                "Win more"
        };

        subTitle=new String[]{
                "Choose your favorite Sport",
                "Choose your favorite League",
                "It takes best team to win",
                "Play more"
        };

        txtTitle.setText(title[0]);
        if (!isSlide){
            isSlide=true;
            Animation animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_from_right);
            txtTitle.startAnimation(animSlideDown);
        }

        Animation animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.top_to_bottom);
        imgLogo.startAnimation(animSlideDown);

        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        pager.setAdapter(myViewPagerAdapter);
        pager.addOnPageChangeListener(viewPagerPageChangeListener);

        btnLogin.setOnClickListener(v->{
            preferences.setPref(PrefConstant.APP_IS_WELCOME,true);
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        txtSkip.setOnClickListener(v->{
            preferences.setPref(PrefConstant.APP_IS_WELCOME,true);
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        /*btnNext.setOnClickListener(v->{

            int current = getItem();
            if (current < images.length) {
                // move to next screen
                pager.setCurrentItem(current);
            } else {
                preferences.setPref(PrefConstant.APP_IS_WELCOME,true);
                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        });*/

    }

    private void addBottomDots(int currentPage) {

        dots = new TextView[images.length];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.gray_8ba6c1));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.white_pure));
    }

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            // txtSubTitle.setText(subTitle[position]);
            txtTitle.setText(title[position]);
            addBottomDots(position);
            if (position == images.length - 1) {
                Animation bottomUp = AnimationUtils.loadAnimation(WelcomeActivity.this,
                        R.anim.bottom_up);
                btnLogin.startAnimation(bottomUp);
                btnLogin.setVisibility(View.VISIBLE);
                txtSkip.setVisibility(View.GONE);

            } else {
                if (btnLogin.getVisibility()==View.VISIBLE){
                    Animation bottomDown = AnimationUtils.loadAnimation(WelcomeActivity.this,
                            R.anim.bottom_down);
                    btnLogin.startAnimation(bottomDown);
                }
                btnLogin.setVisibility(View.GONE);
                txtSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private int getItem() {
        return pager.getCurrentItem() + 1;
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        MyViewPagerAdapter() {
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {


            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert layoutInflater != null;
            View view = layoutInflater.inflate(R.layout.welcome_slide_item, container, false);

           /* TextView txtTitle=view.findViewById(R.id.txtTitle);
            if (!isSlide){
                isSlide=true;
                Animation animSlideDown = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_from_right);
                txtTitle.startAnimation(animSlideDown);
            }*/


            TextView txtSubTitle=view.findViewById(R.id.txtSubTitle);
            ImageView slide_img = (ImageView) view.findViewById(R.id.imgItem);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            float widthPixels = displayMetrics.widthPixels;
            float heightPixels = (float) (widthPixels * 1.33);
            slide_img.setMinimumHeight((int) heightPixels);

            slide_img.setImageResource(images[position]);
            txtSubTitle.setText(subTitle[position]);
            // txtTitle.setText(title[position]);

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

}