package com.cs426.dtkhoi.GrabGirl;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class DetailActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;

    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.airpod, R.drawable.watch, R.drawable.iphonex};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();
        
        showContactDetail();
        
        initSlidingImageView();
    }

    @SuppressLint("SetTextI18n")
    private void showContactDetail() {
        Contact contact = (Contact)getIntent().getSerializableExtra("ContactInfo");

        TextView name = findViewById(R.id.name);
        TextView age = findViewById(R.id.age);
        TextView job = findViewById(R.id.job);
        TextView product = findViewById(R.id.product);

        name.setText("Name: " + contact.get_name());
        age.setText("Age: " + contact.get_age());
        job.setText("Distance: " + contact.get_distance());
        product.setText("Product: " + contact.get_product());
    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for (int myImage : myImageList) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImage);
            list.add(imageModel);
        }

        return list;
    }

    private void initSlidingImageView() {

        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImageAdapter(DetailActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

        //Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }
}