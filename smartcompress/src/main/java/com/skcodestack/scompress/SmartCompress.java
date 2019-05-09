package com.skcodestack.scompress;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.skcodestack.scompress.core.ErrorCode;
import com.skcodestack.scompress.core.QualityLevel;
import com.skcodestack.scompress.core.ResultMode;
import com.skcodestack.scompress.inter.OnCompressListener;
import com.skcodestack.scompress.inter.OnProgressListener;
import com.skcodestack.scompress.util.CompressUtil;
import com.skcodestack.scompress.util.FileUtil;
import com.skcodestack.scompress.util.ThreadPool;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @ProjectName: SmartCompress$
 * @Package: com.skcodestack.scompress$
 * @ClassName: SmartCompress$
 * @Author: sk
 * @CreateDate: 2019/5/8$ 10:33$
 * @UpdateUser: sk
 * @UpdateDate: 2019/5/8$ 10:33$
 * @UpdateRemark:
 * @Version: 1.0
 * @Description: 灵活的图片压缩框架
 */
public class SmartCompress {

    private Context mContext = null;
    private ThreadPool mThreadPool = null;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    //将要压缩的图片
    private List<String> mFileList;
    //压缩图片的质量
    QualityLevel mLevel = QualityLevel.NORMAL;
    OnCompressListener mCompressListener = null;
    OnProgressListener mProgressListener = null;
    //回调模式
    ResultMode mMode = ResultMode.NORMAL;
    //多线程情况下，存储压缩后的图片
    CopyOnWriteArrayList<String> mResultList = null;
    //压缩后图片宽高
    int mFinalWidth = -1;
    int mFinalHeigth = -1;
    //当前压缩任务是否开始
    boolean isExecuted = false;
    //压缩多个图片，是否有错误
    volatile boolean hasFailed = false;
    //当前任务是否手动取消
    volatile boolean isCancle = false;
    //当前任务完成，包括取消
    boolean isEnd = false;
    //压缩超时时间，单位微妙
    long mTimeout = 20000;
    //压缩图片存放路径
    String mCompressDir = null;

    private SmartCompress() {
        throw new RuntimeException("no params controst method  not enable to invoke!");
    }

    private SmartCompress(Context context) {
        if (context == null) {
            throw new RuntimeException("context is null pointer");
        }
        this.mContext = context.getApplicationContext();
        mThreadPool = ThreadPool.getInstance();
    }

    /**
     * 使用工厂模式创建
     *
     * @param context
     * @return
     */
    public static SmartCompress create(Context context) {
        return new SmartCompress(context);
    }


    /**
     * 添加图片
     *
     * @param paths
     * @return
     */
    public SmartCompress load(String... paths) {
        if (paths != null && paths.length > 0) {
            addPaths(Arrays.asList(paths));
        }
        return this;
    }

    /**
     * 添加图片
     *
     * @param list
     * @return
     */
    public SmartCompress load(List<String> list) {
        if (list != null && list.size() > 0) {
            addPaths(list);
        }
        return this;
    }

    /**
     * 设置图片压缩质量
     *
     * @param level
     * @return
     */
    public SmartCompress quality(QualityLevel level) {
        if (level != null) {
            mLevel = level;
        }
        return this;
    }

    /**
     * 设置目标宽
     *
     * @param width
     * @return
     */
    public SmartCompress width(int width) {
        mFinalWidth = width;
        return this;
    }

    /**
     * 设置目标高
     *
     * @param height
     * @return
     */
    public SmartCompress height(int height) {
        mFinalHeigth = height;
        return this;
    }

    /**
     * 设置结果模式
     * STRICT:严格模式 只有全部成功才成功
     * NORMAL:正常模式 一个成功就成功，不成功的返回原图片地址
     *
     * @param mode
     * @return
     */
    public SmartCompress mode(ResultMode mode) {
        if (mode != null) {
            mMode = mode;
        }
        return this;
    }

    /**
     * 设置压缩图片存放目录
     *
     * @param dir
     * @return
     */
    public SmartCompress dir(String dir) {
        this.mCompressDir = dir;
        return this;
    }

    /**
     * 设置压缩超时时间【单位：MILLISECONDS】,默认 20秒[20000]
     *
     * @param timeout TimeUnit.MILLISECONDS
     * @return
     */
    public SmartCompress timeout(long timeout) {
        if (timeout >= 1000) {
            this.mTimeout = timeout;
        } else {
            this.mTimeout = 1000;
        }
        return this;
    }

    /**
     * 添加压缩监听,回调方法运行在主线程
     *
     * @param listener
     * @return
     */
    public SmartCompress setCompressListener(OnCompressListener listener) {
        this.mCompressListener = listener;
        return this;
    }

    /**
     * 任务的状态监听，运行在主线程
     *
     * @param listener
     * @return
     */
    public SmartCompress setProgressListener(OnProgressListener listener) {
        this.mProgressListener = listener;
        return this;
    }


