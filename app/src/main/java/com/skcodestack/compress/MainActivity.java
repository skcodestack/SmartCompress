package com.skcodestack.compress;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.skcodestack.imageselect.ImageSelectActivity;
import com.skcodestack.imageselect.helper.ImageSelectorHelper;
import com.skcodestack.scompress.SmartCompress;
import com.skcodestack.scompress.core.ErrorCode;
import com.skcodestack.scompress.core.QualityLevel;
import com.skcodestack.scompress.core.ResultMode;
import com.skcodestack.scompress.inter.OnDefaultCompressListener;
import com.skcodestack.scompress.inter.OnProgressListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText("图片压缩");
    }

    public void btn_compress(View view) {
        ImageSelectorHelper.create()
                .count(8)
                .multi().showCamera(true).start(this, 0x00002);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 0x00002) {
                List<String> imageList = data.getStringArrayListExtra(ImageSelectActivity.EXTRA_RESULT);
                Log.e("ttt", imageList.toString());
                compressImage(imageList);
            }
        }
    }

    private void compressImage(List<String> imageList) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在压缩...");

        File compressDir = new File(Environment.getExternalStorageDirectory(), "compress_ttt");
        String dir = compressDir.getAbsolutePath() + File.separator;
        Log.e("ttt", "compress image dir :" + dir);

//        SmartCompress smartCompress = SmartCompress.create(this);
        SmartCompress.create(this)
                .dir(dir)
                .width(960)
                .load(imageList)
                .mode(ResultMode.NORMAL)
                .timeout(2000)
                .quality(QualityLevel.LOW)
                .setProgressListener(new OnProgressListener() {
                    @Override
                    public void onStart() {
                        progressDialog.show();
                    }

                    @Override
                    public void onEnd() {
                        progressDialog.dismiss();
                    }
                })
                .setCompressListener(new OnDefaultCompressListener() {
                    @Override
                    public void onSuccess(List<String> paths) {
                        super.onSuccess(paths);
                        Log.e("ttt", "compress image file success :" + paths);
                        Toast.makeText(MainActivity.this, "压缩成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(ErrorCode code) {
                        super.onError(code);
                        Log.e("ttt", "compress image file  faile:" + code);
                        Toast.makeText(MainActivity.this, "压缩失败，失败原因：" + code.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .execute();

//        smartCompress.cancle();
    }
}
