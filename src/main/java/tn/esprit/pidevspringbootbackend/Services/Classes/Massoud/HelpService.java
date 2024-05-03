package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Help;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.HelpRepository;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IHelpService;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
@RequiredArgsConstructor
public class HelpService implements IHelpService, CommandLineRunner {

    private final HelpRepository helpRepository;


    @Override
    public Help createHelp(Help help) {
        return helpRepository.save(help);
    }
    @Override
    public List<Help> getAllHelps() {
        return helpRepository.findAll();
    }

    @Override
    public Optional<Help> getHelpById(Long id) {
        return helpRepository.findById(id);
    }

    @Override
    public Help updateHelp(Help help) {
        return helpRepository.save(help);
    }

    @Override
    public void deleteHelp(Long id) {
        helpRepository.deleteById(id);
    }

    @Override
    public void run(String... args) {
        initializeHelpData();
    }

    private void initializeHelpData() {
        if (helpRepository.count() == 0) {
            helpRepository.save(new Help("How to use our application", "This guide explains how to navigate and use the features of our application."));
            helpRepository.save(new Help("Login issue", "If you are having trouble logging in, please reset your password or contact support."));
            helpRepository.save(new Help("How to customize your profile", "Learn how to update your profile picture, cover photo, and other information in your user profile."));
            helpRepository.save(new Help("Frequently Asked Questions", "Check this section for answers to commonly asked questions by our users."));
        }
    }
}
