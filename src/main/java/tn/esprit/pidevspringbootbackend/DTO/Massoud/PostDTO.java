package tn.esprit.pidevspringbootbackend.DTO.Massoud;

import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    @Size(max = 4096)
    private String content;
}