    /**
     * 运行，开始压缩
     */
    public void execute() {
        if (isExecuted) {
            throw new RuntimeException("lauch is not execute twich");
        }
        if (mFileList == null || mFileList.size() == 0) {
            throw new RuntimeException("compress image is empty");
        }
        if (mFileList == null || mFileList.size() == 0) {
            throw new RuntimeException("compress image is empty");
        }
        if (mCompressListener == null) {
            throw new RuntimeException("CompressListener  is null poniter");
        }
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            onError(ErrorCode.NON_WRITE_STORAGE_PERMISSION);
            return;
        }
        if (mCompressDir == null || TextUtils.isEmpty(mCompressDir)) {
            mCompressDir = mContext.getExternalCacheDir().getAbsolutePath() + "img_compress_tem";
        }
        File dir = new File(mCompressDir);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new RuntimeException("compress dir is not exist,please");
        }

        if (mLevel == null) {
            mLevel = QualityLevel.NORMAL;
        }
        if (mMode == null) {
            mMode = ResultMode.NORMAL;
        }
        if (mTimeout <= 1000) {
            mTimeout = 1000;
        }
        if (mResultList == null) {
            mResultList = new CopyOnWriteArrayList<>();
        }
        // 开始压缩
        onStart();

        //1.提取出非图片文件 和删除空路径
        Iterator<String> it = mFileList.iterator();
        while (it.hasNext()) {
            String path = it.next();
            if (path == null || TextUtils.isEmpty(path)) {
                it.remove();
                continue;
            }
            if (!FileUtil.isPicture(path)) {
                mResultList.add(path);
                it.remove();
            }
        }

        if (mFileList.size() == 0) {
            onSuccess(mResultList);
            return;
        }

        int size = mFileList.size();
        final CountDownLatch countDownLatch = new CountDownLatch(size);

        for (final String path : mFileList) {
            final String temFilePath = mCompressDir + File.separator + System.currentTimeMillis() + "." + FileUtil.getImageSuffix(path);
            mThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    runCompress(path, temFilePath, countDownLatch);
                }
            });
        }

        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                runCompressWait(countDownLatch);
            }
        });
    }

    /**
     * 取消当前压缩的任务
     */
    public void cancle() {
        if (!isCancle && isExecuted) {
            isCancle = true;
            mHandler.removeCallbacksAndMessages(null);
        }
        onCancle();
    }

    /**
     * 关闭所有任务
     */
    public static void shutdown() {
        ThreadPool threadPool = ThreadPool.getInstance();
        threadPool.shutdownNow();
    }


    /**
     * 执行压缩任务
     *
     * @param srcPath
     * @param dstpath
     * @param countDownLatch
     */
    private void runCompress(String srcPath, String dstpath, CountDownLatch countDownLatch) {

        try {
            if (isCancle) {
                mResultList.add(srcPath);
                countDownLatch.countDown();
                return;
            }
            Bitmap bitmap = CompressUtil.decodeFile(srcPath, mFinalWidth, mFinalHeigth);
            if (CompressUtil.compressImage(bitmap, mLevel.mQuality, dstpath, true)) {
                mResultList.add(dstpath);
            } else {
                mResultList.add(srcPath);
                hasFailed = true;
            }
            countDownLatch.countDown();
        } catch (Exception ex) {
            mResultList.add(srcPath);
            countDownLatch.countDown();
        }

    }

    /**
     * 等待压缩的任务
     *
     * @param countDownLatch
     */
    private void runCompressWait(CountDownLatch countDownLatch) {
        try {
            boolean isFin = countDownLatch.await(mTimeout, TimeUnit.MILLISECONDS);
            if (isCancle) {
                return;
            }
            if (hasFailed && mMode == ResultMode.STRICT) {
                onError(ErrorCode.COMPRESS_FAILED);
                return;
            }

            if (isFin) {
                onSuccess(mResultList);
                return;
            }
            onError(ErrorCode.COMPRESS_TIME_OUT);
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            onError(ErrorCode.COMPRESS_TIME_OUT);
            return;
        }
    }

    /**
     * 添加压缩图片
     *
     * @param paths
     */
    private void addPaths(List<String> paths) {
        if (mFileList == null) {
            mFileList = new ArrayList<>();
        }
        mFileList.addAll(paths);
    }

    /**
     * 成功回调
     *
     * @param list
     */
    private void onSuccess(final List<String> list) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                isEnd = true;
                if (mCompressListener != null) {
                    mCompressListener.onSuccess(list);
                    mCompressListener.onSuccess((list != null && list.size() > 0) ? list.get(0) : "");
                }
                if (mProgressListener != null) {
                    mProgressListener.onEnd();
                }
            }
        });
    }

    /**
     * 失败回调
     *
     * @param code
     */
    private void onError(final ErrorCode code) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                isEnd = true;
                if (mCompressListener != null) {
                    mCompressListener.onError(code);
                }
                if (mProgressListener != null) {
                    mProgressListener.onEnd();
                }
            }
        });
    }

    /**
     * 任务取消回调
     */
    private void onCancle() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!isEnd && mProgressListener != null) {
                    mProgressListener.onEnd();
                }
                isEnd = true;
            }
        });
    }

    /**
     * 任务开始回调
     */
    private void onStart() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                isEnd = false;
                if (mProgressListener != null) {
                    mProgressListener.onStart();
                }
            }
        });
    }
}
