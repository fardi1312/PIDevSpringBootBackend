package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;

import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

public interface IEmailService {
    void send(String to, String subject, String content);
    String buildResetPasswordMail(String token);
    String getMsgEmail(User user);
}
