package tn.esprit.pidevspringbootbackend.DTO.Massoud;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEmailDTO {
    private String email;
    private String password;
}
