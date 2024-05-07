package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Promotion;

import java.time.LocalDate;
import java.util.List;

public interface PromotionRepo  extends JpaRepository<Promotion,Long> {

    List<Promotion> findByExpirationDate(LocalDate expirationDate);

    List<Promotion> findByExpireTrue();

  Promotion findByCode(String code);
}
