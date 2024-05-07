package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Resizable;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationRequestRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.eventRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.calendarEvent;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarEventService {

    @Autowired
    private eventRepository eventRepository;

    @Autowired
    private UserRepository userService; // Assuming you have a UserService to manage users

    @Autowired
    private CollocationOfferRepository collocationOfferRepository ;
    @Autowired
    private CollocationRequestRepository collocationRequestRepository ;
    @Getter
    public List<String> EmailList = new ArrayList<>()  ;
    @Autowired
    EmailServiceAchref emailService ;

    public List<calendarEvent> getAllCalendarEventsByUser(long userId) {
        User user = userService.findByIdUser(userId);
        if (user != null) {
            return eventRepository.findByUsers(user);
        }
        return null;
    }

    public calendarEvent getCalendarEventByIdAndUser(long id, long userId) {
        User user = userService.findByIdUser(userId);
        if (user != null) {
            Optional<calendarEvent> optionalEvent = eventRepository.findByIdAndUsers(id, user);
            return optionalEvent.orElse(null);
        }
        return null;
    }

    public calendarEvent createCalendarEventForUser(long userId, calendarEvent event) {
        List<User>users = new ArrayList<>() ;

        users.add(userService.findByIdUser(userId));
        if (users != null) {
            event.setUsers(users);
            Resizable resizable = new Resizable() ;
            resizable.setAfterEnd(true);
            resizable.setBeforeStart(true);
            event.setResizable(resizable);
            return eventRepository.save(event);
        }
        return null;
    }
    public calendarEvent updateCalendarEventForUser(long userId, calendarEvent event) {
        // Fetch the user who is updating the event
        User user = userService.findByIdUser(userId);

        // Check if the user exists
        if (user != null) {
            // Fetch all users associated with the event
            List<User> users = event.getUsers();

            // Ensure the updating user is included in the users list
            if (!users.contains(user)) {
                users.add(user);
            }

            // Set the updated list of users for the event
            event.setUsers(users);

            // If the event has an ID, it means it already exists and needs to be updated
            if (event.getId() != 0) {
                calendarEvent existingEvent = eventRepository.findById(event.getId()).get();

                // Check if the existing event exists
                if (existingEvent != null) {
                    existingEvent.setStart(event.getStart());
                    existingEvent.setEnd(event.getEnd());
                    existingEvent.setFixedOfferer(event.getFixedOfferer());
                    existingEvent.setFixedRequester(event.getFixedRequester());
                    existingEvent.setTitle(event.getTitle());
                    existingEvent.setMeetingLink(event.getMeetingLink());
                    existingEvent.setResizable(event.getResizable());
                    existingEvent.setDraggable(event.isDraggable());
                    existingEvent.setAcceptRenter(event.getAcceptRenter());
                    existingEvent.setAcceptRenting(event.getAcceptRenting());

                    return eventRepository.save(existingEvent);

                } else {
                    return null;
                }
            } else {
                // If the event doesn't have an ID, it means it's a new event and needs to be created
                return eventRepository.save(event);
            }
        }

        return null;
    }
    public calendarEvent acceptRenting(calendarEvent event) {
        calendarEvent existingEvent = eventRepository.findById(event.getId()).get();
        Boolean acceptRenter = event.getAcceptRenter();
        if (acceptRenter !=null && acceptRenter == true) {
            System.out.println("Entered if ");
            CollocationOffer collocationOffer = collocationOfferRepository.findById(event.getCollocationOfferId()).get();
            CollocationRequest collocationRequest = collocationRequestRepository.findById(event.getIdCollocationRequest()).get();
            collocationOffer.setAvailablePlaces(collocationOffer.getAvailablePlaces() - collocationRequest.getPlaces());
            eventRepository.delete(existingEvent);
            collocationOfferRepository.save(collocationOffer);
            emailService.notifyRequesterOfApproval2(userService.findById(event.getIdOfferer()).get(),userService.findById(event.getIdRequester()).get()) ;
        }

        existingEvent.setAcceptRenting(true);

        return eventRepository.save(existingEvent);
    }
    public calendarEvent acceptRenter(calendarEvent event) {
        Optional<calendarEvent> optionalEvent = eventRepository.findById(event.getId());
        if (optionalEvent.isPresent()) {
            calendarEvent existingEvent = optionalEvent.get();
            Boolean acceptRenting = existingEvent.getAcceptRenting();
            if (acceptRenting != null && acceptRenting) {
                CollocationOffer collocationOffer = collocationOfferRepository.findById(event.getCollocationOfferId()).orElse(null);
                CollocationRequest collocationRequest = collocationRequestRepository.findById(event.getIdCollocationRequest()).orElse(null);
                if (collocationOffer != null && collocationRequest != null) {
                    collocationOffer.setAvailablePlaces(collocationOffer.getAvailablePlaces() - collocationRequest.getPlaces());

                    collocationOfferRepository.save(collocationOffer);
                    eventRepository.delete(existingEvent);
                    emailService.notifyRequesterOfApproval2(userService.findById(event.getIdRequester()).get(),userService.findById(event.getIdOfferer()).get()) ;


                }
            }
            existingEvent.setAcceptRenter(true);
            return eventRepository.save(existingEvent);
        } else {
            // Handle the case when the event is not found
            return null;
        }
    }


    public static BufferedImage byteArrayToImage(byte[] byteArray) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            return ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error as needed
        }
    }


    public void deleteCalendarEventForUser(long id, long userId) {
        calendarEvent event = getCalendarEventByIdAndUser(id, userId);
        if (event != null) {
            eventRepository.delete(event);
        }
    }
}
