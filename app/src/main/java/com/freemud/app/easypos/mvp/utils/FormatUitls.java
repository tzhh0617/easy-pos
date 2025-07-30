package com.freemud.app.easypos.mvp.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;

/**
 * Created by shuyuanbo on 2020/9/3.
 * Description:
 */
public class FormatUitls {
    /**
     * 保留小数点后2位
     *
     * @return
     */
    public static String keepTwo(double b) {
        DecimalFormat format = new DecimalFormat("#0.00");
        String str = format.format(b);
        return str;
    }

    public static String keepTwoInt(int b) {
        double num = (double) b;
        DecimalFormat format = new DecimalFormat("#0.00");
        String str = format.format(num / 100);
        str = addCommaBigNumber(str);
        //12.00
        int index = str.indexOf(".00");
        if (index > -1) {
            str = str.substring(0,index);
        }
        return str;
    }

    public static String keepTwoInt(Integer b) {
        if (b == null) {
            return "";
        }
        double num = (double) b;
        DecimalFormat format = new DecimalFormat("#0.00");
        String str = format.format(num / 100);
        str = addCommaBigNumber(str);
        //12.00
        int index = str.indexOf(".00");
        if (index > -1) {
            str = str.substring(0,index);
        }
        return str;
    }

    public static String keepOneInt(Integer b) {
        if (b == null) {
            return "";
        }
        double num = (double) b;
        DecimalFormat format = new DecimalFormat("#0.0");
        String str = format.format(num / 10);
        str = addCommaBigNumber(str);
        //12.00
        int index = str.indexOf(".0");
        if (index > -1) {
            str = str.substring(0,index);
        }
        return str;
    }

    public static String strMoney(String text) {
        if (TextUtils.isEmpty(text)) {
            return "0.00";
        }else {
            if (text.endsWith(".")){
                text = text.substring(0, text.length()-1);
            }
            return text;
        }
    }

    /**
     * 金额添加千分位
     *
     * @return
     */
    public static String addCommaBigNumber(String text) {
        if (TextUtils.isEmpty(text)) {
            return text;
        }
        String tmp = null;
        String tail = null; //小数点
        if (text.indexOf(".") > -1) {
            tail = text.substring(text.indexOf("."));
            tmp = text.substring(0, text.indexOf("."));
        }
        StringBuilder sb = new StringBuilder(tmp);
        sb.reverse();
        for (int i = 3; i < sb.length(); i += 4) {
            sb.insert(i, ",");
        }
        sb.reverse();
        if (tail != null) {
            sb.append(tail);
        }
        return sb.toString();
    }

    public static String dealPhoneHide(String oldPhone) {
        if (TextUtils.isEmpty(oldPhone)) {
            return "";
        }
        String regex = "(\\d{3})\\d{4}(\\d{4})";
        String replace = "$1****$2";
        return oldPhone.replaceAll(regex, replace);
    }

    public static String dealTimeRep(String str,String find,String replace) {
        if (TextUtils.isEmpty(str) || !str.contains(find)) {
            return str;
        }
        String tmpStr = str.replaceAll(find,replace);
        return tmpStr;
    }

    /**
     *
     * @param b
     * @param fixed 保留几位小数 默认0
     * @param extra 倍数/ 如果是*传 0.x
     * @return
     */
    public static String dealAny(int b, int fixed,int extra) {
        StringBuilder pattern = new StringBuilder("#0");
        if (fixed > 0) {
            pattern.append(".");
            for (int i = 0 ;i < fixed;i++) {
                pattern.append("0");
            }
        }

        double num = (double) b;
        DecimalFormat format = new DecimalFormat(pattern.toString());
        String str = format.format(num / extra);
        return str;
    }

    /**
     *
     * @param number
     * @return
     */
    public static String dealNumberAppendZero(int number) {
        if (number < 10) {
            return "0" + number;
        }
        return String.valueOf(number);
    }
}
