package com.yibu.yibujudge.utils;

import com.yibu.yibujudge.constant.UserConstant;
import com.yibu.yibujudge.model.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    private static final String SIGNKEY = "takaki";

    private static final Long EXPIRE = 60 * 60 * 24 * 70 * 1000L;

    public static String generateJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, SIGNKEY)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .compact();
    }

    public static Map<String, Object> parseJwt(String jwt) {
        return Jwts.parser()
                .setSigningKey(SIGNKEY)
                .parseClaimsJws(jwt)
                .getBody();
    }



    public static String userToJWT(User user) {
        Map<String, Object> mp = new HashMap<>();
        mp.put(UserConstant.USER_ID, user.getId());
        mp.put(UserConstant.ROLE, user.getRole());
        return generateJwt(mp);
    }
}
