package com.ming.m_blog.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

/**
 * JWT工具类
 */
public class JwtUtil {

    //有效期为
    public static final Long JWT_TTL =3 * 60 * 60 *1000L;//3 * 60 * 60 *1000  三个小时
    //设置秘钥明文
    public static final String JWT_KEY = "ming";

    public static String getUUID(){
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        return token;
    }

    /**
     * 生成jtw
     * @param subject token中要存放的数据（json格式）
     * @return
     */
    public static String createJWT(String subject) {
        JwtBuilder builder = getJwtBuilder(subject, null, getUUID());// 设置过期时间,不设置就为默认的三个小时
        return builder.compact();
    }

    /**
     * 生成jwt
     * @param subject token中要存放的数据（json格式）
     * @param ttlMillis token超时时间
     * @return
     */
    public static String createJWT(String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, getUUID());// 设置过期时间
        return builder.compact();
    }

    private static JwtBuilder getJwtBuilder(String subject, Long ttlMillis, String uuid) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        SecretKey secretKey = generalKey();
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        if(ttlMillis==null){
            ttlMillis=JwtUtil.JWT_TTL;
        }
        long expMillis = nowMillis + ttlMillis;
        Date expDate = new Date(expMillis);
        return Jwts.builder()
                .setId(uuid)              //唯一的ID
                .setSubject(subject)   // 主题  可以是JSON数据
                .setIssuer("sg")     // 签发者
                .setIssuedAt(now)      // 签发时间
                .signWith(signatureAlgorithm, secretKey) //使用HS256对称加密算法签名, 第二个参数为秘钥
                .setExpiration(expDate);
    }

    /**
     * 创建token
     * @param id
     * @param subject
     * @param ttlMillis
     * @return
     */
    public static String createJWT(String id, String subject, Long ttlMillis) {
        JwtBuilder builder = getJwtBuilder(subject, ttlMillis, id);// 设置过期时间
        return builder.compact();
    }

    public static void main(String[] args) throws Exception {
        String token = createJWT("dede");  // JWT可以支持JSON格式的加密
        System.out.println(token);
        // System.out.println("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJhMTdmMjgxM2M2Mzc0MGVmYTc3MGQyYTczZGZmNDkyMSIsInN1YiI6IjEyMzQ1NiIsImlzcyI6InNnIiwiaWF0IjoxNjQ3MzQ3MTMyLCJleHAiOjE2NDczNTA3MzJ9.blDSBULCSg-1qHFzGYSrUwoHNAdkQzy-6xFqw3w_ep0");
        // Claims claims = parseJWT(token);   // 对加密后的数据进行解密
        // Claims claims = parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0MDUyMTg5ZTMwNWI0YzVjOTFmYWVhNDUyYzg2ZWJiMiIsInN1YiI6IjEyMzQ1NiIsImlzcyI6InNnIiwiaWF0IjoxNjQ3Mzk3Mzg4LCJleHAiOjE2NDc0MDA5ODh9.X_pKcZgmP_bU_JRrsQEXNNmaTsbeeX6jESC-4QGhSiQ");   // 对加密后的数据进行解密
        // System.out.println(claims.getSubject());  // 获取解密的详细内容
    }

    /**
     * 生成加密后的秘钥 secretKey
     * @return
     */
    public static SecretKey generalKey() {
        byte[] encodedKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        return key;
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) throws Exception {
        SecretKey secretKey = generalKey();
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
    }

    /**
     * 解析token获取subject
     * @param token
     * @return
     * @throws Exception
     */
    public static String parseJwtGetSubject(String token) throws Exception {
        Claims claims = parseJWT(token);
        return claims.getSubject();
    }

}
