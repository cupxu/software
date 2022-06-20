package com.ff.sxbank.util;

import com.ff.sxbank.sm4.SM4Utils;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 秒杀链接工具类
 * @Param:
 * @return:
 * @author: xulifeng
 * @create: 2022-03-16 16:40
 **/
@Slf4j
public class LinkUtil {

    public static String generateLink(String val) {
        return SM4Utils.encryptData_ECB(val).replace("/","");
    }

    public static boolean verifyLink(String path, String src) {
        log.info("path:{}",path);
        log.info("src:{}",src);
        log.info("解密后：{}",SM4Utils.decryptData_ECB(path));
        return SM4Utils.encryptData_ECB(src).replace("/","").equals(path) ? true : false;
    }

}
