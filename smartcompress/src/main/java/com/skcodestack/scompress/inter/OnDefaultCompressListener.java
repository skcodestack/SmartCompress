package com.skcodestack.scompress.inter;

import com.skcodestack.scompress.core.ErrorCode;

import java.util.List;

/**
 * @ProjectName: MyJoy$
 * @Package: com.skcodestack.imgcompress$
 * @ClassName: OnCompressListener$
 * @Author: sk
 * @CreateDate: 2019/5/7$ 09:45$
 * @UpdateUser: sk
 * @UpdateDate: 2019/5/7$ 09:45$
 * @UpdateRemark:
 * @Version: 1.0
 * @Description:
 */
public class OnDefaultCompressListener implements OnCompressListener {

    @Override
    public void onSuccess(List<String> paths) {

    }

    /**
     * 如果只压缩一张，这个方法比较好用
     * 如果有多张图片，只会选择其中一张
     * @param paths
     */
    @Override
    public void onSuccess(String paths) {

    }

    @Override
    public void onError(ErrorCode code) {

    }
}
