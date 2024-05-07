package tn.esprit.pidevspringbootbackend.RestControllers.Amira;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Amira.Subscription;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Amira.ISubscriptionService;

import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/user/subscriptions")
public class SubscriptionController {

    @Autowired
    private ISubscriptionService subscriptionService;

    @GetMapping(path = "/all")
    public ResponseEntity<List<Subscription>> getAllSubscriptions() {
        List<Subscription> subscriptions = subscriptionService.getAllSubscriptions();
        return ResponseEntity.ok(subscriptions);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable Long id) {
        Optional<Subscription> subscription = subscriptionService.getSubscriptionById(id);
        return subscription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    //http://localhost:8083/user/subscriptions/add
    @PostMapping("/add")
    public ResponseEntity<Subscription> createSubscription(@RequestBody Subscription subscription) {
        Subscription newSubscription = subscriptionService.createSubscription(subscription);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSubscription);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Subscription> updateSubscription(@PathVariable Long id, @RequestBody Subscription subscriptionDetails) {
        Optional<Subscription> optionalSubscription = subscriptionService.updateSubscription(id, subscriptionDetails);
        if (optionalSubscription.isPresent()) {
            Subscription existingSubscription = optionalSubscription.get();
            existingSubscription.setName(subscriptionDetails.getName());
            existingSubscription.setDescription(subscriptionDetails.getDescription());
            existingSubscription.setPrice(subscriptionDetails.getPrice());
            existingSubscription.setType(subscriptionDetails.getType());
            existingSubscription.setDuration(subscriptionDetails.getDuration());
            Subscription updatedSubscription = subscriptionService.createSubscription(existingSubscription);
            return ResponseEntity.ok(updatedSubscription);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSubscription(@PathVariable Long id) {
        Optional<Subscription> optionalSubscription = subscriptionService.getSubscriptionById(id);
        if (optionalSubscription.isPresent()) {
            subscriptionService.deleteSubscription(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
