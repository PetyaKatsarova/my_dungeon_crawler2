package nl.hva.hvacrawler.util.security.token;

import java.util.Base64;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.*;
import nl.hva.hvacrawler.business.domain.User;

public class JWTToken {

    private static final String SECRET_KEY = "BWQvoCDvFYyJEXc4ecRf7G-hKS10-CgMp4IIemkZN3v66evDAGC3TdSxSADS2dGtNbjb23U6fTEqzxBVaEwHEg5tLHg7G50vPAEqg9x1iYKeXk6k2Y6leFb31bjLrPiB_xf7nrTLztyxqgi10EylUJ5sFan7e0ZV-H_UV0t-";
    private static final String ISSUER = "Hva Crawler";
    private static final long EXPIRATIONMILLIS = 86400000; // De token verloopt over 1 dag


    public String generateJWTToken(String email) throws JWTCreationException{
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            return JWT.create()
                    .withSubject(email)
                    .withIssuer(ISSUER)
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATIONMILLIS))
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String verifyJWTToken(String token, User user) throws JWTVerificationException {
        if (token == null){
            return "No token";
        }
        String bearer = token.substring(0, 7);
        if (!bearer.equals("Bearer ")) {
            return "Invalid token";
        }

        String[] tokenParts = token.substring(7).split("\\.");

        if (tokenParts.length != 3) {
            return "Invalid token format";
        }

        String header = new String(Base64.getUrlDecoder().decode(tokenParts[0]));
        String payload = new String(Base64.getUrlDecoder().decode(tokenParts[1]));

        // Check header for "alg" and "typ"
        if (!header.contains("\"alg\":\"HS256\"") || !header.contains("\"typ\":\"JWT\"")) {
            return "Invalid token header";
        }

        // Parse payload as JSON and extract claims
        JsonObject payloadJson = new JsonParser().parse(payload).getAsJsonObject();
        String email = payloadJson.get("sub").getAsString();
        if(!email.equals(user.getEmail())){
            return "Invalid user";
        }
        String issuer = payloadJson.get("iss").getAsString();
        if (!issuer.equals(ISSUER)) {
            return "Invalid token issuer";
        }

        // Check expiration time
        long expirationTime = payloadJson.get("exp").getAsLong();
        long currentTime = System.currentTimeMillis() / 1000; // Convert to seconds
        if (expirationTime < currentTime) {
            return "Token has expired";
        }

        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .build();
            verifier.verify(token.substring(7));
            return "Valid token";
        } catch (JWTVerificationException e) {
            e.printStackTrace();
            return "Token verification failed";
        }
    }
}
