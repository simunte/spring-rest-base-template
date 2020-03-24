package template.base.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpointAuthenticationFilter;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import template.base.configuration.properties.SecurityConfigProperties;
import template.base.repositories.UserRepository;
import template.base.services.RoleService;
import template.base.services.dto.RoleDetailDTO;
import template.base.services.dto.UserDetailDTO;
import template.base.services.mapper.UserMapper;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    private final SecurityConfigProperties securityConfigProperties;
    private final UserDetailsService userDetailsService;
    private final ClientDetailsService clientDetailsService;
    private final UserRepository userRepository;
    private final RoleService roleService;

    public WebSecurityConfig(SecurityConfigProperties securityConfigProperties, UserDetailsService userDetailsService, ClientDetailsService clientDetailsService, UserRepository userRepository, RoleService roleService) {
        this.securityConfigProperties = securityConfigProperties;
        this.userDetailsService = userDetailsService;
        this.clientDetailsService = clientDetailsService;
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    @Bean
    public OAuth2RequestFactory requestFactory() {
        CustomOauth2RequestFactory requestFactory = new CustomOauth2RequestFactory(clientDetailsService);
        requestFactory.setCheckUserScopes(true);

        return requestFactory;
    }

    @Bean
    BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    TokenStore tokenStore(){
        return new InMemoryTokenStore();
    }

    @Bean
    JwtAccessTokenConverter accessTokenConverter() {
//        final JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        final JwtAccessTokenConverter converter = new CustomTokenEnhancer();

        converter.setKeyPair(new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"),
                securityConfigProperties.getKeyStorePassword().toCharArray()).getKeyPair("jwt"));

        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices tokenServices
                = new DefaultTokenServices();
        tokenServices.setTokenStore(tokenStore());
        tokenServices.setSupportRefreshToken(true);
        return tokenServices;
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requiresChannel()
                .anyRequest()
                .requiresSecure()
            .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
                .httpBasic()
                .realmName(securityConfigProperties.getSecurityRealm())
            .and()
                .anonymous().disable()
                .csrf().disable()
                .authorizeRequests()
                .anyRequest().authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }
    @Bean
    public TokenEndpointAuthenticationFilter tokenEndpointAuthenticationFilter() {
        return new TokenEndpointAuthenticationFilter(authenticationManager, requestFactory());
    }

    class CustomTokenEnhancer extends JwtAccessTokenConverter {
        @Override
        public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
            template.base.domain.User user = userRepository.findOneByUsername(authentication.getName())
                    .orElseThrow(() -> new EntityNotFoundException("User Not Found"));
            Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());
            UserDetailDTO userDetailDTO = UserMapper.INSTANCE.toDtoDetail(user, new UserDetailDTO());
            userDetailDTO.setRoleDetailDTOS(
                    user.getRoles().stream().map(role -> {
                        Optional<RoleDetailDTO> roleDetailDTO = roleService.getDetailRole(role.getId());
                        return roleDetailDTO.get();
                    }).collect(Collectors.toCollection(LinkedList::new))
            );
            info.put("users", userDetailDTO);
            accessToken = super.enhance(accessToken, authentication);
            ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
            return accessToken;
        }
    }

    class CustomOauth2RequestFactory extends DefaultOAuth2RequestFactory {
        @Autowired
        private TokenStore tokenStore;

        public CustomOauth2RequestFactory(ClientDetailsService clientDetailsService) {
            super(clientDetailsService);
        }

        @Override
        public TokenRequest createTokenRequest(Map<String, String> requestParameters,
                                               ClientDetails authenticatedClient) {
            if (requestParameters.get("grant_type").equals("refresh_token")) {
                OAuth2Authentication authentication = tokenStore.readAuthenticationForRefreshToken(
                        tokenStore.readRefreshToken(requestParameters.get("refresh_token")));
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(authentication.getName(), null,
                                userDetailsService.loadUserByUsername(authentication.getName()).getAuthorities()));
            }
            return super.createTokenRequest(requestParameters, authenticatedClient);
        }
    }
}
