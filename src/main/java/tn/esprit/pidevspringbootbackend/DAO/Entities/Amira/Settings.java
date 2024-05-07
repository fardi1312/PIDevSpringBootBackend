package tn.esprit.pidevspringbootbackend.DAO.Entities.Amira;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Amira.TransactionType;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Settings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double commissionRate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

}
