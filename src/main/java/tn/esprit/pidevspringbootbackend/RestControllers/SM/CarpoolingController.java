package tn.esprit.pidevspringbootbackend.RestControllers.SM;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.SM.IServiceCarpooling;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sui")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CarpoolingController {
@Autowired
    IServiceCarpooling iServiceCarpooling ;

    @Autowired
    private IUserService userService;
@PostMapping("/addCO/{id}")
    public CarpoolingOffer addCarpoolingOffer(@RequestBody CarpoolingOffer c , @PathVariable (value = "id") Long iid){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userService.getUserByEmail(authentication.getName());
    Long idu= user.getIdUser();
    return iServiceCarpooling.addCarpoolingOffer( idu,c);
}
@PostMapping("/addreq/{id}/{ido}/{nba}/{nbr}")
    public CarpoolingRequest addCarpoolingRequest (@PathVariable( value ="nba" ) int nba,@PathVariable( value ="nbr" ) int nbr,@RequestBody CarpoolingRequest c, @PathVariable (value = "id") Long idufake,@PathVariable( value ="ido" ) Long ido){
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userService.getUserByEmail(authentication.getName());
    Long idu= user.getIdUser();
    return iServiceCarpooling.addCarpoolingRequest( idu,ido,c,nba,nbr);
}

@PostMapping("/addPref/{id}")
    public CarpoolingPreferences addCarpoolingPereferences (@RequestBody CarpoolingPreferences c , @PathVariable (value = "id") Long idufake){

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    User user = userService.getUserByEmail(authentication.getName());
    Long idu= user.getIdUser();
    return iServiceCarpooling.addCarpoolingPereferences(idu, c);
}



@PostMapping("/updateOff/{ido}")
public  CarpoolingOffer updateCarpoolingOffert (@RequestBody CarpoolingOffer co , @PathVariable (value ="ido") Long ido){
    return iServiceCarpooling.updateCarpoolingOffert(ido,co);
}
    @PostMapping("/updateReq/{idr}")
    public  CarpoolingRequest updateCarpoolingRequest (@RequestBody CarpoolingRequest cr , @PathVariable (value ="idr") Long idr){
        return iServiceCarpooling.updateCarpoolingrequest(idr,cr);
    }

    @PostMapping("/updatepref/{idp}")
    public  CarpoolingPreferences updateCarpoolingPreferences (@RequestBody CarpoolingPreferences cr , @PathVariable (value ="idp") Long idr){
        return iServiceCarpooling.updateCarpoolingPreferences(idr,cr);
    }

    @PostMapping("/DEL/{id}")
    public Void deleteCarpoolingPereferences ( @PathVariable (value = "id") Long idufdf){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        iServiceCarpooling.deleteCarpoolingOffer(idu);


        return null;
    }

    @GetMapping("/getall")
    public List<CarpoolingOffer> getll(){
return     iServiceCarpooling.getAllActiveOffers();
    }

    @GetMapping("/search/{location}/{type}/{departureDate}/{returnDate}")
    public List<CarpoolingOffer> searchCarpoolingOffer(
            @PathVariable("location") String location,
            @PathVariable("type") CarpoolingType type,
            @PathVariable("departureDate") Date departureDate,
            @PathVariable("returnDate") Date returnDate) {

        return iServiceCarpooling.searchCarpoolingOffer(location, type, departureDate, returnDate);
    }

    @GetMapping("/getMatching/{id}")
    public List<CarpoolingOffer> findMatchingOffers(@PathVariable (value = "id") Long id ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();

        return     iServiceCarpooling.findMatchingOffers(idu);
    }
    @PostMapping("/updatePoint/{idu}/{pcn}")
    public PointCount updatePoint(@PathVariable("idu") Long idu, @PathVariable("pcn") Long pcn) {
        return iServiceCarpooling.updatePoint(idu, pcn);
    }

    @GetMapping("/search-carpooling-offers/{date}/{price}")
    public List<CarpoolingOffer> searchCarpoolingOffers(
            @PathVariable("date") String date,
            @PathVariable("price") double price
    ) {
        return iServiceCarpooling.findCarpoolingOffersByDateAndPrice(date, price);
    }

    @GetMapping("/getcarpolingbyuser")
    public List<CarpoolingOffer> getCarpoolingOffersByUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();

        return iServiceCarpooling.getCarpoolingOfferByIdUser(idu);
    }
    @GetMapping("/namerequester/{offerId}")

    public List<String> getRequesterNames(@PathVariable("offerId") Long idCarpoolingOffer) {
        return iServiceCarpooling.getNameUser(idCarpoolingOffer);
    }

    @GetMapping("/offersrequests/{offerId}")
    public List<CarpoolingRequest> getAllRequestsByOffer(@PathVariable Long offerId) {
    return iServiceCarpooling.getAllRequestsByOffer(offerId);


    }
    @GetMapping("/carpoolingrequests")
    public List<CarpoolingRequest> findCarpoolingRequestsByUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();

        return iServiceCarpooling.findCarpoolingRequestsByUserId(idu);
    }


    @DeleteMapping("/removeCarrpooling/{postId}")
    public ResponseEntity<Void> removePost(@PathVariable Long postId) {


        iServiceCarpooling.removeCarrpooling(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // HTTP 204 No Content

    }




    @PutMapping("/updatePoint11/{userId}")
    public ResponseEntity<PointCount> updateQuizPoint(@PathVariable Long userId) {
        PointCount updatedPointCount = iServiceCarpooling.updateQuizPoint(userId);
        return new ResponseEntity<>(updatedPointCount, HttpStatus.OK);
    }

    @GetMapping("point/{userId}")

    public ResponseEntity<Double> getPointsByUser(@PathVariable long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        Double pointCount = iServiceCarpooling.getPointByuser(idu);
        if (pointCount != null) {
            return ResponseEntity.ok(pointCount);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/updatePointFidelite/{idu}/{pcn}")
    public PointCount updatePointFidelite(@PathVariable("idu") Long idudd, @PathVariable("pcn") Long pcn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        Long idu= user.getIdUser();
        return iServiceCarpooling.updatePointFidelite(idu, pcn);
    }

    @PutMapping("/convert")
    public ResponseEntity<?> convertPoints(@RequestParam Long userId,
                                           @RequestParam double pointFidelite,
                                           @RequestParam double pcn) {
        PointCount updatedPointCount = iServiceCarpooling.convertToPoint(userId, pointFidelite, pcn);
        if (updatedPointCount == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPointCount);
    }
    @GetMapping("/getAccueil")
    public List<Object> getAllAccueil() {
        System.out.println("haddlkjkalzd");
        return iServiceCarpooling.getAllAccueil();
    }

}

