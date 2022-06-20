package com.ff.sxbank.util;

import java.util.Calendar;

/**
 * @description: 计算年龄
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-03-04 15:55
 **/
public class CalculateAge {

    /**
     * 根据身份证计算年龄
     *
     * @param idcard
     * @return
     */
    public static Integer idCardToAge(String idcard) {
        Integer selectYear = Integer.valueOf(idcard.substring(6, 10));         //出生的年份
        Integer selectMonth = Integer.valueOf(idcard.substring(10, 12));       //出生的月份
        Integer selectDay = Integer.valueOf(idcard.substring(12, 14));         //出生的日期
        Calendar cal = Calendar.getInstance();
        Integer yearMinus = cal.get(Calendar.YEAR) - selectYear;
        Integer monthMinus = cal.get(Calendar.MONTH) + 1 - selectMonth;
        Integer dayMinus = cal.get(Calendar.DATE) - selectDay;
        Integer age = yearMinus;
        if (yearMinus < 0) {
            age = 0;
        } else if (yearMinus == 0) {
            age = 0;
        } else if (yearMinus > 0) {
            if (monthMinus == 0) {
                if (dayMinus < 0) {
                    age = age - 1;
                }
            } else if (monthMinus > 0) {
                age = age + 1;
            }
        }
        return age;
    }

}
