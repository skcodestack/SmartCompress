package com.skcodestack.scompress.inter;

import com.skcodestack.scompress.core.ErrorCode;

import java.util.List;


public interface OnCompressListener {
    void onSuccess(List<String> paths);
    void onSuccess(String paths);
    void onError(ErrorCode code);
}
