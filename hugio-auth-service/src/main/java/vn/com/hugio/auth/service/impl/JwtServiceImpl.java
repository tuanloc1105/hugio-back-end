package vn.com.hugio.auth.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import vn.com.hugio.auth.dto.UserDto;
import vn.com.hugio.auth.service.JwtService;
import vn.com.hugio.common.log.LOG;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

@Component
public class JwtServiceImpl implements JwtService {

    @Value("${token.secret:jtgLdUg80hwnaklzvG6P9qoOit5yryHt}")
    private String secretKey;

    @Value("${token.time:432000}")
    private Integer time;

    @Override
    public String generateJWTToken(UserDto userDto) {

        Calendar now = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
        now.add(Calendar.SECOND, time);

        return Jwts.builder()
                .setSubject((userDto.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(now.getTimeInMillis()))
                .signWith(SignatureAlgorithm.HS512, secretKey)
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
                claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token.substring(7)).getBody();
            } else {
                claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
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
