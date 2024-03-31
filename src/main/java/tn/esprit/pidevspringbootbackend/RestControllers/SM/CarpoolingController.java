package tn.esprit.pidevspringbootbackend.RestControllers.SM;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class CarpoolingController {
    @Autowired
    private  IServiceCarpooling iServiceCarpooling;
    @Autowired
    private  IUserService userService;

// http://localhost:8083/sui/addCO

    @PostMapping("/addCO")
    public CarpoolingOffer addCarpoolingOffer(@RequestBody CarpoolingOffer c){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        return iServiceCarpooling.addCarpoolingOffer(user.getIdUser(), c);
    }

    @PostMapping("/addreq/{ido}/{nba}/{nbr}")
    public CarpoolingRequest addCarpoolingRequest (@PathVariable( value ="nba" ) int nba, @PathVariable( value ="nbr" ) int nbr, @RequestBody CarpoolingRequest c, @PathVariable( value ="ido" ) Long ido){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        return iServiceCarpooling.addCarpoolingRequest(user.getIdUser(), ido, c, nba, nbr);
    }

    @PostMapping("/addPref")
    public CarpoolingPreferences addCarpoolingPereferences (@RequestBody CarpoolingPreferences c){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        return iServiceCarpooling.addCarpoolingPereferences(user.getIdUser(), c);
    }

    @PostMapping("/updateOff/{ido}")
    public CarpoolingOffer updateCarpoolingOffert (@RequestBody CarpoolingOffer co , @PathVariable (value ="ido") Long ido){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        return iServiceCarpooling.updateCarpoolingOffert(user.getIdUser(), co);
    }

    @PostMapping("/updateReq/{idr}")
    public CarpoolingRequest updateCarpoolingRequest (@RequestBody CarpoolingRequest cr , @PathVariable (value ="idr") Long idr){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        return iServiceCarpooling.updateCarpoolingrequest(user.getIdUser(), cr);
    }

    @PostMapping("/updatepref/{idp}")
    public CarpoolingPreferences updateCarpoolingPreferences (@RequestBody CarpoolingPreferences cr , @PathVariable (value ="idp") Long idr){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        return iServiceCarpooling.updateCarpoolingPreferences(user.getIdUser(), cr);
    }

    @PostMapping("/DEL")
    public Void deleteCarpoolingPereferences (){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        iServiceCarpooling.deleteCarpoolingOffer(user.getIdUser());
        return null;
    }

    @GetMapping("/getall")
    public List<CarpoolingOffer> getll(){
        return iServiceCarpooling.getAllActiveOffers();
    }

    @GetMapping("/search/{location}/{type}/{departureDate}/{returnDate}")
    public List<CarpoolingOffer> searchCarpoolingOffer(
            @PathVariable("location") String location,
            @PathVariable("type") CarpoolingType type,
            @PathVariable("departureDate") Date departureDate,
            @PathVariable("returnDate") Date returnDate) {

        return iServiceCarpooling.searchCarpoolingOffer(location, type, departureDate, returnDate);
    }

    @GetMapping("/getMatching")
    public List<CarpoolingOffer> findMatchingOffers() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        return iServiceCarpooling.findMatchingOffers(user.getIdUser());
    }

    @PostMapping("/updatePoint/{pcn}")
    public PointCount updatePoint(@PathVariable("pcn") Long pcn) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        return iServiceCarpooling.updatePoint(user.getIdUser(), pcn);
    }

    @GetMapping("/search-carpooling-offers/{date}/{price}")
    public List<CarpoolingOffer> searchCarpoolingOffers(
            @PathVariable("date") String date,
            @PathVariable("price") double price
    ) {
        return iServiceCarpooling.findCarpoolingOffersByDateAndPrice(date, price);
    }
}
