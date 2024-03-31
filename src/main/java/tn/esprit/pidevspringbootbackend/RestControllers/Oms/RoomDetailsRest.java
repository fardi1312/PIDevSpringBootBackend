package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.RoomDetails;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.RoomDetailsService;

import java.util.List;

@RestController

@RequestMapping("/api/room-details")
@CrossOrigin("*")

public class RoomDetailsRest {
    @Autowired
    private RoomDetailsService roomDetailsService;

    @GetMapping
    public ResponseEntity<List<RoomDetails>> getAllRoomDetails() {
        List<RoomDetails> roomDetailsList = roomDetailsService.getAllRoomDetails();
        return new ResponseEntity<>(roomDetailsList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomDetails> getRoomDetailsById(@PathVariable long id) {
        RoomDetails roomDetails = roomDetailsService.getRoomDetailsById(id);
        return new ResponseEntity<>(roomDetails, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDetails> updateRoomDetails(@PathVariable long id, @RequestBody RoomDetails updatedRoomDetails) {
        RoomDetails roomDetails = roomDetailsService.updateRoomDetails(id, updatedRoomDetails);
        return new ResponseEntity<>(roomDetails, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoomDetails(@PathVariable long id) {
        roomDetailsService.deleteRoomDetails(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
