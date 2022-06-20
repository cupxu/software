package com.ff.sxbank.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ff.sxbank.exception.ResponseResult;

import java.util.Calendar;
import java.util.Map;

public class JWTUtils {
    //私钥
    private static String TOKEN = "token!Q@W3e4r";
    /**
     * 把想添加的用户信息先放到map再从map生成token
     * @param map
     * @return 返回token
     */
    public static String createToken(Map<String,String> map){
        JWTCreator.Builder builder = JWT.create();
        map.forEach((k,v)->{
            builder.withClaim(k,v);
        });
        Calendar instance = Calendar.getInstance();
        //令牌有效期 1天
        instance.add(Calendar.DATE,1);
        builder.withExpiresAt(instance.getTime());
        return builder.sign(Algorithm.HMAC256(TOKEN)).toString();
    }
    /**
     * 验证token
     * @param token
     * @return DecodedJWT返回的是一个解密后的JWT
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
    }
    /**
     * 获取token中payload
     * @param token
     * @return
     */
    public static DecodedJWT getDecodedToken(String token){
        return JWT.decode(token);
    }
}
