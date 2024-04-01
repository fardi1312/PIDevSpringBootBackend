package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationPreferences;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.RoomDetails;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class MatchingService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollocationOfferRepository offerRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    public List<CollocationOffer> getMatchingOffersForUser(Long userId) {
        // Retrieve the user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Retrieve the user's preferences
        CollocationPreferences preferences = user.getCollocationPreferences();
        if (preferences == null) {
            return Collections.emptyList();
        }

        // Retrieve all collocation offers
        List<CollocationOffer> allOffers = offerRepository.findAll();
        if (allOffers.isEmpty()) {
            return Collections.emptyList();
        }

        List<CollocationOffer> matchingOffers = new ArrayList<>();
        for (CollocationOffer offer : allOffers) {
            int matchPercentage = calculateMatchPercentage(preferences, offer);
            if (matchPercentage >= 80) { // You can adjust the threshold as needed
                offer.setMatchPercentage(matchPercentage);
                matchingOffers.add(offer);
                if (matchPercentage == 100) {
                    CompletableFuture.runAsync(() -> sendMatchingEmail(user.getEmail(), user.getUsername(), offer.getIdCollocationOffer()));
                }
            }
        }

        matchingOffers.sort(Comparator.comparingInt(CollocationOffer::getMatchPercentage).reversed());

        return matchingOffers;
    }


    private int calculateMatchPercentage(CollocationPreferences preferences, CollocationOffer offer) {
        int totalProperties = 0;
        int matchingProperties = 0;

        // Compare preferences and offer properties
        if (preferences.getPets().equals(offer.getPetsAllowed())) {
            matchingProperties++;
        }
        totalProperties++;

        if (preferences.getSmoking().equals(offer.getSmokingAllowed())) {
            matchingProperties++;
        }
        totalProperties++;

        if (preferences.getInterest().equals(offer.getInterest())) {
            matchingProperties++;
        }
        totalProperties++;

        if (preferences.getGender().equals(offer.getGender())) {
            matchingProperties++;
        }
        totalProperties++;

        if (preferences.getFurnitureCollocation() == null || preferences.getFurnitureCollocation().equals(offer.getFurnitureCollocation())) {
            matchingProperties++;
        }
        totalProperties++;

        if (preferences.getHouseType() == offer.getHouseType()) {
            matchingProperties++;
        }
        totalProperties++;



        float budget = preferences.getBudget();
        float roomPrice = 0;

        // Calculate average room price
        if (offer.getRoomDetailsList() != null && !offer.getRoomDetailsList().isEmpty()) {
            for (RoomDetails room : offer.getRoomDetailsList()) {
                roomPrice += room.getPrix();
            }
            roomPrice /= offer.getRoomDetailsList().size();
        }

        float priceRange = 0.2f * budget; // 20% range around the budget
        if (roomPrice >= budget - priceRange && roomPrice <= budget + priceRange) {
            matchingProperties++;
        }
        totalProperties++;

        if (preferences.getLocation().equals(offer.getGovernorate())) {
            matchingProperties++;
        }
        totalProperties++;

        // Calculate the match percentage
        return totalProperties > 0 ? (matchingProperties * 100) / totalProperties : 0;
    }

    private void sendMatchingEmail(String userEmail, String username, Long offerId) {
        System.out.println("Sending email to: " + userEmail);

        String offerDetailUrl = "http://localhost:4200/Collocation/showDetailsOffer/" + offerId;
        String emailContent = String.format("Dear %s,\n\nCongratulations! We have found a collocation offer that matches all your preferences with a 100%% match. Please click the link below to view the offer details:\n%s\n\nBest regards,\nCO&CO", username, offerDetailUrl);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Congratulations! You have a 100% matching offer.");
        message.setText(emailContent);

        javaMailSender.send(message);

        System.out.println("Email sent successfully.");
    }


}
