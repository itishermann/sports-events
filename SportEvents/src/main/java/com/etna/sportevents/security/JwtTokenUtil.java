package com.etna.sportevents.security;

import com.etna.sportevents.models.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    @Value("${jwt.access_token.expiration_ms}")
    private int ACCESS_TOKEN_EXPIRATION_MS;

    @Value("${jwt.refresh_token.expiration_ms}")
    private int REFRESH_TOKEN_EXPIRATION_MS;

    @Value("${jwt.prefix}")
    private String jwtPrefix;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractTokenFromHeader(String header) {
        return header.replace(jwtPrefix, "").trim();
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Integer getUserIdFromToken(String token) {
        return Integer.parseInt(getAllClaimsFromToken(token).getId());
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    public Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String generateAccessToken(JwtUserDetails jwtUserDetails) {
        return Jwts.builder()
                .setSubject(jwtUserDetails.getUsername())
                .setId(String.valueOf(jwtUserDetails.getId()))
                .setIssuer("SportEvents")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_MS + 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public String generateRefreshToken(
            String accessToken
    ) {
        final String username = getUsernameFromToken(accessToken);
        final Integer userId = getUserIdFromToken(accessToken);
        return Jwts.builder()
                .setSubject(username)
                .setId(String.valueOf(userId))
                .claim("access_token", accessToken)
                .setIssuer("SportEvents")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_MS + 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public Boolean validateToken(String token, JwtUserDetails jwtUserDetails) {
        // Validate by id because id is immutable but username can be changed
        final Integer userId = getUserIdFromToken(token);
        return (userId.equals(jwtUserDetails.getId()) && !isTokenExpired(token));
    }
}
