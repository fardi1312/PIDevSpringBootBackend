package tn.esprit.pidevspringbootbackend.DAO.Repositories.S;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Panier9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Post9ach;

@Repository
public interface RepoPanier9ach extends JpaRepository<Panier9ach, Long> {
}
