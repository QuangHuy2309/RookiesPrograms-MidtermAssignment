package com.nashtech.MyBikeShop.security.JWT;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.nashtech.MyBikeShop.security.services.UserDetailsImpl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtUtils {
	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
	private static List<String> jwtBlackList = new ArrayList<String>();

	@Value("${rookies.app.jwtSecret}")
	private String jwtSecret;

	@Value("${rookies.app.jwtExpirationMs}")
	private int jwtExpirationMs;

	public String generateJwtToken(Authentication authentication) {

		UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
		
		return Jwts.builder().setSubject((String.valueOf(userPrincipal.getId()))).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public String getUserNameFromJwtToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (SignatureException e) {
			logger.error("Invalid JWT signature: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}
		return false;
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
	}

	public String refreshToken(String token) {
		final Date createdDate = new Date();
		final Date expirationDate = new Date(createdDate.getTime() + jwtExpirationMs * 1000);
		final Claims claims = getAllClaimsFromToken(token);
		claims.setIssuedAt(createdDate);
		claims.setExpiration(expirationDate);
		return Jwts.builder().setClaims(claims).signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}
	public boolean addToBlackList(String jwtToken) throws Exception {
        if (isInBlacklist(jwtSecret)){
            return false;
        }
        jwtBlackList.add(jwtToken);
        return true;
    }

    public boolean isInBlacklist(String jwtSecret) throws Exception {
        if (jwtBlackList.contains(jwtSecret)){
            throw new Exception("JWT Token is in blacklist");
        }
        return false;
    }
    public Boolean isExpire(String jwt){
        Date expiration = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(jwt).getBody().getExpiration();
        Date now = new Date();
        if(expiration.before(now))
            return true;
        return false;
    }

    @Scheduled(cron="0 0 1 * *")
    public void deleteExpireToken(){
        for (String jwt : jwtBlackList) {
            if(isExpire(jwt))
                jwtBlackList.remove(jwt);
        }
    }
}
