package tn.esprit.pidevspringbootbackend.DTO.Massoud;

import lombok.Data;

@Data
public class AuthenticationDTO {

    private String email;
    private String password;
    private boolean emailVerified;


}
