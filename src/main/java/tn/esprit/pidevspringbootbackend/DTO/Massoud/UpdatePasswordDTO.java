package tn.esprit.pidevspringbootbackend.DTO.Massoud;


import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePasswordDTO {

    private String currentPassword;
    private String newPassword;

}
