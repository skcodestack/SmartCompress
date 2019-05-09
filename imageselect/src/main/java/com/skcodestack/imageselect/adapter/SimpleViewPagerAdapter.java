package com.skcodestack.imageselect.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.skcodestack.imageselect.R;

import java.util.List;

import uk.co.senab.photoview.PhotoView;


//import com.skcodestack.imageselect.wigdet.photoview.PhotoView;

/**
 * Email  1562363326@qq.com
 * Github https://github.com/skcodestack
 * Created by sk on 2018/8/22
 * Version  1.0
 * Description:
 */
public class SimpleViewPagerAdapter extends PagerAdapter {


    private Context mContext;
    private List<String> mDataList;
    private final LayoutInflater mLayoutInflater;


    public SimpleViewPagerAdapter(Context mContext, List<String> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View mContentView = null;
        String info = mDataList.get(position);
        try {

            mContentView = ontainCommonView(mLayoutInflater, container, info);

        } catch (Exception ex) {
            Log.e("xxx", "vvvvv--->" + ex);
        }
        container.addView(mContentView);
        return mContentView;
    }

    private View ontainCommonView(LayoutInflater mLayoutInflater, ViewGroup container, String path) {

        View contentView = mLayoutInflater.inflate(R.layout.item_perview_layout, null);
        final PhotoView mPhotoView = (PhotoView) contentView.findViewById(R.id.preview_image);

        Glide.with(contentView.getContext())
                .load(path)
                .error(R.drawable.miss_default_error)
                .placeholder(R.drawable.miss_default_error)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .override(480, 800)
                .into(mPhotoView);


        return contentView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
