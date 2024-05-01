package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.AccountVerificationToken;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Notification;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.AccountVerificationTokenRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.NotificationRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class ScheduledUser {

	private final AccountVerificationTokenRepository accountVerificationTokenRepository;
	private final NotificationRepository notificationRepository;

	public ScheduledUser(AccountVerificationTokenRepository accountVerificationTokenRepository, NotificationRepository notificationRepository) {
		this.accountVerificationTokenRepository = accountVerificationTokenRepository;
		this.notificationRepository = notificationRepository;
	}


	//@Scheduled(fixedRate = 10000)
	@Scheduled(cron = "0 0 0 * * *") // A minuit chaque jour
	public void removeExpiredVerificationTokens() {
		LocalDateTime now = LocalDateTime.now();
		LocalDate today = now.toLocalDate();
		LocalTime midnight = LocalTime.MIDNIGHT;
		LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);

		List<AccountVerificationToken> expiredTokens = accountVerificationTokenRepository.findByExpiryDateBefore(todayMidnight);
		accountVerificationTokenRepository.deleteAll(expiredTokens);
	}


	//@Scheduled(fixedRate = 10000)
	@Scheduled(fixedRate = 3600000)
	public void removeExpiredNotifications() {
		LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
		Date oneHourAgoDate = java.sql.Timestamp.valueOf(oneHourAgo);
		List<Notification> expiredNotifications = notificationRepository.findByDateCreatedBefore(oneHourAgoDate);
		notificationRepository.deleteAll(expiredNotifications);
	}
}
