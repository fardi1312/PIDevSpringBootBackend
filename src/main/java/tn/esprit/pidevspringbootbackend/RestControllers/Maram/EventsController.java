package tn.esprit.pidevspringbootbackend.RestControllers.Maram;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.CommentMaram;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.EventRequests;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Maram.Events;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Maram.IServiceCommentMaram;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Maram.IServiceEvents;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tfou")
@CrossOrigin("http://localhost:4200")
@RequiredArgsConstructor
public class EventsController {
    @Autowired
    private IServiceEvents iServiceEvents;

    @Autowired
    private IServiceCommentMaram iServiceComment;

    @Autowired
    private IUserService userService;

    @PostMapping("/addEvent/{id}")
    public ResponseEntity<?> addEvent(@RequestBody Events e, @PathVariable("id") Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByEmail(authentication.getName());
        Events addedEvent = iServiceEvents.addEvents(user.getIdUser(), e);
        if (addedEvent != null) {
            return new ResponseEntity<>(addedEvent, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("User with ID " + id + " not found", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/events")
    public List<Events> getAllEvents() {

        return iServiceEvents.getAllEvents();
    }

    @GetMapping("/Actifevents")
    public List<Events> getAllActifEvents () {
        return iServiceEvents.getAllActifEvents();
    }

    @PostMapping("/participate/{id}")
    public EventRequests participate (@PathVariable (value = "id") Long ide){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByEmail(authentication.getName());
        return iServiceEvents.participate(ide, user.getIdUser());


    }

    @GetMapping("/getEventById/{id}")
    public Events getEventById(@PathVariable (value = "id") Long eventId) {
        return iServiceEvents.getEventById(eventId);
    }

    @GetMapping("/event/{eventId}")
    public List<CommentMaram> getCommentsByEvent(@PathVariable Long eventId) {
        return iServiceEvents.getCommentsByEvent(eventId);
    }



    @GetMapping("/comments")
    public List<CommentMaram> getAllComments() {
        return iServiceComment.getAllComments();
    }


    @PostMapping("/addComment/{eventId}/{userId}")
    public CommentMaram createComment(@RequestBody CommentMaram c, @PathVariable Long eventId, @PathVariable Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUserByEmail(authentication.getName());
        return iServiceComment.createComment(c, eventId, user.getIdUser());
    }


    @PostMapping("/DeleteComment/{id}")
    public Void deleteComment ( @PathVariable (value = "id") Long idE){
        iServiceComment.deleteComment(idE);


        return null;
    }

    @PostMapping("/addLike/{idU}/{idE}")
    public Void addLike(@PathVariable (value = "idU") Long idU, @PathVariable (value = "idE")  Long idE) {

        iServiceEvents.addLike(1L,idE);
        return null;
    }

    @GetMapping("/likesCount/{eventId}")
    public int getLikesCountByEventId(@PathVariable Long eventId) {
        return iServiceEvents.getLikesCountByEventId(eventId);
    }

    @GetMapping("/isLiked/{idU}/{idE}")
    public int islikedOrnot (@PathVariable (value = "idU") Long idU, @PathVariable (value = "idE")  Long idE) {

        return   iServiceEvents.islikedOrnot(1L,idE);

    }

    @GetMapping("/eventsPerMonth")
    public ResponseEntity<Map<String, Integer>> getEventsPerMonth() {

        Map<String, Integer> eventsPerMonth = iServiceEvents.getEventsPerMonth();
        return new ResponseEntity<>(eventsPerMonth, HttpStatus.OK);
    }

    @GetMapping("/categoryPercentages")
    public double[] getCategoryPercentages() {
        return iServiceEvents.getCategoryPercentages();
    }

    @GetMapping("/profits")
    public ResponseEntity<Map<YearMonth, Float>> getMonthlyProfits() {
        Map<YearMonth, Float> monthlyProfits = iServiceEvents.calculateMonthlyProfits();
        return new ResponseEntity<>(monthlyProfits, HttpStatus.OK);
    }

}
