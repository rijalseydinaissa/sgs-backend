package com.example.sgs_backend.infrastructure.security;

import com.example.sgs_backend.infrastructure.config.ApplicationProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service @RequiredArgsConstructor @Slf4j
public class JwtService {

    private final ApplicationProperties props;

    public String generateAccessToken(UserDetails ud) { return generateAccessToken(ud, new HashMap<>()); }

    public String generateAccessToken(UserDetails ud, Map<String, Object> extra) {
        return buildToken(ud, extra, props.getSecurity().getJwt().getExpirationMs());
    }

    public String generateRefreshToken(UserDetails ud) {
        return buildToken(ud, Map.of("type","refresh"), props.getSecurity().getJwt().getRefreshExpirationMs());
    }

    public String generatePartialToken(UserDetails ud) {
        return buildToken(ud, Map.of("partial", true, "type","partial"), 300_000L);
    }

    private String buildToken(UserDetails ud, Map<String, Object> extra, long expMs) {
        Map<String, Object> claims = new HashMap<>(extra);
        claims.put("roles", ud.getAuthorities().stream().map(a -> a.getAuthority()).toList());
        Date now = new Date();
        return Jwts.builder()
                .claims(claims).subject(ud.getUsername())
                .issuedAt(now).expiration(new Date(now.getTime() + expMs))
                .signWith(getSigningKey(), Jwts.SIG.HS512).compact();
    }

    public boolean isTokenValid(String token, UserDetails ud) {
        try { return extractUsername(token).equals(ud.getUsername()) && !isTokenExpired(token); }
        catch (JwtException e) { return false; }
    }

    public boolean isPartialToken(String token) {
        try { return Boolean.TRUE.equals(extractAllClaims(token).get("partial")); }
        catch (Exception e) { return false; }
    }

    public String extractUsername(String token)  { return extractClaim(token, Claims::getSubject); }
    public Date   extractExpiration(String token) { return extractClaim(token, Claims::getExpiration); }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        return resolver.apply(extractAllClaims(token));
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().verifyWith(getSigningKey()).build()
                .parseSignedClaims(token).getPayload();
    }

    private boolean isTokenExpired(String token) { return extractExpiration(token).before(new Date()); }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(props.getSecurity().getJwt().getSecret()));
    }
}
