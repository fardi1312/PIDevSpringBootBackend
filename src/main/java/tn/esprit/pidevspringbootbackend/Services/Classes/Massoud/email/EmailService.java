package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.email;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IEmailService;
@Service
@Transactional
@RequiredArgsConstructor
public class EmailService implements IEmailService {
    @Autowired
    private  JavaMailSender mailSender;
    @Autowired
    private  Environment environment;
    @Override @Async
    public void send(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");
            messageHelper.setText(content, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email");
        }
    }
    @Override
    public String buildResetPasswordMail(String token) {
        String url = environment.getProperty("app.root.frontend") + "/reset-password/" + token;
        return buildEmailBody(
                url,
                "Reset Your Password",
                "Please, click on the link below to get a new password.",
                "Get New Password"
        );
    }
    private String buildEmailBody(String url, String emailBodyHeader, String emailBodyDetail, String buttonText) {
        return "<div style=\"margin: 0 auto; width: 500px; text-align: center; background: #ffffff; border-radius: 5px; border: 3px solid #838383;\">" +
                    "<h2 style=\"background: #838383; padding: 15px; margin: 0; font-weight: 700; font-size: 24px; color: #ffffff;\">" + emailBodyHeader + "</h2>" +
                    "<p style=\"padding: 20px; font-size: 20px; color: #202020;\">" + emailBodyDetail + "</p>" +
                    "<a style=\"display: inline-block; padding: 10px 20px; margin-bottom: 30px; text-decoration: none; background: #3f51b5; font-size: 16px; border-radius: 3px; color: #ffffff;\" href=\" " + url + " \">" + buttonText + "</a>" +
                "</div>";
    }
    public String getMsgEmail(User user) {
        String verificationLink = "http://localhost:8083/verify-email?email=" + user.getEmail();
        String buttonHtml = "<a href=\"" + verificationLink + "\"><button style=\"background-color: #4CAF50; /* Green */\n" +
                "border: none;\n" +
                "color: white;\n" +
                "padding: 15px 32px;\n" +
                "text-align: center;\n" +
                "text-decoration: none;\n" +
                "display: inline-block;\n" +
                "font-size: 16px;\n" +
                "margin: 4px 2px;\n" +
                "cursor: pointer;\">Verify Email</button></a>";
        return "Please click the button below to verify your email address:<br>" + buttonHtml;
    }

    @Override
    @Async
    public void sendResetPasswordEmail(String to, String newPassword) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setTo(to);
            messageHelper.setSubject("Password Reset Request");
            messageHelper.setText("Your new password is: " + newPassword);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send password reset email", e);
        }
    }



}
