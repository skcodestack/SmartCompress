package com.skcodestack.scompress.util;

import android.text.TextUtils;

public class FileUtil {

    /**
     * 获取图片的后缀名 ，如果没有返回png
     * @param filename 文件名
     * @return 后缀名
     */
    public static String getImageSuffix(String filename){
        if (filename != null && !TextUtils.isEmpty(filename)) {
            int index = filename.lastIndexOf(".");
            String type = filename.substring(index + 1);
            if(type != null && !TextUtils.isEmpty(type)){
                return "png";
            }
            return type;
        }
        return "png";
    }

    /**
     * 根据文件名判断，是否是图片类型
     *
     * @param filename  文件名
     * @return 类型
     */
    public static boolean isPicture(String filename) {
        if (filename != null && !TextUtils.isEmpty(filename)) {
            int index = filename.lastIndexOf(".");
            String type = filename.substring(index + 1);
            if(type == null || TextUtils.isEmpty(type)){
                return false;
            }
            return isContain(types, type);
        }
        return false;

    }

    private static String[] types = {"png", "jpg", "jpeg", "bmp", "gif"};
    private static boolean isContain(String[] contain, String param) {
        for (int i = 0; i < contain.length; i++) {
            if (contain[i].toLowerCase().equals(param.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

}
