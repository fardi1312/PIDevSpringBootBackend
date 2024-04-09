package tn.esprit.pidevspringbootbackend.DTO.Massoud;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {
    @NotEmpty
    private String tagName;

    @NotEmpty
    private String action;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TagDTO tagDto = (TagDTO) o;
        return Objects.equals(tagName, tagDto.tagName) && Objects.equals(action, tagDto.action);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName, action);
    }
}
