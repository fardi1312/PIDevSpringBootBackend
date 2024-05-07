package tn.esprit.pidevspringbootbackend.DAO.Repositories.S;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Post9ach;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
@Repository
public interface RepoPost9ach extends JpaRepository<Post9ach, Long> {


}
