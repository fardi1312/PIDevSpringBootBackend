package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.calendarEvent;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CalendarEventService;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.CollocationOfferServices;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.EmailService;

import java.awt.image.BufferedImage;
import java.util.List;

@RestController
@RequestMapping("/api/calendar-events")
public class CalendarEventController {

    @Autowired
    private CalendarEventService calendarEventService;
    @Autowired
    private CollocationOfferServices collocationOfferServices ;
    @Autowired
    private EmailService emailService ;
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<calendarEvent>> getAllCalendarEventsByUser(@PathVariable("userId") long userId) {
        List<calendarEvent> events = calendarEventService.getAllCalendarEventsByUser(userId);
        if (events != null) {
            return ResponseEntity.ok(events);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/user/{userId}")
    public ResponseEntity<calendarEvent> getCalendarEventByIdAndUser(@PathVariable("id") long id,
                                                                     @PathVariable("userId") long userId) {
        calendarEvent event = calendarEventService.getCalendarEventByIdAndUser(id, userId);
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/acceptRenting")
    public calendarEvent acceptRenting(@RequestBody calendarEvent event) {
        return calendarEventService.acceptRenting(event);
    }

    @PostMapping("/send-mail")
    public ResponseEntity<String> sendMail1(@RequestBody calendarEvent event) {

        boolean success = collocationOfferServices.SendMail1(event);
        if (success) {
            return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/send-mail-refusal")
    public ResponseEntity<String> sendMail(@RequestBody calendarEvent event) {

        boolean success = collocationOfferServices.SendMail2(event);
        if (success) {
            return new ResponseEntity<>("Email sent successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/acceptRenter")
    public calendarEvent acceptRenter(@RequestBody calendarEvent event) {
        return calendarEventService.acceptRenter(event);
    }



    @PostMapping("/user/{userId}")
    public ResponseEntity<calendarEvent> createCalendarEventForUser(@PathVariable("userId") long userId,
                                                                    @RequestBody calendarEvent event) {
        calendarEvent createdEvent = calendarEventService.createCalendarEventForUser(userId, event);
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
        calendarEvent updated = calendarEventService.updateCalendarEventForUser( userId, updatedEvent);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<Void> deleteCalendarEventForUser(@PathVariable("id") long id,
                                                           @PathVariable("userId") long userId) {
        calendarEventService.deleteCalendarEventForUser(id, userId);
        return ResponseEntity.noContent().build();
    }
}
