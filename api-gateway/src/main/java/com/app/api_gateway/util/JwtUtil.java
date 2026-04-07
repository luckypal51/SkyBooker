package com.app.api_gateway.util;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {
	private static String SECRET = "asdfghjklqwertyuiopzxcvbnm756312369qwertyuiopsdfghjk";
	
	public static  void validate(String token) {
		Jwts.parser().setSigningKey(getKey()).build().parseClaimsJws(token);
	}
	
	 public static SecretKey getKey(){
         return Keys.hmacShaKeyFor(SECRET.getBytes());
     }
	 
	 public  static Claims getClaims(String token){
         return Jwts.parser()
                 .verifyWith(getKey())
                 .build().parseSignedClaims(token)
                 .getPayload();
     }
}
