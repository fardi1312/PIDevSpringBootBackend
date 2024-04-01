package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.RoomDetails;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.RoomDetailsRepository;

import java.util.List;
@Service
public class RoomDetailsService {
    @Autowired
    private RoomDetailsRepository roomDetailsRepository;
    @Autowired
    private CollocationOfferRepository collocationOfferRepository ;



    public List<RoomDetails> getAllRoomDetails() {
        return roomDetailsRepository.findAll();
    }

    public RoomDetails getRoomDetailsById(long id) {
        return roomDetailsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RoomDetails not found with id: " + id));
    }

    public RoomDetails createRoomDetailsForOffer(long offerId, RoomDetails roomDetails) {
        CollocationOffer collocationOffer = collocationOfferRepository.findById(offerId)
                .orElseThrow(() -> new EntityNotFoundException("CollocationOffer not found with id: " + offerId));

        roomDetails.setCollocationOffer(collocationOffer);
        return roomDetailsRepository.save(roomDetails);
    }

    public RoomDetails updateRoomDetails(long id, RoomDetails updatedRoomDetails) {
        RoomDetails existingRoomDetails = getRoomDetailsById(id);
        existingRoomDetails.setAvailablePlaces(updatedRoomDetails.getAvailablePlaces());
        existingRoomDetails.setRoomType(updatedRoomDetails.getRoomType());
        existingRoomDetails.setPrix(updatedRoomDetails.getPrix());
        return roomDetailsRepository.save(existingRoomDetails);
    }

    public void deleteRoomDetails(long id) {
        roomDetailsRepository.deleteById(id);
    }
}


