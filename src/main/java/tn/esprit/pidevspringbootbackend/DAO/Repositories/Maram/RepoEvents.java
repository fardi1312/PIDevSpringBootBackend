package tn.esprit.pidevspringbootbackend.DAO.Repositories.Maram;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.Events;

@Repository
public interface RepoEvents extends JpaRepository<Events, Long> {


}
