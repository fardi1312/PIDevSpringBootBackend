package tn.esprit.pidevspringbootbackend.DTO.Massoud;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String email;
    private String password;
    private String passwordRepeat;
    private String firstName;
    private String lastName;
}
