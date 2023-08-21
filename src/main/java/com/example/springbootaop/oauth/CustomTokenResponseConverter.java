package com.example.springbootaop.oauth;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import java.util.Map;

public class CustomTokenResponseConverter implements Converter<Map<String, Object>, OAuth2AccessTokenResponse> {
    private DefaultMapOAuth2AccessTokenResponseConverter defaultMapOAuth2AccessTokenResponseConverter;

    public CustomTokenResponseConverter(DefaultMapOAuth2AccessTokenResponseConverter defaultMapOAuth2AccessTokenResponseConverter) {
        this.defaultMapOAuth2AccessTokenResponseConverter = defaultMapOAuth2AccessTokenResponseConverter;
    }

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, Object> source) {
        source.put(OAuth2ParameterNames.TOKEN_TYPE, OAuth2AccessToken.TokenType.BEARER.getValue());
        OAuth2AccessTokenResponse oAuth2AccessTokenResponse = defaultMapOAuth2AccessTokenResponseConverter.convert(source);
        return oAuth2AccessTokenResponse;
    }
}