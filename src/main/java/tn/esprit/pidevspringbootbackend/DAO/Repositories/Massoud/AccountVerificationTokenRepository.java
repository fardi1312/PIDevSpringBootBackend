package tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.AccountVerificationToken;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AccountVerificationTokenRepository extends JpaRepository<AccountVerificationToken, Long> {
    AccountVerificationToken findByToken(String token);

    List<AccountVerificationToken> findByExpiryDateBefore(LocalDateTime todayMidnight);

    AccountVerificationToken findByUser(User user);
}
