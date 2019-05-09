package com.skcodestack.imageselect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.skcodestack.imageselect.adapter.SimpleViewPagerAdapter;
import com.skcodestack.imageselect.translucent.TranslucentCompat;
import com.skcodestack.imageselect.view.CustomViewPager;

import java.util.ArrayList;
import java.util.List;


/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2018/8/22
 * Version  1.0
 * Description:
 */
public class ImagePerviewActivity extends AppCompatActivity {

    //已经选中的图片key
    public static final String EXTRA_DATA_LIST = "EXTRA_DATA_LIST";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    AppCompatTextView mHintCountView;
    FrameLayout mTopLayout;
    private int mInitPosition;

    List<String> mDataList;
    int mMaxCount = 0;
    private CustomViewPager mViewPager;
    private ImageView mBackView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        TranslucentCompat.setStatusBarColor(this, mTopLayout, false, getResources().getColor(R.color.transparent));
        TranslucentCompat.setNavigationBarColor(this, false, getResources().getColor(R.color.transparent));

        initBundle();
        initView();
        initEvent();

    }

    public static void start(Context context, String path) {
        ArrayList<String> list = new ArrayList<>();
        list.add(path);
        start(context, list, 0);
    }

    public static void start(Context context, ArrayList<String> list) {
        start(context, list, 0);
    }

    public static void start(Context context, ArrayList<String> list, int current) {
        Intent intent = new Intent(context, ImagePerviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_DATA_LIST, list);
        bundle.putInt(EXTRA_POSITION, current);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    public void initView() {

        mViewPager = findViewById(R.id.viewpager);
        mBackView = findViewById(R.id.iv_perview_back);
        mHintCountView = findViewById(R.id.tv_perview_count_hint);
        mTopLayout = findViewById(R.id.perview_actionBar);
    }


    private void initBundle() {
        mDataList = (List<String>) getIntent().getExtras().getSerializable(EXTRA_DATA_LIST);
        mInitPosition = getIntent().getExtras().getInt(EXTRA_POSITION, 0);
        if (mDataList != null) {
            mMaxCount = mDataList.size();
        }
    }

    private void initEvent() {
        mBackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SimpleViewPagerAdapter mViewPagerAdapter = new SimpleViewPagerAdapter(this, mDataList);

//        FragmentViewPagerAdapter mViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(),mDataList);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                selectedChangedListener(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        selectedChangedListener(mInitPosition);

        mViewPager.setCurrentItem(mInitPosition);
    }

    public void selectedChangedListener(int position) {
        int numb = position + 1;
        mHintCountView.setText(numb + "/" + mMaxCount);

    }


}
