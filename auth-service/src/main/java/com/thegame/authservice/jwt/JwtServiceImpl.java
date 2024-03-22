package com.thegame.authservice.jwt;

import com.thegame.AppUser;
import com.thegame.dto.AuthenticationUserObject;
import com.thegame.dto.RefreshTokenAuthResponse;
import com.thegame.mapper.UserMapper;
import com.thegame.model.Role;
import com.thegame.response.LogoutResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@RequiredArgsConstructor
class JwtServiceImpl implements JwtService {

    public static final String USER_ID = "userId";
    public static final String EMAIL = "email";
    public static final String ROLES = "roles";

    private final TokenEncryption tokenEncryption;
    private final JwtConfig jwtConfig;

    @Override
    public AuthenticationResponse authenticate(AppUser user, HttpServletResponse response) {
        return setAuthenticationResponse(user, response);
    }

    @Override
    public AuthenticationUserObject authenticateToken(String token) {
        String decryptedAccessToken = tokenEncryption.decrypt(token);
        Collection<? extends GrantedAuthority> authorities = extractAuthorities(decryptedAccessToken);
        Long userId = extractUserId(decryptedAccessToken);
        String userEmail = extractUserEmail(decryptedAccessToken);
        String username = extractUsername(decryptedAccessToken);
        Role userRole = extractUserRole(authorities);
        Date accessTokenExpiration = extractExpiration(decryptedAccessToken);

        return AuthenticationUserObject.builder()
                .username(username)
                .id(userId)
                .email(userEmail)
                .role(userRole)
                .accessTokenExpiration(accessTokenExpiration)
                .build();
    }

    @Override
    public RefreshTokenAuthResponse authenticateRefreshToken(String refreshToken, HttpServletResponse response) {
        AuthenticationUserObject authenticationUserObject = authenticateToken(refreshToken);
        AppUser user = UserMapper.mapToUserEntity(authenticationUserObject);
        return setRefreshTokenAuthenticationResponse(user, response);
    }

    @Override
    public LogoutResponse logout(HttpServletResponse response) {
        HttpHeaders httpHeaders = buildHttpTokenHeaders("", "", 0, 0);
        applyHttpHeaders(response, httpHeaders);
        return new LogoutResponse("/login?logout=true","SUCCESSFULLY_LOGGED_OUT");
    }

    private RefreshTokenAuthResponse setRefreshTokenAuthenticationResponse(AppUser user, HttpServletResponse response) {
        TokenHeaders result = buildAndGetTokenHeaders(user);
        applyHttpHeaders(response,result.httpHeaders);
        AuthenticationUserObject authenticationUserObject = UserMapper.mapUserEntityToAuthObject(user);
        return new RefreshTokenAuthResponse(authenticationUserObject,result.httpHeaders);
    }

    private AuthenticationResponse setAuthenticationResponse(AppUser user, HttpServletResponse response) {
        TokenHeaders result = buildAndGetTokenHeaders(user);
        //        revokeAllUserTokens(user);
        //        saveUserToken(user, encryptedRefreshToken);
        applyHttpHeaders(response, result.httpHeaders());
        response.setStatus(HttpServletResponse.SC_OK);
        return new AuthenticationResponse(result.encryptedAccessToken(), result.encryptedRefreshToken());
    }

    private TokenHeaders buildAndGetTokenHeaders(AppUser user) {
        var accessToken = generateAccessToken(user);
        var refreshToken = generateRefreshToken(user);

        String encryptedAccessToken;
        String encryptedRefreshToken;

        encryptedAccessToken = encryptToken(accessToken);
        encryptedRefreshToken = encryptToken(refreshToken);

        HttpHeaders httpHeaders = buildHttpTokenHeaders(encryptedAccessToken, encryptedRefreshToken, jwtConfig.getJwtExpiration(), jwtConfig.getRefreshTokenExpiration());
        return new TokenHeaders(encryptedAccessToken, encryptedRefreshToken, httpHeaders);
    }

    private Role extractUserRole(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .map(authorityName -> switch (authorityName) {
                    case "ROLE_AWAITING_DETAILS" -> Role.ROLE_AWAITING_DETAILS;
                    case "ROLE_MONITORING" -> Role.ROLE_MONITORING;
                    case "ROLE_ADMIN" -> Role.ROLE_ADMIN;
                    case "ROLE_USER" -> Role.ROLE_USER;
                    default -> null;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }


    private String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Collection<? extends GrantedAuthority> extractAuthorities(String token) {
        return extractClaim(token, claims -> {
            List<?> rolesList = claims.get(ROLES, List.class);
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

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private String generateAccessToken(UserDetails userDetails) {
        AppUser user = (AppUser) userDetails;
        Map<String, Object> userClaims = Map.of(ROLES, userDetails.getAuthorities(), USER_ID, user.getId(), EMAIL, user.getEmail());
        return buildToken(userClaims, userDetails, jwtConfig.getJwtExpiration());
    }

    private Long extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(USER_ID, Long.class);
    }

    private String extractUserEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get(EMAIL, String.class);
    }

    private String generateRefreshToken(UserDetails userDetails) {
        AppUser user = (AppUser) userDetails;
        Map<String, Object> userClaims = Map.of(ROLES, userDetails.getAuthorities(), USER_ID, user.getId(), EMAIL, user.getEmail());
        return buildToken(userClaims, userDetails, jwtConfig.getRefreshTokenExpiration());
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
        byte[] keyBytes = jwtConfig.getSecretKey();
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

    private HttpHeaders buildHttpTokenHeaders(String accessToken, String refreshToken, long jwtExpiration, long refreshExpiration) {

        ResponseCookie accessTokenCookie = ResponseCookie.from("accessToken", accessToken)
                .httpOnly(true)
                .secure(false)
//                .domain("localhost")
//                .sameSite("None")
                .path("/")
                .maxAge(jwtExpiration / 1000)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
//                .domain("localhost")
//                .sameSite("None")
                .path("/")
                .maxAge(refreshExpiration / 1000)
                .build();
        return getHttpHeaders(accessTokenCookie, refreshTokenCookie);
    }

    private HttpHeaders getHttpHeaders(ResponseCookie accessTokenCookie, ResponseCookie refreshTokenCookie) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE, accessTokenCookie.toString());
        headers.add(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString());
        return headers;
    }

    private record TokenHeaders(String encryptedAccessToken, String encryptedRefreshToken, HttpHeaders httpHeaders) {
    }
}
