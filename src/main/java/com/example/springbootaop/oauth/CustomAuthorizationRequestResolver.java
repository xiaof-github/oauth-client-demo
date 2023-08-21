package com.example.springbootaop.oauth;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class CustomAuthorizationRequestResolver
        implements OAuth2AuthorizationRequestResolver {

    private OAuth2AuthorizationRequestResolver resolver;

    public CustomAuthorizationRequestResolver(
            ClientRegistrationRepository repo, String authorizationRequestBaseUri) {
        resolver = new CustomOauth2AuthorizationRequestResolver(repo, authorizationRequestBaseUri);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        OAuth2AuthorizationRequest req = resolver.resolve(request);
        if (req != null) {
            //req = customizeAuthorizationRequest(req);
        }
        return req;
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String clientRegistrationId) {
        OAuth2AuthorizationRequest req = resolver.resolve(request, clientRegistrationId);

        return req;
    }


}
