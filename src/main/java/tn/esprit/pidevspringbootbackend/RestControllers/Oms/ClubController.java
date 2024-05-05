package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.*;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CalendarEventService;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.ClubService;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.GlobalDataService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/clubs")
@CrossOrigin

public class ClubController {

    private final ClubService clubService;

    @Autowired
    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }
    @Autowired
    public GlobalDataService globalDataService ;

    // Create a new club
    @PostMapping("/{id}")
    public ResponseEntity<Club> createClub(@RequestBody Club club,@PathVariable long id) {
        System.out.println(club.getClubMemberShip().get(0).getEmail().toString());
        Club createdClub = clubService.createClub(club,id);
        return new ResponseEntity<>(createdClub, HttpStatus.CREATED);
    }
    @GetMapping("/verify-email/{email}")
    public ResponseEntity<User> verifyEmail(@PathVariable String email) {
        Optional<User> userOptional = clubService.verifyEmail(email);
        if (userOptional.isPresent()) {
            return ResponseEntity.ok(userOptional.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/MemberShipApplication/{id}")
    public ResponseEntity<MemberShipApplication> updateMemberShipApplication(@PathVariable("id") long id,
                                                                             @RequestBody MemberShipApplication updatedMemberShipApplication) {
        try {
            // Call the service method to update the MemberShipApplication
            MemberShipApplication updatedApplication = clubService.updateMemberShipApplication(id, updatedMemberShipApplication);
            return new ResponseEntity<>(updatedApplication, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/assignInterviewer/{id}")
    public ResponseEntity<MemberShipApplication> assignInterviewer(
            @PathVariable long id,
            @RequestBody MemberShipApplication updatedMemberShipApplication) {
        MemberShipApplication result = clubService.assignInterviewer(id, updatedMemberShipApplication);

        return new ResponseEntity<>(result, HttpStatus.OK);

    }



   @PostMapping("/{clubId}/members/{userId}")
    public ResponseEntity<Club> addMemberToClub(@PathVariable long clubId, @PathVariable long userId) {
        Club club = clubService.addMember(clubId, userId);
        return new ResponseEntity<>(club, HttpStatus.OK);
    }

    @PostMapping("/addMember/{clubId}/{userId}")
    public ResponseEntity<Club> addMembershipApplication(@PathVariable long clubId, @PathVariable long userId, @RequestBody Map<String, String> payload) {
        String message = payload.get("message");

        Club club = clubService.addMemberShipApplication(clubId, userId,message);
        return new ResponseEntity<>(club, HttpStatus.OK);
    }
    @PostMapping("/accept")
    public ResponseEntity<Club> acceptMembership(@RequestBody calendarEvent calendarEvent) {
        return new ResponseEntity<> (clubService.acceptMemberShip(calendarEvent),HttpStatus.OK) ;
    }

    @PostMapping("/refuse")
    public ResponseEntity<Club> refuseMembership(@RequestBody calendarEvent calendarEvent) {
        globalDataService.IncrementGlobalData() ;
        return new ResponseEntity<> (clubService.refuseMemberShip(calendarEvent),HttpStatus.OK) ;
    }
    @GetMapping("/getGlobalData")
    public int getGlobalData() {
        return globalDataService.getGlobalData();
     }

    @GetMapping("/MembershipApplications")
    public List<MemberShipApplication> getAllMembershipApplications() {
        return clubService.getMembershipApplications();
    }
 @PostMapping



    @GetMapping("/Memberships")
    public List<ClubMembership> getAllMemberships() {
        return clubService.getAllClubMemberships();
    }


    @GetMapping
    public ResponseEntity<List<Club>> getAllClubs() {
        List<Club> clubs = clubService.getAllClubs();
        return new ResponseEntity<>(clubs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Club> getClubById(@PathVariable("id") long id) {
        Optional<Club> club = clubService.getClubById(id);
        return club.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PostMapping("/uploadImage/{id}")
    public Club updateCompetitionImage(@PathVariable("id") Long idCC, @RequestParam("image") MultipartFile image) {
        System.out.println("aaaaaaaaa d5alna");
        System.out.println(idCC);
        System.out.println(image);


        return clubService.updateClubLogo(idCC, image);
    }

    @GetMapping("/getImage/{id}")
    public String getCompetitionImage(@PathVariable("id") Long idCc){
        return clubService.getImageUrlForCovByID(idCc);
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
