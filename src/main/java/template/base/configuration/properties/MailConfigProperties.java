package template.base.configuration.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mail")
public class MailConfigProperties {
    private String host;
    private String username;
    private String password;
    private Properties properties;

    @Data
    public static class Properties {
        private String protocol;
        private Integer port;
        private Boolean auth;
        private Starttls starttls;

        @Data
        public static class Starttls {
            private Boolean enable;
            private Boolean required;
        }
    }
}
