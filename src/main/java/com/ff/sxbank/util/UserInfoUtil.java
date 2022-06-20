package com.ff.sxbank.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @description: 随机生成用户逾期信息
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-04-10 20:22
 **/
public class UserInfoUtil {

    public static List<Integer> generateInfo() {
        Random r = new Random();
        List<Integer> list = new ArrayList();
        int isBorrow = r.nextInt(2);
        int isStore = r.nextInt(2);
        int workState = r.nextInt(3);
        int overdueDays = r.nextInt(100);
        int overDuetimes = r.nextInt(5);

        list.add(isBorrow);
        list.add(isStore);
        list.add(workState);
        list.add(overdueDays);
        list.add(overDuetimes);

        return list;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            System.out.println(generateInfo());
        }
    }

}
