package com.accounting.accounting_tool.utilities;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;

public class JwtUtil
{
    private static final String SECRET = "${secret_key}";
    // SecretKey key = Jwts.SIG.HS256;
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public static String generateToken (String username)
    {
        return Jwts.builder()
            .subject(username)
             // .signWith(SECRET, SignatureAlgorithm.HS512)
            .compact();
    }
}
