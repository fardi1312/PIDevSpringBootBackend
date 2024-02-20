package tn.esprit.pidevspringbootbackend.DTO.Massoud;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfoDTO {

    private String email;

    private String firstName;


    private String lastName;


    private String intro;


    private String gender;


    private String hometown;


    private String currentCity;


    private String eduInstitution;


    private String workplace;


    private String countryName;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthDate;
}
