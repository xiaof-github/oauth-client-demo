package com.example.springbootaop.config;

import com.example.springbootaop.oauth.CustomAuthorizationCodeTokenResponseClient;
import com.example.springbootaop.oauth.CustomAuthorizationRequestResolver;
import com.example.springbootaop.oauth.CustomTokenResponseConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.endpoint.DefaultMapOAuth2AccessTokenResponseConverter;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class Oauth2LoginConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .oauth2Client(oauth2 -> oauth2.authorizationCodeGrant(codeGrant -> codeGrant
                        .accessTokenResponseClient(this.accessTokenResponseClient())
                        // 插入授权请求定制的类。我们在授权请求中添加了额外的参数language=en
                        .authorizationRequestResolver(new CustomAuthorizationRequestResolver(
                                clientRegistrationRepository(),
                                OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI))));
//                                "/login/oauth2/code/"))));
        // @formatter:on
        return http.build();
    }

    @Bean
    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient(){
        CustomAuthorizationCodeTokenResponseClient accessTokenResponseClient =
                new CustomAuthorizationCodeTokenResponseClient();

        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
                new OAuth2AccessTokenResponseHttpMessageConverter();
        tokenResponseHttpMessageConverter.setAccessTokenResponseConverter(new CustomTokenResponseConverter(new DefaultMapOAuth2AccessTokenResponseConverter()));
        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter));
        restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

        accessTokenResponseClient.setRestOperations(restTemplate);
        return accessTokenResponseClient;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // @formatter:off
        UserDetails userDetails = User.withDefaultPasswordEncoder()
                .username("zhouzhou")
                .password("123")
                .roles("ADMIN")
                .build();
        // @formatter:on
        return new InMemoryUserDetailsManager(userDetails);
    }

    private ClientRegistration buildGiteeClientRegistration() {
        // @formatter:off
//        ClientRegistration weiBoClientRegistration = ClientRegistration
//                .withRegistrationId("weibo") // 唯一标识ClientRegistration的ID
//                .clientName("weibo") // 客户端名称
//                .clientId("<YOUR CLIENT_ID>") // 客户端ID
//                .clientSecret("<YOUR CLIENT_SECRET>") //客户端密钥
//                .scope(new String[]{"statuses/home_timeline","account/profile/email","emotions"})//客户端在授权请求中请求的授权范围
//                .redirectUri("http://127.0.0.1:8080/callback") // 重定向URI
//                .authorizationUri("https://api.weibo.com/oauth2/authorize") // 授权服务器授权地址，这里是新浪微博的授权地址
//                .tokenUri("https://api.weibo.com/oauth2/access_token") // 授权服务器令牌请求地址，这里是新浪微博的令牌请求地址
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // 声明授权类型是授权码类型
//                .build();
          ClientRegistration giteeClientRegistration = ClientRegistration
                .withRegistrationId("gitee") // 唯一标识ClientRegistration的ID
                .clientName("gitee") // 客户端名称
                .clientId("f6c8cf4219b00a12dcb9696abbaaa4f7ef60b6219f38ecfa0b793cc0a7bff661") // 客户端ID
                .clientSecret("4b704364a33a410d279b48a401922ed341e210f9cd9276815c5f7490d8cd1688") //客户端密钥
                .scope(new String[]{"user_info","projects"})//客户端在授权请求中请求的授权范围
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/gitee") // 重定向URI
                .authorizationUri("https://gitee.com/oauth/authorize") // 授权服务器授权地址，这里是新浪微博的授权地址
                .tokenUri("https://gitee.com/oauth/token") // 授权服务器令牌请求地址，这里是新浪微博的令牌请求地址
                .userInfoUri("https://gitee.com/api/v5/user")
                .userNameAttributeName("name")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE) // 声明授权类型是授权码类型
                .build();
        // @formatter:on
        return giteeClientRegistration;
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        ClientRegistration clientRegistration = this.buildGiteeClientRegistration();
        return new InMemoryClientRegistrationRepository(clientRegistration);
    }
}
