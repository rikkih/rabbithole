package com.hoderick.rabbithole.config;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.JwkProviderBuilder;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Auth0HandshakeHandler extends DefaultHandshakeHandler {

    private final JwkProvider jwkProvider;
    private final String issuer;
    private final String audience;

    public Auth0HandshakeHandler(String issuer, String audience) {
        this.issuer = issuer;
        this.audience = audience;

        this.jwkProvider = new JwkProviderBuilder(issuer)
                .cached(10, 24, TimeUnit.HOURS)
                .rateLimited(10, 1, TimeUnit.MINUTES)
                .build();
    }

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler, Map<String, Object> attributes) {

        String query = request.getURI().getQuery();
        if (query == null || !query.contains("token=")) {
            throw new RuntimeException("Missing token in WebSocket connection");
        }

        String token = null;
        for (String param : query.split("&")) {
            if (param.startsWith("token=")) {
                token = param.substring("token=".length());
                break;
            }
        }

        if (token == null) {
            throw new RuntimeException("Token not found in WebSocket query");
        }

        try {
            DecodedJWT jwt = JWT.decode(token);
            Jwk jwk = jwkProvider.get(jwt.getKeyId());

            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(issuer)
                    .withAudience(audience)
                    .build();

            DecodedJWT verifiedJWT = verifier.verify(token);

            return verifiedJWT::getSubject; // returns Principal.getName()

        } catch (Exception e) {
            throw new RuntimeException("JWT verification failed: " + e.getMessage(), e);
        }
    }

}