package tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.calendarEvent;

import java.util.List;
import java.util.Optional;

@Repository
public interface eventRepository extends JpaRepository<calendarEvent,Long> {
    public List<calendarEvent> findByUsers(User user);
    Optional<calendarEvent> findByIdAndUsers(Long id, User user);

}
