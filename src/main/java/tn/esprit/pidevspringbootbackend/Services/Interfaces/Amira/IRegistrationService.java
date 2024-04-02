package tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira;


import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Registration;

import java.util.List;

public interface IRegistrationService {

    // Create
    Registration addInscription(Registration registration);

    // Read
    List<Registration> getAllInscriptions();
    Registration getInscriptionById(Long id);
    ///registrationbyiduser
    List<Registration> getRegistrationsByUserId(Long userId);

    // Update
    Registration updateInscription(Registration registration);

    // Delete
    void deleteInscription(Long id);
}
