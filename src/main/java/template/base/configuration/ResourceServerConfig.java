package template.base.configuration;

import template.base.configuration.properties.SecurityConfigProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration
@EnableResourceServer
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private final SecurityConfigProperties securityConfigProperties;
    private final TokenStore tokenStore;

    public ResourceServerConfig(SecurityConfigProperties securityConfigProperties, TokenStore tokenStore) {
        this.securityConfigProperties = securityConfigProperties;
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(securityConfigProperties.getJwt().getResourceIds())
                .stateless(true)
                .tokenStore(tokenStore);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .cors().disable()
            .anonymous().disable()
            .authorizeRequests()
                .antMatchers("/api/**").authenticated()
            .and()
            .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }
}
