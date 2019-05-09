package com.skcodestack.scompress.core;

/**
 * @ProjectName: MyJoy$
 * @Package: com.skcodestack.imgcompress$
 * @ClassName: ResultMode$
 * @Author: sk
 * @CreateDate: 2019/5/7$ 09:53$
 * @UpdateUser: sk
 * @UpdateDate: 2019/5/7$ 09:53$
 * @UpdateRemark:
 * @Version: 1.0
 * @Description:
 */
public enum ResultMode {
    //严格模式只有全部成功才成功
    STRICT,
    //正常模式一个成功就成功，不成功的返回原图片地址
    NORMAL,
}
