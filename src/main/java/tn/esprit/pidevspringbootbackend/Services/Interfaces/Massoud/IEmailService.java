package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;

import org.springframework.scheduling.annotation.Async;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

public interface IEmailService {
    void send(String to, String subject, String content);
    String buildResetPasswordMail(String token);
    String getMsgEmail(User user);
    @Async
    void sendResetPasswordEmail(String to, String newPassword);

}
