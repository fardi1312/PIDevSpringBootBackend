package tn.esprit.pidevspringbootbackend.Services.Classes.Amira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Registration;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Amira.RegistrationStatus;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Amira.RegistrationRepository;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira.IRegistrationService;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService implements IRegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Override
    public Registration addInscription(Registration registration) {
        return registrationRepository.save(registration);
    }
    public Registration updateRegistrationStatusToActive(Long registrationId) {
        Optional<Registration> optionalRegistration = registrationRepository.findById(registrationId);
        if (optionalRegistration.isPresent()) {
            Registration registration = optionalRegistration.get();
            registration.setStatus(RegistrationStatus.ACTIVE);
            return registrationRepository.save(registration);
        } else {
            throw new IllegalArgumentException("Registration not found with ID: " + registrationId);
        }
    }
    @Override
    public List<Registration> getAllInscriptions() {
        return registrationRepository.findAll();
    }

    @Override
    public Registration getInscriptionById(Long id) {
        Optional<Registration> optionalInscription = registrationRepository.findById(id);
        return optionalInscription.orElse(null);
    }

    @Override
    public List<Registration> getRegistrationsByUserId(Long userId) {
        return registrationRepository.findByUserIdUser(userId);
    }

    @Override
    public Registration updateInscription(Registration registration) {
        return registrationRepository.save(registration);
    }

    @Override
    public void deleteInscription(Long id) {
        registrationRepository.deleteById(id);
    }
}
