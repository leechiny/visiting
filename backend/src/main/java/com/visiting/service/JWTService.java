package com.visiting.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Date;

@Service
public class JWTService {

  private RSAPrivateKey privateKey;
  private RSAPublicKey publicKey;
  private long expirationTime = 1800000;

  @PostConstruct
  private void init() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    keyPairGenerator.initialize(2048);
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    privateKey = (RSAPrivateKey) keyPair.getPrivate();
    publicKey = (RSAPublicKey) keyPair.getPublic();
  }

  public String generateToken(String name, String role) {
     return JWT.create()
            .withClaim("name", name)
            .withClaim("role", role)
            .withExpiresAt( new Date(System.currentTimeMillis() + expirationTime))
            .sign(Algorithm.RSA256(publicKey, privateKey));
  }

  public String verifyToken(String token) throws JWTVerificationException {
    String encodedPayload = JWT.require(Algorithm.RSA256(publicKey,privateKey))
      .build()
      .verify(token)
      .getPayload();

    return new String(Base64.getDecoder().decode(encodedPayload));
  }
}
