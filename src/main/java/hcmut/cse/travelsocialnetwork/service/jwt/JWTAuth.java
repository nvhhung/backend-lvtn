package hcmut.cse.travelsocialnetwork.service.jwt;

import hcmut.cse.travelsocialnetwork.factory.configuration.ENVConfig;
import hcmut.cse.travelsocialnetwork.model.LoginToken;
import hcmut.cse.travelsocialnetwork.utils.Base64Convert;
import hcmut.cse.travelsocialnetwork.utils.RandomUtils;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author : hung.nguyen23
 * @since : 8/19/22 Friday
 **/
@Component
public class JWTAuth {
    @Autowired
    JWTAuthProvider jwtAuthProvider;
    @Autowired
    private ENVConfig applicationConfig;
    @Autowired
    private RandomUtils randomUtils;

    private final long EXPIRED_TOKEN = 60 * 60 * 1000L;

    private String generateToken(JsonObject data) {
    return jwtAuthProvider.getJwtAuth().generateToken(data
                    .put("sub","hcmut")
                    .put("exp", System.currentTimeMillis() / EXPIRED_TOKEN) // after 60'
                    .put("iat", System.currentTimeMillis() / 1000)
            , new JWTOptions().setAlgorithm("RS256"));
    }

    public Optional<LoginToken> createLoginToken(JWTTokenData tokenData) throws Exception  {
        var tokenBase64 = Base64Convert.objectToBase64(tokenData);
        var accessToken = generateToken(new JsonObject()
                .put("userId", tokenData.getUserId())
                .put("role", tokenData.getRole())
                .put("softKey", randomUtils.randomString("mix", 16)));

        var refreshToken = applicationConfig.getStringProperty("application.auth.domain") +
                "/user/refresh_token?access_token=" +
                accessToken +
                "&refresh_token=" +
                URLEncoder.encode(tokenBase64, StandardCharsets.UTF_8.name());
        return Optional.of(LoginToken.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build());
    }
}
