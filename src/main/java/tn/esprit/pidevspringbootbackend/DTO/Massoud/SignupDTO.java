package tn.esprit.pidevspringbootbackend.DTO.Massoud;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupDTO {
    private String email;
    private String password;
    private String passwordRepeat;
    private String firstName;
    private String lastName;
}

