package com.airbnb.service;

import com.airbnb.entity.PropertyUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
 @Value("${jwt.algorithm.key}")
 private String algorithmKey;//this is the key which is applied while generating the token on the alogrithm.
 @Value("${jwt.issuer}")
 private String issuer;//it is used to verify the token is genereted by the right backend code ,so that noone can create a dumy token and try to log
@Value("${jwt.expiry.duration}")
 private int expiryTime;

private Algorithm algorithm;

private final static String USER_NAME="username";



 @PostConstruct
 public void postConstruct(){
 algorithm=algorithm.HMAC256(algorithmKey);
}
public String generateTokan(PropertyUser propertyUser){
  return JWT.create().withClaim(USER_NAME,propertyUser.getUsername())
         .withExpiresAt(new Date(System.currentTimeMillis()+expiryTime))
         .withIssuer(issuer)
         .sign(algorithm);
 }//in these the jwt token having the username
 public String getUsername(String token) {
  DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);//here verify the token extract the username and returns back
  return decodedJWT.getClaim(USER_NAME).asString();
 }
}
