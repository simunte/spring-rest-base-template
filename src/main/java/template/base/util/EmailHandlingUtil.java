package template.base.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

@Component
public class EmailHandlingUtil implements CommandLineRunner{

    @Autowired
    public JavaMailSender emailSender;

    public void sendSimpleMail(String recepient, String cc, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(new String[]{recepient});
        message.setCc(new String[]{cc});
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void sendMailWithAttachment(String recepient, String cc, String subject, String text, String pathToAttachment) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(new String[]{recepient});
        helper.setCc(new String[]{cc});
        helper.setSubject(subject);
        helper.setText(text);

        FileSystemResource file
                = new FileSystemResource(new File(pathToAttachment));
        helper.addAttachment("Invoice", file);
        emailSender.send(message);
    }

    @Override
    public void run(String... args) throws Exception {
//        this.sendSimpleMail("tommimunte210795@gmail.com", "tommi.munte@ebizcipta.com", "TEST MAIL", "Ini test mail");
    }
}
