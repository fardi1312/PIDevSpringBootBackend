package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollocationFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCollocationFeedback;
    private Date feedbackDate;
    private String feedbackDescription;
    private int rating;
    @ManyToOne
    private CollocationOffer collocationOffer;

    @ManyToOne
    private User user ;
}