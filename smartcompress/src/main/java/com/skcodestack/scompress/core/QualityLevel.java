package com.skcodestack.scompress.core;

/**
 * @ProjectName: MyJoy$
 * @Package: com.skcodestack.imgcompress$
 * @ClassName: QualityLevel$
 * @Author: sk
 * @CreateDate: 2019/5/7$ 09:41$
 * @UpdateUser: sk
 * @UpdateDate: 2019/5/7$ 09:41$
 * @UpdateRemark:
 * @Version: 1.0
 * @Description:
 */
public enum QualityLevel  {

    LOW(50),
    NORMAL(70),
    HIGHT(100);

    public int mQuality;
    QualityLevel(int i) {
        mQuality = i;
    }
}
