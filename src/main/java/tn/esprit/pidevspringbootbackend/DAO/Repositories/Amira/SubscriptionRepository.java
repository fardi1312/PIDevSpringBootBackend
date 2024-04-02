package tn.esprit.pidevspringbootbackend.DAO.Repositories.Amira;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Subscription;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription,Long> {
}
