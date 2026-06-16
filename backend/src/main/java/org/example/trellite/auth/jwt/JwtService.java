package org.example.trellite.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.expiration-ms}")
    private Long JWT_EXPIRATION;
    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;


    public String doGenerateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(
            Map<String, Object> claims,
            UserDetails userDetails
    ) {
        return buildToken(claims, userDetails);
    }

    public String buildToken(
        Map<String, Object> claims,
        UserDetails userDetails
    ) {
        var authorities = userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts
                .builder()
                .claims( claims )
                .subject( userDetails.getUsername() )
                .issuedAt( new Date(System.currentTimeMillis()) )
                .expiration( new Date(System.currentTimeMillis() + JWT_EXPIRATION) )
                .claim("authorities", authorities)
                .signWith( getSignInKey() )
                .compact();
    }

    private SecretKey getSignInKey() {
        byte[] keyToBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyToBytes);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return ( username.equals(userDetails.getUsername()) && !isTokenExpired(token) );
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith( getSignInKey() )
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
