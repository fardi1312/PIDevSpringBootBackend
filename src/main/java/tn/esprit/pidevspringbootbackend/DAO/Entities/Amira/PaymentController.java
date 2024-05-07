package tn.esprit.pidevspringbootbackend.DAO.Entities.Amira;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Value("${stripe.secretKey}")
    private String stripeSecretKey;

    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody PaymentRequest paymentRequest) {
        Stripe.apiKey = stripeSecretKey; // Use the injected secret key

        try {
            Map<String, Object> chargeParams = new HashMap<>();
            chargeParams.put("amount", paymentRequest.getAmount());
            chargeParams.put("currency", paymentRequest.getCurrency());
            chargeParams.put("source", paymentRequest.getStripeToken());
            chargeParams.put("description", paymentRequest.getDescription());

            Charge charge = Charge.create(chargeParams);

            // Payment successful
            return ResponseEntity.ok("Payment successful. Charge ID: " + charge.getId());
        } catch (StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing payment: " + e.getMessage());
        }
    }
}
