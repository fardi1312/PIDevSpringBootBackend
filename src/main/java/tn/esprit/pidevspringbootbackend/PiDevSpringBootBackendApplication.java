package tn.esprit.pidevspringbootbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.RoomDetails;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationRequestService;

@SpringBootApplication
@EnableScheduling
public class PiDevSpringBootBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PiDevSpringBootBackendApplication.class, args);
    }

}
