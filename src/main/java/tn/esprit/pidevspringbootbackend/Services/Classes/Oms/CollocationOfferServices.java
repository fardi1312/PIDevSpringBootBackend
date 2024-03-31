package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.RoomDetails;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationRequestRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.RoomDetailsRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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

    NameFile fileNamingUtil;
    FileUtil utils;



    public Optional<CollocationOffer> getCollocationOfferById(long id) {
        return collocationOfferRepository.findById(id);
    }
    public List<CollocationPreferences> getCollocationPreferencesByUserId(Long userId) {
        return collocationOfferRepository.findByUserIdUser(userId);
    }

    public List<CollocationOffer> getAllCollocationOffers() {
        return collocationOfferRepository.findAll();
    }

    public void deleteCollocationOffer(long id) {
        collocationOfferRepository.deleteById(id);
    }
    public boolean acceptRequest(long offerId, long requestId) {
        CollocationOffer collocationOffer = collocationOfferRepository.getReferenceById(offerId);

        List<CollocationRequest> collocationRequests = collocationOffer.getCollocationRequests();

        Iterator<CollocationRequest> iterator = collocationRequests.iterator();

        while (iterator.hasNext()) {
            CollocationRequest request = iterator.next();

            if (request.getIdCollocationRequest() == requestId) {
                request.setRequest(Request.Approved);
                collocationOffer.setAvailablePlaces(collocationOffer.getAvailablePlaces() - 1);
                collocationRequestRepository.save(request);

                RoomDetails roomDetailsToRemove = collocationOffer.getRoomDetailsList().stream()
                        .filter(roomDetails -> roomDetails.getCollocationRequest().getIdCollocationRequest() == requestId)
                        .findFirst()
                        .orElse(null);

                if (roomDetailsToRemove != null) {
                    collocationOffer.getRoomDetailsList().remove(roomDetailsToRemove);
                    roomDetailsRepository.delete(roomDetailsToRemove);
                }

                return true;
            }
        }

        return false;
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
    public CollocationOffer saveCollocationOffer(CollocationOffer collocationOffer, long userId) {
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
    public void toggleOfferSavedStatus(Long offerId) {
        CollocationOffer collocationOffer = collocationOfferRepository.getReferenceById(offerId);
        if (collocationOffer != null) {
            collocationOffer.setSaved(!collocationOffer.isSaved());
            // Save the updated offer back to the database
            collocationOfferRepository.save(collocationOffer);
        }
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



    public CollocationOffer updateCollocationImage(long idCollocation, MultipartFile competitionImage) {
        CollocationOffer collocationOffer= collocationOfferRepository.findById(idCollocation).get();
        try {
            if (competitionImage != null && !competitionImage.isEmpty() && competitionImage.getSize() > 0) {
                String uploadDir = environment.getProperty("upload.competition.images");

                String newPhotoName = fileNamingUtil.nameFile(competitionImage);
                collocationOffer.setImageCollocation(newPhotoName);



                utils.saveNewFile(uploadDir, newPhotoName, competitionImage);
            }
            return collocationOfferRepository.save(collocationOffer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to update competition photo", e);
        }
    }

    public String getImageUrlForCompetitionByID(long idCollocation) {
        CollocationOffer collocationOffer= collocationOfferRepository.findById(idCollocation).get();
        String baseUrl = environment.getProperty("export.competition.images");
        String competitionImage = collocationOffer.getImageCollocation();

        if (competitionImage != null && !competitionImage.isEmpty()) {

            System.err.println(baseUrl + competitionImage);
            return baseUrl + competitionImage;
        }

        return null;
    }

}


