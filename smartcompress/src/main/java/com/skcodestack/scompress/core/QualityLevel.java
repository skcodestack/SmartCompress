package com.skcodestack.scompress.core;


public enum QualityLevel  {

    LOW(50),
    NORMAL(70),
    HIGHT(100);

    public int mQuality;
    QualityLevel(int i) {
        mQuality = i;
    }
}
