package com.skcodestack.scompress.core;

/**
 * @ProjectName: MyJoy$
 * @Package: com.skcodestack.imgcompress$
 * @ClassName: ErrorCode$
 * @Author: sk
 * @CreateDate: 2019/5/7$ 15:15$
 * @UpdateUser: sk
 * @UpdateDate: 2019/5/7$ 15:15$
 * @UpdateRemark:
 * @Version: 1.0
 * @Description:
 */
public enum ErrorCode {

    COMPRESS_FAILED("图片压缩失败"),//有图片没有压缩成功
    NON_WRITE_STORAGE_PERMISSION("没有读写存储设备权限"),//没有权限
    COMPRESS_TIME_OUT("压缩超时"),//超时
    INTERRUPTED("压缩图片线程被中断");//被中断

    String errorMsg = "";

    ErrorCode(String msg){
        this.errorMsg = msg;
    }

    public String getMessage(){
        return errorMsg;
    }

}
