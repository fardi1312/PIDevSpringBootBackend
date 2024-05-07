package tn.esprit.pidevspringbootbackend.DAO.Repositories.S;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.LikePost;
import tn.esprit.pidevspringbootbackend.DAO.Entities.S.Panier9ach;
@Repository
public interface RepoLikePost  extends JpaRepository<LikePost, Long> {
}
