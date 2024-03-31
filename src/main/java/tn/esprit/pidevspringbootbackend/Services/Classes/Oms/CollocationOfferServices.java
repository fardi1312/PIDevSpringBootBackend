package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.*;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.QrCodeServices;

import java.util.*;

@Service
public class CollocationOfferServices {
    @Autowired
    private  CollocationOfferRepository collocationOfferRepository;
    @Autowired
    private CollocationRequestRepository collocationRequestRepository;
    @Autowired
    private UserRepository userRepository ;
    @Autowired
    private RoomDetailsRepository roomDetailsRepository ;
    @Autowired
    private ExternalEventRepository externalEventRepository ;
    @Autowired
    private eventRepository calendarEventRepository;
    @Autowired
    private EmailService notificationService ;
    public CollocationOffer saveCollocationOffer(CollocationOffer collocationOffer) {
        return collocationOfferRepository.save(collocationOffer);
    }

    public Optional<CollocationOffer> getCollocationOfferById(long id) {
        return collocationOfferRepository.findById(id);
    }
    public double calculateAverageRating(CollocationOffer collocationOffer) {
        List<CollocationFeedback>collocationFeedbacks =  collocationOffer.getCollocationFeedbacks();
        if (collocationFeedbacks != null && !collocationFeedbacks.isEmpty()) {
            int sum = 0;
            int count = 0;

            // Iterate through the list of feedbacks
            for (CollocationFeedback feedback : collocationFeedbacks) {
                // Assuming getRating() method exists in CollocationFeedback class
                int rating = feedback.getRating();

                // Check if the rating is valid (non-negative)
                if (rating >= 0) {
                    sum += rating;
                    count++;
                }
            }

            // Calculate the average rating
            if (count > 0) {
                return (double) sum / count;
            }
        }

        // Return 0 if there are no valid ratings
        return 0;
    }

    public List<CollocationOffer> getAllCollocationOffers() {
        return collocationOfferRepository.findAll();
    }

    public void deleteCollocationOffer(long id) {
        collocationOfferRepository.deleteById(id);
    }
    public boolean SendMail(long offerId,long requestId) {
        CollocationRequest collocationRequest = collocationRequestRepository.findById(requestId).get();
        CollocationOffer collocationOffer = collocationOfferRepository.findById(offerId).get();
        notificationService.notifyRequesterOfApproval(collocationRequest.getUser(), collocationOffer);
        return true ;

    }
    public boolean acceptRequest(long offerId, long requestId) {
        System.out.println("aaaaaaa");

        // Retrieve the collocation offer and request
        CollocationOffer collocationOffer = collocationOfferRepository.findById(offerId).orElse(null);
        CollocationRequest collocationRequest = collocationRequestRepository.findById(requestId).orElse(null);

        // Check if either the collocation offer or request is null
        if (collocationOffer == null || collocationRequest == null) {
            return false;
        }

        // Check if the available places in the collocation offer are sufficient for the request
        int requestedPlaces = collocationRequest.getPlaces();
        int availablePlaces = collocationOffer.getAvailablePlaces();
        if (availablePlaces < requestedPlaces) {
            return false;
        }

        // Set the request status to Approved
        collocationRequest.setRequest(Request.Approved);
        collocationRequestRepository.save(collocationRequest);

        // Find the user's existing calendar events
        List<calendarEvent> userCalendarEvents = calendarEventRepository.findByUsers(collocationOffer.getUser());

        // Initialize the start and end times for the new external event
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 9); // Set to 9 AM
        calendar.set(Calendar.MINUTE, 0);
        Date start = calendar.getTime();
        calendar.setTime(start);
        System.out.println("test" + start);
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        Date end = calendar.getTime();

        boolean conflictFound = false; // Initialize conflictFound to false

        // Check for conflicts with existing events
        while (!conflictFound) { // Continue loop until conflictFound is false
            for (calendarEvent event : userCalendarEvents) {
                if ((event.getStart().before(end) && event.getEnd().after(start)) ||
                        (event.getStart().after(start) && event.getEnd().before(end)) ||
                        (event.getStart().before(start) && event.getEnd().after(end)) ||
                        (event.getStart().equals(start) && event.getEnd().equals(end))) {
                    // Found a conflict with an existing event
                    calendar.add(Calendar.HOUR_OF_DAY, 1); // Add 1 hour to the start time
                    start = calendar.getTime(); // Update the start time
                    System.out.println("dfsdfdsf" + start);
                    calendar.add(Calendar.HOUR_OF_DAY, 1); // Add 1 hour to the start time
                    end =calendar.getTime() ;
                    conflictFound = true; // Set conflictFound to true

                }

            }
        }

