package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.eventRepository;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.calendarEvent;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.QrCodeServices;

import javax.imageio.ImageIO;
import javax.swing.plaf.synth.SynthTextAreaUI;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CalendarEventService {

    @Autowired
    private eventRepository eventRepository;

    @Autowired
    private UserRepository userService; // Assuming you have a UserService to manage users
    @Autowired
    private QrCodeServices qrCodeServices ;

    public List<calendarEvent> getAllCalendarEventsByUser(long userId) {
        User user = userService.findById(userId).get();
        if (user != null) {
            return eventRepository.findByUsers(user);
        }
        return null;
    }

    public calendarEvent getCalendarEventByIdAndUser(long id, long userId) {
        User user = userService.findById(userId).get();
        if (user != null) {
            Optional<calendarEvent> optionalEvent = eventRepository.findByIdAndUsers(id, user);
            return optionalEvent.orElse(null);
        }
        return null;
    }

    public calendarEvent createCalendarEventForUser(long userId, calendarEvent event) {
        List<User>users = new ArrayList<>() ;

        users.add(userService.findByIdUser(userId));
        if (users != null) {
            event.setUsers(users);
            return eventRepository.save(event);
        }
        return null;
    }

    public calendarEvent updateCalendarEventForUser(long userId, calendarEvent event) {
        // Fetch the user who is updating the event
        User user = userService.findByIdUser(userId);

        // Check if the user exists
        if (user != null) {
            // Fetch all users associated with the event
            List<User> users = event.getUsers();

            // Ensure the updating user is included in the users list
            if (!users.contains(user)) {
                users.add(user);
            }

            // Set the updated list of users for the event
            event.setUsers(users);

            // If the event has an ID, it means it already exists and needs to be updated
            if (event.getId() != 0) {
                calendarEvent existingEvent = eventRepository.findById(event.getId()).get();

                // Check if the existing event exists
                if (existingEvent != null) {
                    existingEvent.setStart(event.getStart());
                    existingEvent.setEnd(event.getEnd());

                    existingEvent.setTitle(event.getTitle());
                    // Save the updated event
                    return eventRepository.save(existingEvent);

                } else {
                    return null;
                }
            } else {
                // If the event doesn't have an ID, it means it's a new event and needs to be created
                return eventRepository.save(event);
            }
        }

        return null;
    }
    public static BufferedImage byteArrayToImage(byte[] byteArray) {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(byteArray);
            return ImageIO.read(bis);
        } catch (IOException e) {
            e.printStackTrace();
            return null; // Handle the error as needed
        }
    }


    public void deleteCalendarEventForUser(long id, long userId) {
        calendarEvent event = getCalendarEventByIdAndUser(id, userId);
        if (event != null) {
            eventRepository.delete(event);
        }
    }
}
