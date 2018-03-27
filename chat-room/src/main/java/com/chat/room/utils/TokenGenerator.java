package com.chat.room.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenGenerator {
	
	@Value( "${token.secretKey}" )
	private String secretKey;
	
	@Value( "${token.bearerStr}" )
	private String bearerStr;
	
	public TokenGenerator() {
		
	}
	
	public String getToken(String username) {
		return bearerStr + " " + Jwts.builder().setSubject(username).claim("roles", "user").setIssuedAt(new Date())
		.signWith(SignatureAlgorithm.HS256, secretKey).compact();
	}
	
}
