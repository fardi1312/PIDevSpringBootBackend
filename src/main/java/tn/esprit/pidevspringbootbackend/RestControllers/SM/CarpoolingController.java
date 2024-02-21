package tn.esprit.pidevspringbootbackend.RestControllers.SM;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.SM.IServiceCarpooling;

@RestController
@RequestMapping("/sui")
@RequiredArgsConstructor
public class CarpoolingController {
@Autowired
    IServiceCarpooling iServiceCarpooling ;

@PostMapping("/do/{id}")
    public CarpoolingOffer addCarpoolingOffer(@RequestBody CarpoolingOffer c , @PathVariable (value = "id") Long iid){

    return iServiceCarpooling.addCarpoolingOffer( iid,c);
}
@PostMapping("/addreq/{id}/{ido}")
    public CarpoolingRequest addCarpoolingRequest (@RequestBody CarpoolingRequest c, @PathVariable (value = "id") Long idu,@PathVariable( value ="ido" ) Long ido){

    return iServiceCarpooling.addCarpoolingRequest( idu,ido,c);
}

@PostMapping("/addPref/{id}")
    public CarpoolingPreferences addCarpoolingPereferences (@RequestBody CarpoolingPreferences c , @PathVariable (value = "id") Long idu){
    return iServiceCarpooling.addCarpoolingPereferences(idu, c);
}
    @PostMapping("/DEL/{id}")
    public Void deleteCarpoolingPereferences ( @PathVariable (value = "id") Long idu){
        iServiceCarpooling.deleteCarpoolingOffer(idu);


        return null;
    }


}

