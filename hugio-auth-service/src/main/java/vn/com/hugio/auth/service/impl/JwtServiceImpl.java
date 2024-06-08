package vn.com.hugio.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.com.hugio.auth.dto.UserDto;
import vn.com.hugio.auth.service.JwtService;
import vn.com.hugio.common.log.LOG;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Component
public class JwtServiceImpl implements JwtService {

    private static SecretKey key;

    @Value("${token.secret:jtgLdUg80hwnaklzvG6P9qoOit5yryHt}")
    private String secretKey;

    @Value("${token.time:432000}")
    private Integer time;

    @PostConstruct
    public void setJwtKey() {
        byte[] keyBytes = Decoders.BASE64.decode(this.secretKey);
        key = Keys.hmacShaKeyFor(keyBytes);
    }


    @Override
    public String generateJWTToken(UserDto userDto) {

        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        now.add(Calendar.SECOND, time);

        return Jwts.builder()
                .subject((userDto.getUsername()))
                .issuedAt(new Date())
                .expiration(new Date(now.getTimeInMillis()))
                .signWith(key)
                .claim("userUid", userDto.getUserUid())
                .claim("username", userDto.getUsername())
                .claim("roles", String.join(",", userDto.getRoles()))
                .compact();
    }

    @Override
    public UserDto validateJwtToken(String token) {
        Claims claims;
        String userInfoJson;
        try {
            if (token.startsWith("Bearer ")) {
                claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token.substring(7)).getPayload();
            } else {
                claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            }
            return UserDto.builder()
                    .userUid(String.valueOf(claims.get("userUid")))
                    .username((String) claims.get("username"))
                    .roles(
                            List.of(String.valueOf(claims.get("roles")).split(","))
                    )
                    .build();
        } catch (Exception e) {
            LOG.warn("[PARSE ACCESS TOKEN EXCEPTION] {}", e.getMessage());
            return null;
        }
    }


}
