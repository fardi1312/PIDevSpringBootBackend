package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailServiceAchref {
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private Environment environment;

    @Async
    public void notifyRequesterOfApproval(User requester, CollocationOffer collocationOffer) {
        String to = requester.getEmail();
        String subject = "Your collocation request has been approved";
        String content = buildApprovalEmailContent(requester, collocationOffer);
        sendEmail(to, subject, content);
    }
    @Async
    public void notifyRequesterOfApproval1(User requester, User offerer) {
        String to = requester.getEmail();
        String subject = "Your Renter has chosen a time";
        String content = buildApprovalEmailContent1(requester,offerer);
        sendEmail(to, subject, content);
    }
    @Async
    public void notifyRequesterOfApproval2(User requester, User offerer) {
        String to = requester.getEmail();
        String subject = "Your Renter has chosen a time";
        String content = buildApprovalEmailContent2(requester,offerer);
        sendEmail2(to, subject, content);
    }
    @Async
    public void notifyRequesterOfRefusal(User requester, User offerer) {
        String to = requester.getEmail();
        String subject = "Your Renter has chosen a time";
        String content = buildRefusalEmailContent(requester,offerer);
        sendEmail2(to, subject, content);
    }


   /* @Async
    public void notifyRequesterOfApprovalScheduler(User requester, CollocationOffer collocationOffer,User offerer) {
        String to = requester.getEmail();
        String subject = "Your Rent due reminder";
        String content = buildApprovalEmailContentScheduler(requester, collocationOffer,offerer);
        sendEmail(to, subject, content);
    }*/




    private String buildApprovalEmailContentScheduler(User requester,CollocationOffer collocationOffer,User offerer ) {
        String scheduleLink = "http://localhost:4200/schedule/calendar";
        String emailBodyHeader = "Dear " + requester.getFirstName() + ",";
        String emailBodyDetail = "please don't forget to pay your rent as discussed in the contract :) " +collocationOffer.getPrice()  + "\n";
        emailBodyDetail += "Contact Information:\n";
        emailBodyDetail += "Email: " + offerer.getEmail() + "\n";
        emailBodyDetail += "Phone: " + offerer.getPhoneNumber() + "\n";
        String buttonText = "View Schedule";
        String buttonHtml = "<a href=\"" + scheduleLink + "\"><button style=\"background-color: #4CAF50; /* Green */\n" +
                "border: none;\n" +
                "color: white;\n" +
                "padding: 15px 32px;\n" +
                "text-align: center;\n" +
                "text-decoration: none;\n" +
                "display: inline-block;\n" +
                "font-size: 16px;\n" +
                "margin: 4px 2px;\n" +
                "cursor: pointer;\">" + buttonText + "</button></a>";
        return emailBodyHeader + "\n\n" + emailBodyDetail + "\n\n" + buttonHtml;
    }


    /* private void createScheduledEmail(User requester, CollocationOffer collocationOffer, User offerer) {
         Date rentDate = collocationOffer.getDateRent();

         int dayOfMonth = rentDate.getDay();

         String cronExpression = "0 * * * * * " ;

         scheduleTask(cronExpression, () -> {
             notifyRequesterOfApprovalScheduler(requester, collocationOffer, offerer);
         });
     }*/
    public void scheduleMonthlyEmail(User requester, CollocationOffer collocationOffer, User offerer) {
    }


    private void scheduleTask(String cronExpression, Runnable task) {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.initialize();
        scheduler.schedule(task, new CronTrigger(cronExpression));
    }

    private void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
    private void sendEmail2(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }

    private String buildRefusalEmailContent(User requester, User offerer) {
        // Personalize the email content based on the requester's name
        String scheduleLink = "http://localhost:4200/schedule/calendar";
        String emailBodyHeader = "Dear " + requester.getFirstName() + ",";
        String emailBodyDetail = "We are sorry to inform you that " + offerer.getFirstName() + " " + offerer.getLastName() + "has declined being your roomie.\n\n";
        emailBodyDetail += "Contact Information:\n";
        emailBodyDetail += "Email: " + offerer.getEmail() + "\n";
        emailBodyDetail += "Phone: " + offerer.getPhoneNumber() + "\n";
        String buttonText = "View Schedule";
        String buttonHtml = "<a href=\"" + scheduleLink + "\"><button style=\"background-color: #4CAF50; /* Green */\n" +
                "border: none;\n" +
                "color: white;\n" +
                "padding: 15px 32px;\n" +
                "text-align: center;\n" +
                "text-decoration: none;\n" +
                "display: inline-block;\n" +
                "font-size: 16px;\n" +
                "margin: 4px 2px;\n" +
                "cursor: pointer;\">" + buttonText + "</button></a>";
        return emailBodyHeader + "\n\n" + emailBodyDetail + "\n\n" + buttonHtml;
    }


    private String buildApprovalEmailContent2(User requester, User offerer) {
        // Personalize the email content based on the requester's name
        String scheduleLink = "http://localhost:4200/schedule/calendar";
        String emailBodyHeader = "Dear " + requester.getFirstName() + ",";
        String emailBodyDetail = "We are pleased to inform you that " + offerer.getFirstName() + " " + offerer.getLastName() + ",is officially your next roomie.\n\n";
        emailBodyDetail += "Contact Information:\n";
        emailBodyDetail += "Email: " + offerer.getEmail() + "\n";
        emailBodyDetail += "Phone: " + offerer.getPhoneNumber() + "\n";
        String buttonText = "View Schedule";
        String buttonHtml = "<a href=\"" + scheduleLink + "\"><button style=\"background-color: #4CAF50; /* Green */\n" +
                "border: none;\n" +
                "color: white;\n" +
                "padding: 15px 32px;\n" +
                "text-align: center;\n" +
                "text-decoration: none;\n" +
                "display: inline-block;\n" +
                "font-size: 16px;\n" +
                "margin: 4px 2px;\n" +
                "cursor: pointer;\">" + buttonText + "</button></a>";
        return emailBodyHeader + "\n\n" + emailBodyDetail + "\n\n" + buttonHtml;
    }






    private String buildApprovalEmailContent1(User requester, User offerer) {
        // Personalize the email content based on the requester's name
        String scheduleLink = "http://localhost:4200/schedule/calendar";
        String emailBodyHeader = "Dear " + requester.getFirstName() + ",";
        String emailBodyDetail = "We are pleased to inform you that your probable next roomie, " + offerer.getFirstName() + " " + offerer.getLastName() + ", has chosen a time for a meeting! You can now accept or refuse it.\n\n";
        emailBodyDetail += "Contact Information:\n";
        emailBodyDetail += "Email: " + offerer.getEmail() + "\n";
        emailBodyDetail += "Phone: " + offerer.getPhoneNumber() + "\n";
        String buttonText = "View Schedule";
        String buttonHtml = "<a href=\"" + scheduleLink + "\"><button style=\"background-color: #4CAF50; /* Green */\n" +
                "border: none;\n" +
                "color: white;\n" +
                "padding: 15px 32px;\n" +
                "text-align: center;\n" +
                "text-decoration: none;\n" +
                "display: inline-block;\n" +
                "font-size: 16px;\n" +
                "margin: 4px 2px;\n" +
                "cursor: pointer;\">" + buttonText + "</button></a>";
        return emailBodyHeader + "\n\n" + emailBodyDetail + "\n\n" + buttonHtml;
    }

    private String buildApprovalEmailContent(User requester, CollocationOffer collocationOffer) {
        // Personalize the email content based on the requester's name
        String scheduleLink = "http://localhost:4200/schedule/calendar";
        String emailBodyHeader = "Dear " + requester.getFirstName() + ",";
        String emailBodyDetail = "We are pleased to inform you that your collocation request has been approved! You can now adjust your schedule to meet the offerer.";
        String buttonText = "View Schedule";
        String buttonHtml = "<a href=\"" + scheduleLink + "\"><button style=\"background-color: #4CAF50; /* Green */\n" +
                "border: none;\n" +
                "color: white;\n" +
                "padding: 15px 32px;\n" +
                "text-align: center;\n" +
                "text-decoration: none;\n" +
                "display: inline-block;\n" +
                "font-size: 16px;\n" +
                "margin: 4px 2px;\n" +
                "cursor: pointer;\">" + buttonText + "</button></a>";
        return buildEmailBody(scheduleLink, emailBodyHeader, emailBodyDetail, buttonText);
    }


    private String buildEmailBody(String url, String emailBodyHeader, String emailBodyDetail, String buttonText) {
        return "<div style=\"margin: 0 auto; width: 500px; text-align: center; background: #ffffff; border-radius: 5px; border: 3px solid #838383;\">" +
                "<h2 style=\"background: #838383; padding: 15px; margin: 0; font-weight: 700; font-size: 24px; color: #ffffff;\">" + emailBodyHeader + "</h2>" +
                "<p style=\"padding: 20px; font-size: 20px; color: #202020;\">" + emailBodyDetail + "</p>" +
                "<a style=\"display: inline-block; padding: 10px 20px; margin-bottom: 30px; text-decoration: none; background: #3f51b5; font-size: 16px; border-radius: 3px; color: #ffffff;\" href=\"" + url + "\">" + buttonText + "</a>" +
                "</div>";
    }


}
