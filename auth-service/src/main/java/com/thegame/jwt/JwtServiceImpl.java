package com.thegame.jwt;

import com.thegame.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@RequiredArgsConstructor
class JwtServiceImpl implements JwtService {

    private final TokenEncryption tokenEncryption;
    private final JwtConfig jwtConfig;


    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        return extractClaim(token, claims -> {
            List<?> rolesList = claims.get("roles", List.class);
            return rolesList.stream()
                    .map(roleMap -> {
                        if (roleMap instanceof Map<?, ?> roleDetails) {
                            return new SimpleGrantedAuthority((String) roleDetails.get("authority"));
                        }
                        throw new IllegalArgumentException("Invalid role format in token");
                    })
                    .toList();
        });
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateAccessToken(UserDetails userDetails) {
        AppUser user = (AppUser) userDetails;
        Map<String, Object> userClaims = Map.of("roles", userDetails.getAuthorities(), "userId", user.getId(), "email", user.getEmail());
        return buildToken(userClaims, userDetails, jwtConfig.getJwtExpiration());
    }

    public Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("userId", Long.class);
    }

    public String extractUserEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("email", String.class);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        AppUser user = (AppUser) userDetails;
        Map<String, Object> userClaims = Map.of("roles", userDetails.getAuthorities(), "userId", user.getId(), "email", user.getEmail());
        return buildToken(userClaims, userDetails, jwtConfig.getRefreshExpiration());
    }

    private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

//    private void saveUserToken(AppUser user, String jwtToken) {
//        var token = Token.builder()
//                .token(jwtToken)
//                .tokenType(TokenType.BEARER)
//                .expired(false)
//                .revoked(false)
//                .userId(user.getId())
//                .build();
//        tokenRepository.save(token);
//    }

//    private void revokeAllUserTokens(AppUser user) {
//        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
//        if (validUserTokens.isEmpty())
//            return;
//        validUserTokens.forEach(token -> {
//            token.setExpired(true);
//            token.setRevoked(true);
//        });
//        tokenRepository.saveAll(validUserTokens);
//    }

//    String refreshToken(String refreshToken, HttpServletResponse response) {
//
//        final String username = extractUsername(refreshToken);
//
//        if (username != null) {
//            AppUser user = userRepository.findByUserName(username).orElseThrow();
//            return setAuthenticationResponse(user, response);
//        } else {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        }
//        return null;
//    }

    public TokenResponse authenticate(AppUser user, HttpServletResponse response) {
        return setAuthenticationResponse(user, response);
    }

    private TokenResponse setAuthenticationResponse(AppUser user, HttpServletResponse response) {
        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        String encryptedAccessToken;
        String encryptedRefreshToken;

        encryptedAccessToken = encryptToken(accessToken);
        encryptedRefreshToken = encryptToken(refreshToken);

//        revokeAllUserTokens(user);
//        saveUserToken(user, encryptedRefreshToken);

        HttpHeaders httpHeaders = buildHttpTokenHeaders(encryptedAccessToken, encryptedRefreshToken, jwtConfig.getJwtExpiration(), jwtConfig.getRefreshExpiration());
        applyHttpHeaders(response, httpHeaders);
        response.setStatus(HttpServletResponse.SC_OK);
        return new TokenResponse(encryptedAccessToken, encryptedRefreshToken);
    }

    private String encryptToken(String token) {
        return tokenEncryption.encrypt(token);
    }

    public String decryptToken(String token) {
        return tokenEncryption.decrypt(token);
    }


    public void applyHttpHeaders(HttpServletResponse response, HttpHeaders httpHeaders) {
        httpHeaders.forEach((headerName, headerValues) ->
                headerValues.forEach(value ->
                        response.addHeader(headerName, value)
                )
        );
    }

    HttpHeaders buildHttpTokenHeaders(String accessToken, String refreshToken, long jwtExpiration, long refreshExpiration) {

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtExpiration / 1000)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshExpiration / 7 * 24 * 60 * 60)
                .build();
        return getHttpHeaders(accessTokenCookie, refreshTokenCookie);
    }

    private HttpHeaders getHttpHeaders(ResponseCookie accessTokenCookie, ResponseCookie refreshTokenCookie) {
        HttpHeaders headers = new HttpHeaders();

        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return headers;
    }
}
