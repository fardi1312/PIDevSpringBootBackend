package tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud;


import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Help;

import java.util.List;
import java.util.Optional;

public interface IHelpService {

    Help createHelp(Help help);

    List<Help> getAllHelps();

    Optional<Help> getHelpById(Long id);

    Help updateHelp(Help help);

    void deleteHelp(Long id);
}
