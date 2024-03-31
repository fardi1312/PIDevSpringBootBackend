package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.UUID;

@Component

public class NameFile {
    public String nameFile(MultipartFile multipartFile) {
        String originalFileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        int fileDotIndex = originalFileName.lastIndexOf('.');
        String fileExtension = originalFileName.substring(fileDotIndex);
        return UUID.randomUUID().toString() + fileExtension;
    }
}
