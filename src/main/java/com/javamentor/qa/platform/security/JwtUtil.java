package com.javamentor.qa.platform.security;

import com.javamentor.qa.platform.models.entity.user.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Deserializer;
import io.jsonwebtoken.lang.Assert;
import io.jsonwebtoken.lang.DateFormats;
import io.jsonwebtoken.lang.Strings;
import io.jsonwebtoken.security.InvalidKeyException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import io.jsonwebtoken.security.WeakKeyException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration-in-sec}")
    private String expirationTime;

    public Claims getClaimsFromToken(String authToken) {
        String key = Base64.getEncoder().encodeToString(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authToken)
                .getBody();
    }

    public String extractUsername(String authToken) {
        return getClaimsFromToken(authToken)
                .getSubject();
    }

    public boolean validateToken(String authToken) {
        return getClaimsFromToken(authToken)
                .getExpiration()
                .after(new Date());
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateToken(User user, Boolean rememberMe) {
        Long expirationSeconds = Long.parseLong(expirationTime);
        if(rememberMe){
            expirationSeconds = expirationSeconds * 365 * 100;
        }
        return generateToken(user, expirationSeconds);
    }

    public String generateToken(User user, Long expirationSeconds) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().getName());

        Date creationDate = new Date();
        Date expirationDate = new Date(creationDate.getTime() + expirationSeconds * 1000);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(creationDate)
                .setExpiration(expirationDate)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    public boolean rememberMe(HttpServletRequest req){
        if(req.getCookies() == null){
            return false;
        }
        return Arrays.stream(req.getCookies()).anyMatch(x -> {
            return x.getName().equals("rememberme") & x.getValue().equals("true");
        });
    }
}
