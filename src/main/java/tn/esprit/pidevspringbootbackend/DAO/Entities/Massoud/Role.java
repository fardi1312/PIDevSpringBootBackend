package tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud;

import jakarta.persistence.*;
import lombok.*;
import tn.esprit.pidevspringbootbackend.DAO.Enumeration.Massoud.RoleType;

@Entity
@Table(name = "roles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRole;
    @Enumerated(EnumType.STRING)
    private RoleType type;



}

