package com.mmall.util;

import java.math.BigDecimal;

/**
 * Created by fanlinglong on 2018/1/21.
 * BigDecimalUtil 计算工具类
 */
public class BigDecimalUtil {

    private BigDecimalUtil(){}

    //相加
    public static BigDecimal add (double v1 ,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }
    //相减
    public static BigDecimal sub (double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }
    //相乘
    public static BigDecimal mul (double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }
    //相除,四舍五入,保留两位小数
    public static BigDecimal div (double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
    }

    public static void main(String[] args){
        System.out.println(0.05+0.01);//丢失精度
    }
}
