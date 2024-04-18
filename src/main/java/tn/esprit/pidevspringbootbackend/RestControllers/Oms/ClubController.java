package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Club;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.ClubService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clubs")

public class ClubController {

    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    // Create a new club
    @PostMapping("/{id}")
    public ResponseEntity<Club> createClub(@RequestBody Club club,@PathVariable long id) {
        Club createdClub = clubService.createClub(club,id);
        return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
    }

    // Get all clubs
    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() {
        List<Club> clubs = clubService.getAllClubs();
        return new ResponseEntity<>(clubs, HttpStatus.OK);
    }

    // Get club by ID
    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable("id") long id) {
        Optional<Club> club = clubService.getClubById(id);
        return club.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update club by ID
    @PutMapping("/{id}")
    public ResponseEntity<Club> updateClub(@PathVariable("id") long id, @RequestBody Club newClubData) {
        Club updatedClub = clubService.updateClub(id, newClubData);
        if (updatedClub != null) {
            return new ResponseEntity<>(updatedClub, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete club by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable("id") long id) {
        clubService.deleteClub(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
