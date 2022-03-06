package com.enten.security.security;

import io.jsonwebtoken.CompressionCodecs;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * token管理
 *
 * @author qy
 * @since 2019-11-08
 */
@Component
public class TokenManager {

    // token的有效时长
    private final long tokenExpiration = 24*60*60*1000;

    // 编码密钥
    private final String tokenSignKey = "123456";

    /**
     *  根据用户名生成token
     *  setSubject:设置内容主体
     *  setExpiration:设置过期时间
     *  signWith:密钥编码加密
     */
    public String createToken(String username) {
        String token = Jwts.builder().setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .signWith(SignatureAlgorithm.HS512, tokenSignKey).compressWith(CompressionCodecs.GZIP).compact();
        return token;
    }

    /**
     * 从token字符串中得到用户信息
     */
    public String getUserFromToken(String token) {
        String user = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token).getBody().getSubject();
        return user;
    }

    public void removeToken(String token) {
        //JWTtoken无需删除，客户端扔掉即可。
    }

}
