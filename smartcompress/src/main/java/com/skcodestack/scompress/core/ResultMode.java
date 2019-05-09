package com.skcodestack.scompress.core;


public enum ResultMode {
    //严格模式只有全部成功才成功
    STRICT,
    //正常模式一个成功就成功，不成功的返回原图片地址
    NORMAL,
}
