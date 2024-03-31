package tn.esprit.pidevspringbootbackend.DAO.Entities.Ons;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Oms.Request;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollocationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idCollocationRequest;

    @Enumerated(EnumType.STRING)
    private Request request;

    @ElementCollection
    private List<Integer> roomPlaces = new ArrayList<>();

    private int houseType;
    private int places;
    private Date date;
    @ElementCollection
    @CollectionTable(name = "selected_dates", joinColumns = @JoinColumn(name = "request_id"))
    @Column(name = "selected_date")
    private List<Date> selectedDate = new ArrayList<>();




    @ManyToMany
    @JsonBackReference
    private List<RoomDetails> roomDetailsList = new ArrayList<>();

    private String Description;

    @JsonIgnore
    @ManyToOne
    private CollocationOffer collocationOffer;

    @ManyToOne
    private User user;
}
