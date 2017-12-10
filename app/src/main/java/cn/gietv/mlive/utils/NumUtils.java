package cn.gietv.mlive.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

import cn.gietv.mlive.constants.CommConstants;

/**
 * author：steven
 * datetime：15/10/8 16:11
 */
public class NumUtils {
    public static int getPage(int total) {
        if (total < CommConstants.COMMON_PAGE_COUNT) {
            return 1;
        }
        if (total % CommConstants.COMMON_PAGE_COUNT == 0) {
            return total / CommConstants.COMMON_PAGE_COUNT;
        } else {
            return total / CommConstants.COMMON_PAGE_COUNT + 1;
        }
    }
    public static int getPage(int total,int pageCount) {
        if (total < pageCount) {
            return 1;
        }
        if (total % pageCount == 0) {
            return total / pageCount;
        } else {
            return total / pageCount + 1;
        }
    }

    public static String w(int num) {
        String w = StringUtils.EMPTY;
        String formatText;
        double n;
        if (num < 10000) {
            n = num;
            formatText = "#,###";
        } else {
            if (num % 10000 == 0) {
                formatText = "#,### 万";
                n = num / 10000;
            } else {
                formatText = "#,###.# 万";
                n = div(num, 10000, 1);
            }
        }
        DecimalFormat f = new DecimalFormat(formatText);
        w = f.format(n);
        return w;
    }

    /**
     * 提供精确加法计算的add方法
     *
     * @param value1 被加数
     * @param value2 加数
     * @return 两个参数的和
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确减法运算的sub方法
     *
     * @param value1 被减数
     * @param value2 减数
     * @return 两个参数的差
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确乘法运算的mul方法
     *
     * @param value1 被乘数
     * @param value2 乘数
     * @return 两个参数的积
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算方法div
     *
     * @param value1 被除数
     * @param value2 除数
     * @param scale  精确范围
     * @return 两个参数的商
     * @throws IllegalAccessException
     */
    public static double div(double value1, double value2, int scale) {
        //如果精确范围小于0，抛出异常信息
        if (scale < 0) {
            return 0;
        }
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
