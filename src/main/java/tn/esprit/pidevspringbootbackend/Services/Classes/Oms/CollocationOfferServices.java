package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.*;
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
    private Environment environment;
    @Autowired
    private ExternalEventRepository externalEventRepository ;
    @Autowired
    private eventRepository calendarEventRepository;

    @Autowired
    private FileUploadUtil utils ;
    @Autowired
    private FileNamingUtil fileNamingUtil ;

    @Autowired
    private EmailServiceAchref notificationService ;

    public Optional<CollocationOffer> getCollocationOfferById(long id) {
        return collocationOfferRepository.findById(id);
    }
    public List<CollocationPreferences> getCollocationPreferencesByUserId(Long userId) {
        return collocationOfferRepository.findByUserIdUser(userId);
    }
    public List<CollocationOffer> getAllCollocationOffersByUserId(long userId) {
        return collocationOfferRepository.findByUser(userRepository.findByIdUser(userId));
    }
    public List<CollocationOffer> getAllCollocationOffers() {
        return collocationOfferRepository.findAll();
    }

    public void deleteCollocationOffer(long id) {
        collocationOfferRepository.deleteById(id);
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

        boolean conflictFound = true; // Initialize conflictFound to false

        // Check for conflicts with existing events
        while (conflictFound) { // Continue loop until conflictFound is false
            for (calendarEvent event : userCalendarEvents) {
                conflictFound = false ;
                if ((event.getStart().before(end) && event.getEnd().after(start)) ||
                        (event.getStart().after(start) && event.getEnd().before(end)) ||
                        (event.getStart().before(start) && event.getEnd().after(end)) ||
                        (event.getStart().equals(start) && event.getEnd().equals(end))) {
                    calendar.setTime(event.getEnd()); // Add 1 hour to the start time

                    start = calendar.getTime(); // Update the start time
                    System.out.println("dfsdfdsf" + start);
                    calendar.add(Calendar.HOUR_OF_DAY, 1);
                    System.out.println(start); // Add 1 hour to the start time
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
        User requester = collocationRequest.getUser() ;

        externalEvent.setStart(start);
        externalEvent.setEnd(end);

        externalEvent.setRequester(requester.getFirstName() + requester.getLastName());
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
    public CollocationOffer updateCollocationOffer(long id, CollocationOffer updatedCollocationOffer)  {
        Optional<CollocationOffer> existingCollocationOfferOptional = collocationOfferRepository.findById(id);

        if (existingCollocationOfferOptional.isPresent()) {
            CollocationOffer existingCollocationOffer = existingCollocationOfferOptional.get();
            existingCollocationOffer.setLocationLx(updatedCollocationOffer.getLocationLx());
            existingCollocationOffer.setLocationLy(updatedCollocationOffer.getLocationLy());
            existingCollocationOffer.setGovernorate(updatedCollocationOffer.getGovernorate());
            existingCollocationOffer.setCountry(updatedCollocationOffer.getCountry());
            existingCollocationOffer.setCity(updatedCollocationOffer.getCity());
            existingCollocationOffer.setStreetAddress(updatedCollocationOffer.getStreetAddress());
            existingCollocationOffer.setHouseType(updatedCollocationOffer.getHouseType());
            existingCollocationOffer.setRoomDetailsList(updatedCollocationOffer.getRoomDetailsList());
            existingCollocationOffer.setAvailablePlaces(updatedCollocationOffer.getAvailablePlaces());
            existingCollocationOffer.setDateRent(updatedCollocationOffer.getDateRent());
            existingCollocationOffer.setDateOffer(updatedCollocationOffer.getDateOffer());
            existingCollocationOffer.setGender(updatedCollocationOffer.getGender());
            existingCollocationOffer.setImageCollocation(updatedCollocationOffer.getImageCollocation());
            existingCollocationOffer.setPrice(updatedCollocationOffer.getPrice());
            existingCollocationOffer.setSaved(updatedCollocationOffer.isSaved());
            existingCollocationOffer.setFurnitureCollocation(updatedCollocationOffer.getFurnitureCollocation());
            existingCollocationOffer.setDescriptionCollocation(updatedCollocationOffer.getDescriptionCollocation());
            existingCollocationOffer.setSmokingAllowed(updatedCollocationOffer.getSmokingAllowed());
            existingCollocationOffer.setPetsAllowed(updatedCollocationOffer.getPetsAllowed());
            existingCollocationOffer.setInterest(updatedCollocationOffer.getInterest());
            existingCollocationOffer.setMatchPercentage(updatedCollocationOffer.getMatchPercentage());

            return collocationOfferRepository.save(existingCollocationOffer);
        } else {
            throw new EntityNotFoundException("CollocationOffer not found with id: " + id);
        }
    }
//    public void toggleOfferSavedStatus(Long offerId, Long userId) {
//        CollocationOffer collocationOffer = collocationOfferRepository.findByUserIdUserAndIdCollocationOffer( userId, offerId);
//        if (collocationOffer != null) {
//            collocationOffer.setSaved(!collocationOffer.isSaved());
//            // Save the updated offer back to the database
//            collocationOfferRepository.save(collocationOffer);
//        }
//    }


    public boolean SendMail(long offerId,long requestId) {
        CollocationRequest collocationRequest = collocationRequestRepository.findById(requestId).get();
        CollocationOffer collocationOffer = collocationOfferRepository.findById(offerId).get();
        return true ;

    }
    public List<CollocationOffer> search(String governorate, Integer houseType, Integer availablePlaces, Date dateRent) {
        if (governorate != null && houseType != null && availablePlaces != null && dateRent != null) {
            return collocationOfferRepository.findByGovernorateIgnoreCaseAndHouseTypeAndAvailablePlacesAndDateRent(governorate, houseType, availablePlaces, dateRent);
        } else if (governorate != null && houseType != null && availablePlaces != null) {
            return collocationOfferRepository.findByGovernorateIgnoreCaseAndHouseTypeAndAvailablePlaces(governorate, houseType, availablePlaces);
        } else if (governorate != null && houseType != null) {
            return collocationOfferRepository.findByGovernorateIgnoreCaseAndHouseType(governorate, houseType);
        } else if (governorate != null && availablePlaces != null) {
            return collocationOfferRepository.findByGovernorateIgnoreCaseAndAvailablePlaces(governorate, availablePlaces);
        } else if (governorate != null) {
            return collocationOfferRepository.findByGovernorateIgnoreCase(governorate);
        } else if (houseType != null) {
            return collocationOfferRepository.findByHouseType(houseType);
        } else if (availablePlaces != null) {
            return collocationOfferRepository.findByAvailablePlaces(availablePlaces);
        }
        else if (dateRent != null) {
            return collocationOfferRepository.findByDateRent(dateRent);
        }
        else {
            return collocationOfferRepository.findAll();
        }
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


