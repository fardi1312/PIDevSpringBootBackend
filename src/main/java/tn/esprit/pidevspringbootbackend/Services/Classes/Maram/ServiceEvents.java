package tn.esprit.pidevspringbootbackend.Services.Classes.Maram;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.CommentMaram;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.EventRequests;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.Events;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Maram.Category;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Maram.EventStatus;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Maram.CommentRepositoryMaram;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Maram.EventRequestRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Maram.RepoEvents;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Maram.IServiceEvents;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;


@Service
@RequiredArgsConstructor
public class ServiceEvents implements IServiceEvents {
    @Autowired
    RepoEvents repoEvents ;

    @Autowired
    UserRepository repoUser ;

    @Autowired
    CommentRepositoryMaram commentRepository ;

    @Autowired
    EventRequestRepository repoEventRequest;


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Events addEvents(Long id, Events E) {
        User u = repoUser.findById(id).orElse(null);
        if (u != null) {
            List<Events> l = u.getEventss();
            if (l == null) {
                l = new ArrayList<>();
                u.setEventss(l);
            }
            l.add(E);
            E.setUserE(u);
            E.setEventStatus(EventStatus.ongoing);

            E.setAttendeeRest(E.getAttendeeCount());

            E.setDateCreation(LocalDate.now());

            return repoEvents.save(E);
        } else {

            return null;
        }
    }



    @Override
    public List<Events> getAllEvents() {
        return repoEvents.findAll();
    }

    @Override
    public List<Events> getAllActifEvents() {
        List<Events> all =  repoEvents.findAll();
        List<Events> actif = new ArrayList<>();
        for (Events event : all) {
            if (event.getEventStatus() != EventStatus.past )
            {
                actif.add(event);
            }
        }



        return actif;


    }



    @Override
    public Events getEventById(Long eventId) {

        Optional<Events> eventOptional = repoEvents.findById(eventId);

        if (eventOptional.isPresent()) {
            Events event = eventOptional.get();

            event.getComments();
            return event;
        } else {
            return null;
        }
    }

    @Override

    public Events updateEvent(Long eventId, Events updatedEvent) {
        return repoEvents.findById(eventId)
                .map(event -> {
                    event.setTitle(updatedEvent.getTitle());
                    event.setDateE(updatedEvent.getDateE());
                    event.setDateCreation(updatedEvent.getDateCreation());
                    event.setDescription(updatedEvent.getDescription());
                    event.setCategory(updatedEvent.getCategory());
                    event.setImage(updatedEvent.getImage());
                    event.setAttendeeCount(updatedEvent.getAttendeeCount());
                    event.setRating(updatedEvent.getRating());
                    event.setEventStatus(updatedEvent.getEventStatus());
                    return repoEvents.save(event);
                })
                .orElse(null); // Event not found
    }

    @Override
    public void deleteEvent(Long eventId) {
        repoEvents.deleteById(eventId);
    }



    @Override
    public EventRequests participate(Long eventId, Long userId) {
        Events event = repoEvents.findById(eventId).orElse(null);

        User user = repoUser.findById(userId).orElse(null);
        PointCount pointCount = user.getPointCount();
        if(pointCount.getNbPoint()>= event.getPrice()){
            pointCount.setNbPoint(pointCount.getNbPoint()-event.getPrice());
            event.getUserE().getPointCount().setNbPoint(event.getUserE().getPointCount().getNbPoint()+event.getPrice());
            if (event != null && user != null) {

                if (event.getAttendeeRest() > 0) {
                    event.setAttendeeRest(event.getAttendeeRest() - 1);
                    if ( event.getAttendeeRest() == 0) {
                        event.setEventStatus(EventStatus.past);
                    }
                    repoEvents.save(event);
                    EventRequests eventRequests = new EventRequests();


                    eventRequests.setUser(user);
                    eventRequests.setEvents(event);


                    return repoEventRequest.save(eventRequests);
                } else {
                    throw new IllegalStateException("No more available spots for this event");
                }

            } else {
                throw new IllegalArgumentException("Event or User not found with ID: " + eventId + ", " + userId);
            }
        }else {

            throw new IllegalStateException("No more available spots for this event");
        }

    }


