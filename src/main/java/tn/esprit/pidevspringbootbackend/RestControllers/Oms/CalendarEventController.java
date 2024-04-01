package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.calendarEvent;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CalendarEventService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;

import java.awt.image.BufferedImage;
import java.util.List;

@RestController
@RequestMapping("/api/calendar-events")
public class CalendarEventController {

    @Autowired
    private CalendarEventService calendarEventService;

    @Autowired
    private IUserService userService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<calendarEvent>> getAllCalendarEventsByUser(@PathVariable("userId") long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        List<calendarEvent> events = calendarEventService.getAllCalendarEventsByUser(user.getIdUser());
        if (events != null) {
            return ResponseEntity.ok(events);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/user/{userId}")
    public ResponseEntity<calendarEvent> getCalendarEventByIdAndUser(@PathVariable("id") long id,
                                                                     @PathVariable("userId") long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        calendarEvent event = calendarEventService.getCalendarEventByIdAndUser(id, user.getIdUser());
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @PostMapping("/user/{userId}")
    public ResponseEntity<calendarEvent> createCalendarEventForUser(@PathVariable("userId") long userId,
                                                                    @RequestBody calendarEvent event) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        calendarEvent createdEvent = calendarEventService.createCalendarEventForUser(user.getIdUser(), event);
        if (createdEvent != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvent);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
    @PostMapping("/convertByteArrayToImage")
    public ResponseEntity<byte[]> convertByteArrayToImage(@RequestParam byte[] byteArray) {
        BufferedImage image = calendarEventService.byteArrayToImage(byteArray);
        if (image != null) {
            // Convert BufferedImage to byte array
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.IMAGE_JPEG)
                    .body(byteArray);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<calendarEvent> updateCalendarEventForUser(
                                                                    @PathVariable("userId") long userId,
                                                                    @RequestBody calendarEvent updatedEvent) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        calendarEvent updated = calendarEventService.updateCalendarEventForUser( user.getIdUser(), updatedEvent);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<Void> deleteCalendarEventForUser(@PathVariable("id") long id,
                                                           @PathVariable("userId") long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.getUserByEmail(authentication.getName());
        calendarEventService.deleteCalendarEventForUser(id, user.getIdUser());
        return ResponseEntity.noContent().build();
    }
}
