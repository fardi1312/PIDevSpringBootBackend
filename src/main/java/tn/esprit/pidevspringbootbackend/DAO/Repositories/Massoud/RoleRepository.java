package tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Role;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Massoud.RoleType;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByType(RoleType type);

    boolean existsByType(RoleType type);

}
