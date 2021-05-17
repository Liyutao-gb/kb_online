package com.atguigu.serurity.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * <p>
 * token操作工具类
 * </p>
 *
 * @author qy
 * @since 2019-11-08
 */
@Component
public class TokenManager {

    private long tokenExpiration = 24 * 60 * 60 * 1000; //token有效时长
    private String tokenSignKey = "123456"; //自定义密钥,在编码和解码时用

    /**
     * 1.使用Jwt根据用户名生成token
     */
    public String createToken(String username) {
        String token = Jwts.builder()

                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))

                .signWith(SignatureAlgorithm.HS512, tokenSignKey)

                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    /**
     * 2.从token字符串中得到用户信息
     */
    public String getUserFromToken(String token) {
        String user = Jwts.parser()
                .setSigningKey(tokenSignKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        return user;
    }

    /**
     * 删除token
     */
    public void removeToken(String token) {
        //jwttoken无需删除，客户端扔掉即可
    }

}
