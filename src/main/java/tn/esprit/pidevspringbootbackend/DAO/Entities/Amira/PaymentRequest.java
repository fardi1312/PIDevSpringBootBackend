package tn.esprit.pidevspringbootbackend.DAO.Entities.Amira;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class PaymentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int amount;
    private String currency;
    private String stripeToken;
    private String description;

    // Constructor
    public PaymentRequest(int amount, String currency, String stripeToken, String description) {
        this.amount = amount;
        this.currency = currency;
        this.stripeToken = stripeToken;
        this.description = description;
    }
}
