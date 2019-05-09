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
public interface OnCompressListener {
    void onSuccess(List<String> paths);
    void onSuccess(String paths);
    void onError(ErrorCode code);
}
