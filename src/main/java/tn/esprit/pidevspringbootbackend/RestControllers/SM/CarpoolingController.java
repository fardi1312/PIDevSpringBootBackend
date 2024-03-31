package tn.esprit.pidevspringbootbackend.RestControllers.SM;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;

import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.SM.IServiceCarpooling;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sui")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class CarpoolingController {
@Autowired
    IServiceCarpooling iServiceCarpooling ;

@PostMapping("/addCO/{id}")
    public CarpoolingOffer addCarpoolingOffer(@RequestBody CarpoolingOffer c , @PathVariable (value = "id") Long iid){
Long s= 1L;
    return iServiceCarpooling.addCarpoolingOffer( s,c);
}
@PostMapping("/addreq/{id}/{ido}/{nba}/{nbr}")
    public CarpoolingRequest addCarpoolingRequest (@PathVariable( value ="nba" ) int nba,@PathVariable( value ="nbr" ) int nbr,@RequestBody CarpoolingRequest c, @PathVariable (value = "id") Long idu,@PathVariable( value ="ido" ) Long ido){

    return iServiceCarpooling.addCarpoolingRequest( idu,ido,c,nba,nbr);

}

@PostMapping("/addPref/{id}")
    public CarpoolingPreferences addCarpoolingPereferences (@RequestBody CarpoolingPreferences c , @PathVariable (value = "id") Long idu){
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
    public Void deleteCarpoolingPereferences ( @PathVariable (value = "id") Long idu){
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

        return     iServiceCarpooling.findMatchingOffers(id);
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



}