        // Set the end time to 1 hour after the start time
        System.out.println("dtest" + start);
        calendar.setTime(start);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        end = calendar.getTime();

        // Create the new external event
        calendarEvent externalEvent = new calendarEvent();
        externalEvent.setTitle("Accepted Request Event");
        User offerer=collocationOffer.getUser();
        externalEvent.setStart(start);
        externalEvent.setEnd(end);
        externalEvent.setRequester(collocationRequest.getUser().getFirstName() + collocationRequest.getUser().getLastName());
        externalEvent.setOfferer(offerer.getFirstName() + offerer.getLastName());

        List<User> users = new ArrayList<>();
        users.add(collocationOffer.getUser());
        users.add(collocationRequest.getUser());
        externalEvent.setUsers(users);

        calendarEventRepository.save(externalEvent);
        System.out.println("External Event created");

        // Update the overall available places for the collocation offer
        collocationOffer.setAvailablePlaces(availablePlaces - requestedPlaces);
        collocationOfferRepository.save(collocationOffer);
        System.out.println("Offer saved");

        return true;
    }
    public boolean refuseRequest(long offerId, long requestId) {
        CollocationOffer collocationOffer = collocationOfferRepository.findById(offerId).orElse(null);
        CollocationRequest collocationRequest = collocationRequestRepository.findById(requestId).orElse(null);

        if (collocationOffer == null || collocationRequest == null) {
            return false;
        }

        // Update the overall available places for the collocation offer
        int requestedPlaces = collocationRequest.getPlaces();
        int availablePlaces = collocationOffer.getAvailablePlaces() + requestedPlaces;
        collocationOffer.setAvailablePlaces(availablePlaces);
        collocationOfferRepository.save(collocationOffer);

        // Mark the request as refused
        collocationRequest.setRequest(Request.Canceled);
        collocationRequestRepository.save(collocationRequest);

        // Revert the available places for each room associated with the request
        List<RoomDetails> roomDetailsList = collocationOffer.getRoomDetailsList();
        for (RoomDetails room : roomDetailsList) {
            int remainingPlaces = room.getAvailablePlaces() + collocationRequest.getPlaces();
            room.setAvailablePlaces(remainingPlaces);
        }

        // Save the updated room details
        roomDetailsRepository.saveAll(roomDetailsList);

        return true;
    }
    public boolean checkEventConflict(User u,calendarEvent event ) {
        boolean eventConflict = false ;
        List<calendarEvent> userCalendarEvents = calendarEventRepository.findByUsers(u);
        for (calendarEvent calendarEvent:userCalendarEvents
             ) {     if (calendarEvent.getEnd().after(event.getStart()) && calendarEvent.getStart().before(event.getEnd())) {
            eventConflict = true;
            break;
        }

        }
    return false ;

    }

    public List<CollocationOffer> getAllCollocationOffersByUserId(long userId) {
        return collocationOfferRepository.findByUser(userRepository.findByIdUser(userId));
    }

    public boolean RefuseColloaction(long idOffer , long idRequest) {
       CollocationRequest collocationRequest =  collocationRequestRepository.getReferenceById(idRequest);
       if (collocationRequest != null){
        collocationRequestRepository.getReferenceById(idRequest).setRequest(Request.Canceled);
        collocationRequestRepository.save(collocationRequest);
        return true ; }
       else { return false ;
       }
    }
    //add offer and Associate to a certain user
    public CollocationOffer saveCollocationOfferAndAssociateUser(CollocationOffer collocationOffer, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId)) ;

        List<CollocationOffer> userOffers = user.getCollocationOffers();

        collocationOffer.setUser(user);

        userOffers.add(collocationOffer);

        user.setCollocationOffers(userOffers);

        return collocationOfferRepository.save(collocationOffer);
    }



}


