package tn.esprit.pidevspringbootbackend.RestControllers.Massoud;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Help;
import tn.esprit.pidevspringbootbackend.Services.Classes.Massoud.UserActivityService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IHelpService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserActivityController {

    private final   UserActivityService userActivityService;
    private final   IHelpService iHelpService;



    @GetMapping("user/account/connected-user")
        public List<String> getUserActivitiesByConnectedUserId() {
        return userActivityService.getUserActivitiesByConnectedUserId();
    }


    @GetMapping("user/account/helps")
    public List<Help> getAllHelps() {
        return iHelpService.getAllHelps();
    }

}

