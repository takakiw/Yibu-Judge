package com.yibu.yibuJudge.utils;

import com.yibu.yibuJudge.constant.UserConstant;
import com.yibu.yibuJudge.model.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtil {

    private static final String signKey = "takaki";

    private static final Long expire = 60 * 60 * 24 * 70 * 1000L;

    public static String generateJwt(Map<String, Object> claims){
        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, signKey)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .compact();
    }

    public static Map<String, Object> parseJwt(String jwt) {
        return Jwts.parser()
                .setSigningKey(signKey)
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
