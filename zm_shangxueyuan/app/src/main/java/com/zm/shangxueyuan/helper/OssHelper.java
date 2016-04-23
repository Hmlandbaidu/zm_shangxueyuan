package com.zm.shangxueyuan.helper;

import android.content.Context;

import com.zm.utils.PhoneUtil;

/**
 * Creator: dengshengjin on 16/4/23 18:14
 * Email: deng.shengjin@zuimeia.com
 */
public class OssHelper {
    public static String getImageTopicUrl(Context context, String sourceUrl) {
        String finalUrl = sourceUrl + "@" + PhoneUtil.getDisplayWidth(context) + "w";
        return finalUrl;
    }

    public static String getImageCommonUrl(Context context, String sourceUrl) {
        String finalUrl = sourceUrl + "@" + PhoneUtil.getDisplayWidth(context) / 2 + "w";
        return finalUrl;
    }

    public static String getImageListUrl(Context context, String sourceUrl) {
        String resUrl = "@%sw_%sh_0e";
        String finalUrl = sourceUrl + String.format(resUrl, PhoneUtil.getDisplayWidth(context) / 4, PhoneUtil.getDisplayHeight(context) / 4);
        return finalUrl;
    }

    public static String getImageDetailUrl(Context context, String sourceUrl) {
        String resUrl = "@%sw_%sh_0e";
        String finalUrl = sourceUrl + String.format(resUrl, PhoneUtil.getDisplayWidth(context), PhoneUtil.getDisplayHeight(context));
        return finalUrl;
    }
}
