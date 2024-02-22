package tn.esprit.pidevspringbootbackend.Services.Classes.SM;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.PointCount;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.SM.CarpoolingType;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoPointCount;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.SM.IServiceCarpooling;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicesCarpooling implements IServiceCarpooling {



    @Autowired
    RepoCarpoolingOffer repoCarpoolingOffer ;
    @Autowired
    RepoCarpoolingPreferences repoCarpoolingPreferences ;
    @Autowired
    RepoCarpoolingRequest repoCarpoolingRequest ;
     @Autowired
    RepoPointCount repoPointCount;
     @Autowired
    UserRepository repoUser ;




    @Override
    public CarpoolingOffer  addCarpoolingOffer(Long id, CarpoolingOffer c){
        User u = repoUser.findById(id).orElse(null);
        List<CarpoolingOffer> l = u.getCarpoolingOffers();
        if(l==null){
            List<CarpoolingOffer> ln =new ArrayList<CarpoolingOffer>();
            ln.add(c);
            u.setCarpoolingOffers(ln);

        }
        l.add(c);
        u.setCarpoolingOffers(l);
c.setUserO(u);
//repoUser.save(u);
        return repoCarpoolingOffer.save(c);

    }

   @Override
    public  CarpoolingRequest addCarpoolingRequest(Long id, Long idO, CarpoolingRequest c){

       CarpoolingOffer o = repoCarpoolingOffer.findById(idO).orElse(null);
       User u = repoUser.findById(id).orElse(null);
       PointCount pc = u.getPointCount();
       double priceF= o.getPrice();
       if (o.getCarpoolingType()== CarpoolingType.Aller_et_Retour    ){
           if ( c.getRequestType()==CarpoolingType.Aller_et_Retour){

             // priceF=  priceF*2*c.getPlaceNbre();

           }
           else if(c.getRequestType()==CarpoolingType.Aller){

            //   priceF=  priceF*c.getPlaceNbre();
           }
           else if(c.getRequestType()==CarpoolingType.Retour){
               //o.setCarpoolingType(CarpoolingType.Aller);
            //   priceF=  priceF*c.getPlaceNbre();
           }

       }


       if( pc.getNbPoint()>priceF){

          pc.setNbPoint( pc.getNbPoint()-priceF);

o.getUserO().getPointCount().setNbPoint(o.getUserO().getPointCount().getNbPoint()+o.getPrice()*priceF);
       }

        o.getCarpoolingRequests().add(c);
        c.setCarpoolingoffer(o);
        u.getCarpoolingRequests().add(c);
c.setUserR(u);



        return repoCarpoolingRequest.save(c);
   }

   @Override
    public CarpoolingPreferences addCarpoolingPereferences(Long id, CarpoolingPreferences c){

       User u = repoUser.findById(id).orElse(null);
       u.getCarpoolingPreferences().add(c);
       c.setUserS(u);

        return repoCarpoolingPreferences.save(c);

   }

    @Override
    public void deleteCarpoolingOffer(Long offerId) {
        CarpoolingOffer co = repoCarpoolingOffer.findById(offerId).orElse(null);

        User user = co.getUserO();
        if (user != null) {

            List<CarpoolingOffer> offers = user.getCarpoolingOffers();

            offers.remove(co);
            user.setCarpoolingOffers(offers);
            repoCarpoolingOffer.delete(co);

        }
    }



}
