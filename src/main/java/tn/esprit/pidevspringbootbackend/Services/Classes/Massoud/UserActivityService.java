package tn.esprit.pidevspringbootbackend.Services.Classes.Massoud;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Comment;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.Post;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.CollocationRequest;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingRequest;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.AccountVerificationTokenRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.CommentRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.PostRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Massoud.UserRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationOfferRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.CollocationRequestRepository;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingRequest;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserActivityService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  CommentRepository commentRepository;
    @Autowired
    private  PostRepository postRepository;
    @Autowired
    private  CollocationOfferRepository collocationOfferRepository;
    @Autowired
    private  CollocationRequestRepository collocationRequestRepository;
    @Autowired
    private  RepoCarpoolingOffer repoCarpoolingOffer;
    @Autowired
    private  RepoCarpoolingRequest repoCarpoolingRequest;
    @Autowired
    private IUserService iUserService;

    @Autowired
    private AccountVerificationTokenRepository accountVerificationTokenRepository;


    public List<String> getUserActivitiesByConnectedUserId() {
        List<String> userActivities = new ArrayList<>();

        User authUser = iUserService.getAuthenticatedUser();
        Long connectedUserId = authUser.getIdUser();

        User connectedUser = userRepository.findById(connectedUserId).orElse(null);
        if (connectedUser == null) {
            return userActivities;
        }

        List<Comment> comments = commentRepository.findByAuthor(connectedUser);
        for (Comment comment : comments) {
            StringBuilder paragraph = new StringBuilder();
            paragraph.append("Un commentaire a été ajouté par ");
            paragraph.append(connectedUser.getLastName());
            paragraph.append(" le ");
            paragraph.append(comment.getDateCreated());
            paragraph.append(". Le contenu du commentaire est : '");
            paragraph.append(comment.getContent());
            paragraph.append("'.");
            userActivities.add(paragraph.toString());
        }

        List<Post> posts = postRepository.findByAuthor(connectedUser);
        for (Post post : posts) {
            StringBuilder paragraph = new StringBuilder();
            paragraph.append("Un post a été publié par ");
            paragraph.append(connectedUser.getLastName());
            paragraph.append(" le ");
            paragraph.append(post.getDateCreated());
            paragraph.append(". Le contenu du post est : '");
            paragraph.append(post.getContent());
            paragraph.append("'.");
            userActivities.add(paragraph.toString());
        }


        List<CollocationOffer> collocationOffers = collocationOfferRepository.findByUser(connectedUser);
        for (CollocationOffer collocationOffer : collocationOffers) {
            StringBuilder paragraph = new StringBuilder();
            paragraph.append("Une offre de collocation a été publiée par ");
            paragraph.append(connectedUser.getLastName());
            paragraph.append(" le ");
            paragraph.append(collocationOffer.getDateOffer());
            paragraph.append(". L'offre est pour ");
            paragraph.append(collocationOffer.getAvailablePlaces());
            paragraph.append(" personnes à ");
            paragraph.append(collocationOffer.getGovernorate());
            paragraph.append(".");
            userActivities.add(paragraph.toString());
        }

        List<CollocationRequest> collocationRequests = collocationRequestRepository.findByUser(connectedUser);
        for (CollocationRequest collocationRequest : collocationRequests) {
            StringBuilder paragraph = new StringBuilder();
            paragraph.append("Une demande de collocation a été publiée par ");
            paragraph.append(connectedUser.getLastName());
            paragraph.append(" le ");
            paragraph.append(collocationRequest.getDate());
            paragraph.append(".");
            userActivities.add(paragraph.toString());
        }

        List<CarpoolingOffer> carpoolingOffers = repoCarpoolingOffer.findByUserO(connectedUser);
        for (CarpoolingOffer carpoolingOffer : carpoolingOffers) {
            StringBuilder paragraph = new StringBuilder();
            paragraph.append("Une offre de covoiturage a été publiée par ");
            paragraph.append(connectedUser.getLastName());
            paragraph.append(" le ");
            paragraph.append(carpoolingOffer.getDateAller());
            paragraph.append(". L'offre est pour ");
            paragraph.append(carpoolingOffer.getPlaceDispoAller());
            paragraph.append(" personnes de ");
            paragraph.append(carpoolingOffer.getLocationLx());
            paragraph.append(" à ");
            paragraph.append(carpoolingOffer.getLocationLy());
            paragraph.append(".");
            userActivities.add(paragraph.toString());
        }

        List<CarpoolingRequest> carpoolingRequests = repoCarpoolingRequest.findByUserR(connectedUser);
        for (CarpoolingRequest carpoolingRequest : carpoolingRequests) {
            StringBuilder paragraph = new StringBuilder();
            paragraph.append("Une demande de covoiturage a été publiée par ");
            paragraph.append(connectedUser.getLastName());
            paragraph.append(" le ");
            paragraph.append(carpoolingRequest.getDateAllerReserver());
            paragraph.append(".");
            userActivities.add(paragraph.toString());
        }



        return userActivities;
    }
}