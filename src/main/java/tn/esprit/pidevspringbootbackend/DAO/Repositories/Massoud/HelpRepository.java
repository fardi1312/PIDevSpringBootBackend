package tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Help;


import java.util.Optional;

@Repository
public interface HelpRepository extends JpaRepository<Help, Long> {



}
