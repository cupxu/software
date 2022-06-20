package com.ff.sxbank.sm4;

import com.ff.sxbank.util.IdentityUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * sm4加密解密
 * CBC、ECB模式
 *
 * @author wzk
 */


/**
 * 运    行   方   法
 * <p>
 * 1、CBC模式
 * 前端：
 * var sm4=new SM4Util();
 * sm4.encryptData_CBC('');
 * <p>
 * 后端：
 * SM4Utils.decryptData_CBC("");
 * <p>
 * 2、ECB模式（比较更为安全）
 * 前端：
 * var sm4=new SM4Util();
 * sm4.encryptData_ECB('');
 * <p>
 * 后端：
 * SM4Utils.decryptData_ECB("");
 */


@SuppressWarnings("restriction")
public class SM4Utils {

    /**
     * 和前端key一致
     */
    private static String secretKey = "GJwsXX_BzW=gJWJW";

    /**
     * 和前端iv一致
     */
    private static String iv = "ZkR_SiNoSOFT=568";

    public static String getSecretKey() {
        return secretKey;
    }

    public static void setSecretKey(String secretKey) {
        SM4Utils.secretKey = secretKey;
    }

    public static String getIv() {
        return iv;
    }

    public static void setIv(String iv) {
        SM4Utils.iv = iv;
    }

    private static String UTF_8 = "UTF-8";

    private static final boolean hexString = false;

    public SM4Utils() {
    }

    // ECB模式加密
    public static String encryptData_ECB(String plainText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            keyBytes = secretKey.getBytes();
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_ecb(ctx, plainText.getBytes(UTF_8));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // ECB模式解密
    public static String decryptData_ECB(String cipherText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            keyBytes = secretKey.getBytes();
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(ctx, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // CBC模式加密
    public static String encryptData_CBC(String plainText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;

            keyBytes = secretKey.getBytes();
            ivBytes = iv.getBytes();

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            byte[] encrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, plainText.getBytes(UTF_8));
            String cipherText = new BASE64Encoder().encode(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Pattern p = Pattern.compile("\\s*|\t|\r|\n");
                Matcher m = p.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // CBC模式解密
    public static String decryptData_CBC(String cipherText) {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = Util.hexStringToBytes(secretKey);
                ivBytes = Util.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_cbc(ctx, ivBytes, new BASE64Decoder().decodeBuffer(cipherText));
            return new String(decrypted, UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
