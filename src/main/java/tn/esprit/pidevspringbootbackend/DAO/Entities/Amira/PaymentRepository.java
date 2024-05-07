package tn.esprit.pidevspringbootbackend.DAO.Entities.Amira;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

public interface PaymentRepository extends JpaRepository<PaymentRequest, Long> {
}
