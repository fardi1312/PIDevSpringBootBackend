package tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

    @Column(length = 128, nullable = false, unique = true)
    private String username;

    @Column(length = 255, nullable = false, unique = true)
    private String email;

    @Column(length = 256, nullable = false)
    @JsonIgnore
    private String password; // Use a secure password hashing mechanism!

    @Column(length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = false)
    private String lastName;

    @Column(length = 100)
    private String intro;

    @Column(length = 16)
    private String gender;

    @Column(length = 128)
    private String hometown;

    @Column(length = 128)
    private String currentCity;

    @Column(length = 128)
    private String eduInstitution;

    @Column(length = 128)
    private String workplace;

    @Column(length = 256)
    private String profilePhoto;

    @Column(length = 256)
    private String coverPhoto;



    private Integer followerCount;
    private Integer followingCount;

    @Column(nullable = false)
    private Boolean enabled; // Account enabled status

    @Column(nullable = false)
    private Boolean accountVerified; // Account verification status

    @Column(nullable = false)
    private Boolean emailVerified; // Email verification status

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate; // Store as LocalDate

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime joinDate; // Store as LocalDateTime

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateLastModified; // Store as LocalDateTime


    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
