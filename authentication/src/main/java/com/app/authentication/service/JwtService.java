package com.app.authentication.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
	private String SECRET = "asdfghjklqwertyuiopzxcvbnm756312369qwertyuiopsdfghjk";

    public String generateToken(String username,String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role",role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(getKey())
                .compact();
    }
        public String extractUsername(String token) {
            return getClaims(token).getSubject();
        }
        public Claims getClaims(String token){
            return Jwts.parser()
                    .verifyWith(getKey())
                    .build().parseSignedClaims(token)
                    .getPayload();
        }
        public SecretKey getKey(){
            return Keys.hmacShaKeyFor(SECRET.getBytes());
        }
    public boolean validateToken(String token, UserDetails userDetails) {
    	Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token);
        final String username = extractUsername(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
