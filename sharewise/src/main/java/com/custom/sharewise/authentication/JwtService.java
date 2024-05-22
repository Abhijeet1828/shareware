package com.custom.sharewise.authentication;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

	@Value("${security.jwt.secret-key}")
	private String secretKey;

	@Value("${security.jwt.expiration-time}")
	private long jwtExpiration;

	/**
	 * This method takes a JWT token as input and extracts the subject (usually the
	 * username) from the token’s claims. It uses the `extractClaim` method to
	 * extract the subject claim.
	 * 
	 * @param token
	 * @return
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * This is a generic method used to extract a specific claim from the JWT
	 * token’s claims. It takes a JWT token and a `Function` that specifies how to
	 * extract the desired claim (e.g., subject or expiration) and returns the
	 * extracted claim.
	 * 
	 * @param <T>
	 * @param token
	 * @param claimsResolver
	 * @return
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * This method is used to generate a JWT token. It takes a username as input,
	 * creates a set of claims (e.g., subject, issued-at, expiration), and then
	 * builds a JWT token using the claims and the signing key. The resulting token
	 * is returned.
	 * 
	 * @param userDetails
	 * @return
	 */
	public String generateToken(UserDetails userDetails) {
		return generateToken(new HashMap<>(), userDetails);
	}

	/**
	 * This method is used to generate a JWT token. It takes a username as input,
	 * creates a set of claims (e.g., subject, issued-at, expiration), and then
	 * builds a JWT token using the claims and the signing key. The resulting token
	 * is returned.
	 * 
	 * @param extraClaims
	 * @param userDetails
	 * @return
	 */
	public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
		return buildToken(extraClaims, userDetails, jwtExpiration);
	}

	/**
	 * Return the generalized expiration time for a token.
	 */
	public long getExpirationTime() {
		return jwtExpiration;
	}

	/**
	 * This method is responsible for creating the JWT token. It uses the `Jwts`
	 * builder to specify the claims, subject, issue date, expiration date, and the
	 * signing key. The token is then signed and compacted to produce the final JWT
	 * token, which is returned.
	 * 
	 * @param extraClaims
	 * @param userDetails
	 * @param expiration
	 * @return
	 */
	private String buildToken(Map<String, Object> extraClaims, UserDetails userDetails, long expiration) {
		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

	/**
	 * This method is used to validate a JWT token. It first extracts the username
	 * from the token and then checks whether it matches the username of the
	 * provided `UserDetails` object. Additionally, it verifies whether the token
	 * has expired. If the token is valid, it returns `true`; otherwise, it returns
	 * `false`.
	 * 
	 * @param token
	 * @param userDetails
	 * @return
	 */
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
	}

	/**
	 * This method checks whether a JWT token has expired by comparing the token’s
	 * expiration date (obtained using `extractExpiration`) to the current date. If
	 * the token has expired, it returns `true`; otherwise, it returns `false`.
	 * 
	 * @param token
	 * @return
	 */
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * This method extracts the expiration date from the JWT token’s claims. It’s
	 * used to determine whether the token has expired or not.
	 * 
	 * @param token
	 * @return
	 */
	private Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * This method parses the JWT token and extracts all of its claims. It uses the
	 * `Jwts` builder to create a parser that is configured with the appropriate
	 * signing key and then extracts the token’s claims.
	 * 
	 * @param token
	 * @return
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
	}

	/**
	 * This method is used to obtain the signing key for JWT token creation and
	 * validation. It decodes the `SECRET` key, which is typically a Base64-encoded
	 * key, and converts it into a cryptographic key using the `Keys.hmacShaKeyFor`
	 * method.
	 * 
	 * @return
	 */
	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
