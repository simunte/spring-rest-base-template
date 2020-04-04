package template.base.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import template.base.configuration.properties.MailConfigProperties;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(MailConfigProperties.class)
public class MailConfiguration {
    private final MailConfigProperties mailConfigProperties;

    public MailConfiguration(MailConfigProperties mailConfigProperties) {
        this.mailConfigProperties = mailConfigProperties;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailConfigProperties.getHost());
        mailSender.setPort(mailConfigProperties.getProperties().getPort());

        mailSender.setUsername(mailConfigProperties.getUsername());
        mailSender.setPassword(mailConfigProperties.getPassword());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", mailConfigProperties.getProperties().getProtocol());
        props.put("mail.smtp.auth", mailConfigProperties.getProperties().getAuth());
        props.put("mail.smtp.starttls.enable", mailConfigProperties.getProperties().getStarttls().getEnable());
        props.put("mail.debug", "true");

        return mailSender;
    }
}
