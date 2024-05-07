package tn.esprit.pidevspringbootbackend.Services.Interfaces.Maram;

import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.CommentMaram;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.EventRequests;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.Events;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

public interface IServiceEvents {
    Events addEvents(Long id, Events E);

    List<Events> getAllEvents();

    List<Events> getAllActifEvents();

    Events getEventById(Long eventId);

    // Update operation
    Events updateEvent(Long eventId, Events updatedEvent);

    void deleteEvent(Long eventId);




    List<CommentMaram> getCommentsByEvent(Long eventId);

    void addLike(Long userId, Long eventId);

     EventRequests participate(Long eventId, Long userId);

    int getLikesCountByEventId(Long eventId);

    int islikedOrnot(Long idE, Long idU);

    Map<String, Integer> getEventsPerMonth();

     double[] getCategoryPercentages();

    Map<YearMonth, Float> calculateMonthlyProfits();
}