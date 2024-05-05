package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Resizable ;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileNamingUtil;
import tn.esprit.pidevspringbootbackend.UserConfig.utilFiles.FileUploadUtil;

import java.io.IOException;
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
@Autowired
    Environment environment;
@Autowired
    FileNamingUtil fileNamingUtil;
@Autowired
    FileUploadUtil utils;



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
    public boolean SendMail1(calendarEvent event ) {
        User offerer = userRepository.findByIdUser(event.getIdOfferer()) ;
        User requester = userRepository.findByIdUser(event.getIdRequester()) ;

        notificationService.notifyRequesterOfApproval1(requester,offerer);
        return true ;
    }
    public boolean SendMail2(calendarEvent event ) {
        User offerer = userRepository.findByIdUser(event.getIdOfferer()) ;
        User requester = userRepository.findByIdUser(event.getIdRequester()) ;

        notificationService.notifyRequesterOfRefusal(requester,offerer);
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        Date start = calendar.getTime();
        calendar.setTime(start);
        System.out.println("test" + start);
        calendar.add(Calendar.HOUR_OF_DAY, 1);

        Date end = calendar.getTime();

        boolean conflictFound = true; // Initialize conflictFound to true

        while (conflictFound) { // Continue loop until conflictFound is false
            conflictFound = false; // Reset conflictFound for each iteration
            for (calendarEvent event : userCalendarEvents) {
                if ((event.getStart().before(end) && event.getEnd().after(start)) ||
                        (event.getStart().after(start) && event.getEnd().before(end)) ||
                        (event.getStart().before(start) && event.getEnd().after(end)) ||
                        (event.getStart().equals(start) && event.getEnd().equals(end))) {
                    // Found a conflict with an existing event
                    conflictFound = true;
                    // Fix the time
                    start = event.getEnd(); // Update the start time to the end time of the conflicting event
                    calendar.setTime(start);
                    calendar.add(Calendar.HOUR_OF_DAY, 1); // Add 1 hour to the start time
                    end = calendar.getTime(); // Update the end time
                    break; // Exit the loop after fixing the time for the current conflicting event
                }
            }
        }

        System.out.println("dtest" + start);
        calendar.setTime(start);
        calendar.add(Calendar.HOUR_OF_DAY, 1);
        end = calendar.getTime();

        // Create the new external event
        calendarEvent externalEvent = new calendarEvent();
        externalEvent.setTitle("Accepted Request Event");
        externalEvent.setStart(start);
        externalEvent.setEnd(end);


        User requester = collocationRequest.getUser() ;
        User offerer=collocationOffer.getUser();
        externalEvent.setRequester(requester.getFirstName() +' '+ requester.getLastName());
        externalEvent.setOfferer(offerer.getFirstName() +' ' + offerer.getLastName());
        externalEvent.setIdRequester(requester.getIdUser());
        externalEvent.setIdOfferer(offerer.getIdUser());
        externalEvent.setCollocationOfferId(collocationOffer.getIdCollocationOffer());
        externalEvent.setIdCollocationRequest(collocationRequest.getIdCollocationRequest());
        externalEvent.setType(true);
        Resizable resizable = new Resizable() ;
        externalEvent.setResizable(resizable);
        List<User> users = new ArrayList<>();
        users.add(collocationOffer.getUser());
        users.add(collocationRequest.getUser());
        externalEvent.setUsers(users);

        calendarEventRepository.save(externalEvent);
        System.out.println("External Event created");

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


    public String getImageUrlForCovByID(Long id) {
        CollocationOffer coo = collocationOfferRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("CarpoolingOffer not found with id: " + id));
        String baseUrl = environment.getProperty("export.collocation.images");
        String ccImage = coo.getImageCollocation();

        if (ccImage != null && !ccImage.isEmpty()) {

            System.err.println(baseUrl + ccImage);
            return baseUrl + ccImage;
        }

        return null;
    }
    public CollocationOffer updatePostImage(Long idO, MultipartFile image) {
        CollocationOffer coo = collocationOfferRepository.findById(idO).orElseThrow(() -> new EntityNotFoundException("CarpoolingOffer not found with id: " + idO));

        try {
            if (image != null && !image.isEmpty() && image.getSize() > 0) {
                String uploadDir = environment.getProperty("upload.collocation.images");
                String newPhotoName = fileNamingUtil.nameFile(image);
                coo.setImageCollocation(newPhotoName);

                utils.saveNewFile(uploadDir, newPhotoName, image);
            }
            return collocationOfferRepository.save(coo);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update CC photo", e);
        }
    }



}


