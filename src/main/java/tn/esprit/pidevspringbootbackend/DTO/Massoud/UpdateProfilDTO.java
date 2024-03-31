package tn.esprit.pidevspringbootbackend.DTO.Massoud;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfilDTO {


    @NotEmpty
    @Size(max = 64)
    private String firstName;

    @NotEmpty
    @Size(max = 64)
    private String lastName;

    @Size(max = 100)
    private String intro;

    @Size(max = 16)
    private String gender;

    @Size(max = 64)
    private String hometown;

    @Size(max = 64)
    private String currentCity;

    @Size(max = 128)
    private String eduInstitution;

    @Size(max = 128)
    private String workplace;


    @Size(max = 20)
    private String phoneNumber;




    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date birthDate;
}
