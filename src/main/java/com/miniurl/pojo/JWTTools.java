package com.miniurl.pojo;

import com.auth0.jwt.algorithms.Algorithm;
import com.miniurl.repository.entity.User;

public class JWTTools {
    public String getToken(User user) {
        String token = "";
        token = com.auth0.jwt.JWT.create()
                .withAudience(user.getUserId().toString())          // 将 user id 保存到 token 里面
                .sign(Algorithm.HMAC256(user.getHashPass()));   // 以 password 作为 token 的密钥
        return token;
    }
}