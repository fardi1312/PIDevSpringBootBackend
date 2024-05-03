package tn.esprit.pidevspringbootbackend.DTO.Massoud;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordForgetDTO {
    private String email;
    private String currentPassword;
    private String newPassword;
}
