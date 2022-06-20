package com.ff.sxbank.util;


import java.io.FileWriter;
import java.io.IOException;

/**
 * @description: 生成测试用户
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-04-11 15:03
 **/
public class PressureTestUtil {

    // 删除所有添加的用户 在数据库中直接用delete语句删除所有密码为test123456的用户
    public static void main(String[] args) throws IOException {
        FileWriter fw = new FileWriter("user_data.txt");
        fw.write("");
        fw.flush();
        int size = 20000;
        String productId = "1";
        for (int i = 0; i < size; i++) {
            fw.write("1111111111"+(11111111 + i)+","+productId);
            fw.write("\r\n");
        }
        fw.flush();
        fw.close();
    }

}
