package com.wl3321.utils;

import java.text.DecimalFormat;

/**
 * author : WYH
 * e-mail : wenyuhang@qinjia001.com
 * date   : 2020/10/16 17:42
 * desc   :
 */
public class CoinUtils {
    static DecimalFormat dF = new DecimalFormat("0.00");

    public static float calc(float a) {
        return Float.parseFloat(dF.format((float)a/1000));
    }

    public static float format(float a){
        return Float.parseFloat(dF.format(a));
    }
}
