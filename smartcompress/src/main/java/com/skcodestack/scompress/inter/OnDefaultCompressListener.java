package com.skcodestack.scompress.inter;

import com.skcodestack.scompress.core.ErrorCode;

import java.util.List;

public class OnDefaultCompressListener implements OnCompressListener {

    @Override
    public void onSuccess(List<String> paths) {

    }

    /**
     * 如果只压缩一张，这个方法比较好用
     * 如果有多张图片，只会选择其中一张
     * @param paths 返回的
     */
    @Override
    public void onSuccess(String paths) {

    }

    @Override
    public void onError(ErrorCode code) {

    }
}