    @Override
    public List<CommentMaram> getCommentsByEvent(Long id) {
        Events E = repoEvents.findById(id).orElse(null);
        List<CommentMaram> EventComments = new ArrayList<>();
        List<CommentMaram> allComments = commentRepository.findAll();
        for (CommentMaram comment : allComments) {
            if (comment.getEvent() != null && comment.getEvent().equals(E)) {
                EventComments.add(comment);
            }
        }
        return EventComments;
    }

    @Override
    public void addLike(Long userId, Long eventId) {

        User user = repoUser.findById(userId).orElse(null);
        Events event = repoEvents.findById(eventId).orElse(null);

        if (user != null && event != null) {
            if (!event.getLikedByUsers().contains(user)) {
                event.getLikedByUsers().add(user);
                event.setLikesCount(event.getLikesCount() + 1); // Increment likes count
                System.out.println("User " + user.getUsername() + " liked event " + event.getTitle());
            } else {
                event.getLikedByUsers().remove(user); // Remove the user from likedByUsers
                event.setLikesCount(event.getLikesCount() - 1); // Decrement likes count
                System.out.println("User " + user.getUsername() + " removed like from event " + event.getTitle());
            }
            // Update the user's liked events list as well
            if (!user.getLikedEvents().contains(event)) {
                user.getLikedEvents().add(event);
            } else {
                user.getLikedEvents().remove(event);
            }
            // Save both user and event to update the relationship
            repoEvents.save(event);
            repoUser.save(user);
        } else {
            System.out.println("User or event not found");
        }
    }






    @Override
    public int getLikesCountByEventId(Long eventId) {
        Events event = repoEvents.findById(eventId).orElse(null);
        if (event != null) {
            return event.getLikesCount();
        } else {
            // If the event is not found, you may return 0 or handle it as per your requirement
            return 0;
        }
    }

    @Override
    public int islikedOrnot(Long idU, Long idE) {
        Events event = repoEvents.findById(idE).orElse(null);
        User user = repoUser.findById(idU).orElse(null);
        if (event != null && user != null) {
            if (!event.getLikedByUsers().contains(user)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return -1; // Return -1 to indicate either event or user is null
        }
}

    public Map<String, Integer> getEventsPerMonth() {
        // Get all events from the repository
        List<Events> events = repoEvents.findAll();

        // Initialize a map to store the number of events per month
        Map<String, Integer> eventsPerMonth = new HashMap<>();

        // Loop through each event
        for (Events event : events) {
            // Get the month and year of the event
            LocalDate eventDate = event.getDateE().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            int month = eventDate.getMonthValue();
            int year = eventDate.getYear();

            // Format the month and year as a string
            String monthYear = month + "/" + year;

            // Increment the count of events for the corresponding month and year
            eventsPerMonth.put(monthYear, eventsPerMonth.getOrDefault(monthYear, 0) + 1);
        }

        return eventsPerMonth;
    }


@Override
public double[] getCategoryPercentages() {
    List<Events> eventsList = repoEvents.findAll();

    // Initialize counters
    int charityCount = 0;
    int totalCount = eventsList.size();

    // Count occurrences of charity events
    for (Events event : eventsList) {
        if (event.getCategory() == Category.Charity) {
            charityCount++;
        }
    }

    // Calculate percentages
    double charityPercentage = (double) charityCount / totalCount * 100;
    double otherPercentage = 100 - charityPercentage;

    // Return the percentages as an array
    return new double[]{charityPercentage, otherPercentage};
}

 @Autowired
 public Map<YearMonth, Float> calculateMonthlyProfits() {
     List<Events> events = repoEvents.findAll();
     Map<YearMonth, Float> monthlyProfits = new HashMap<>();

     for (Events event : events) {
         float profit = calculateEventProfit(event);

         YearMonth yearMonth = YearMonth.from(event.getDateCreation());

         monthlyProfits.put(yearMonth, monthlyProfits.getOrDefault(yearMonth, 0f) + profit);
     }

     return monthlyProfits;
 }

    private float calculateEventProfit(Events event) {

        float profitPerEvent = 0.1f * event.getPrice() * (event.getAttendeeCount() - event.getAttendeeRest());
        return profitPerEvent;
    }

}












