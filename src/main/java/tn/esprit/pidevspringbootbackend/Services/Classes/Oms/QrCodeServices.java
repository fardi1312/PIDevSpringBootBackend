package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import lombok.RequiredArgsConstructor;
import net.glxn.qrgen.QRCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Massoud.User;

import java.io.ByteArrayOutputStream;

@Service
@Transactional
@RequiredArgsConstructor

public class QrCodeServices {

    public String generateQRContent(User u ) {
        return "Phone: " + u.getPhoneNumber() + "\nEmail: " + u.getEmail();
    }
    public byte[] generateQRCodeImage(String content) {
        ByteArrayOutputStream stream = QRCode.from(content).stream();
        return stream.toByteArray();
    }


}
