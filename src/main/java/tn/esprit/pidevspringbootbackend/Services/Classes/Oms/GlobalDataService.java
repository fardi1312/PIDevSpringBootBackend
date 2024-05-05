package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.GlobalData;

@Service
public class GlobalDataService {
    @Autowired
    GlobalData globalData ;
     public int IncrementGlobalData () {
         globalData.setGlobalVariable(globalData.getGlobalVariable() + 1);
         return  globalData.getGlobalVariable() + 1 ;

     }
     public int getGlobalData() {
         return  globalData.getGlobalVariable() ;
     }
}
