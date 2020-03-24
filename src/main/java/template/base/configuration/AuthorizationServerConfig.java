package template.base.configuration;

import template.base.configuration.properties.SecurityConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAuthorizationServer
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String REFRESH_TOKEN = "refresh_token";

    private final SecurityConfigProperties securityConfigProperties;
    private final JwtAccessTokenConverter jwtAccessTokenConverter;
    private final TokenStore tokenStore;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthorizationServerConfig(SecurityConfigProperties securityConfigProperties,
                                     JwtAccessTokenConverter jwtAccessTokenConverter,
                                     TokenStore tokenStore,
                                     AuthenticationManager authenticationManager,
                                     PasswordEncoder passwordEncoder) {
        this.securityConfigProperties = securityConfigProperties;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
        this.tokenStore = tokenStore;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(securityConfigProperties.getJwt().getClientId())
                .secret(passwordEncoder.encode(securityConfigProperties.getJwt().getClientSecret()))
                .authorizedGrantTypes(securityConfigProperties.getJwt().getGrantType(), REFRESH_TOKEN)
                .resourceIds(securityConfigProperties.getJwt().getResourceIds())
                .scopes(securityConfigProperties.getJwt().getScopeRead(), securityConfigProperties.getJwt().getScopeWrite())
                .accessTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(securityConfigProperties.getJwt().getAccessTokenValidity()))
                .refreshTokenValiditySeconds((int) TimeUnit.HOURS.toSeconds(securityConfigProperties.getJwt().getRefreshTokenValidity()));
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtAccessTokenConverter));

        endpoints.tokenStore(tokenStore)
                .authenticationManager(authenticationManager)
                .tokenEnhancer(tokenEnhancerChain);

    }
}