package tn.esprit.pidevspringbootbackend.RestControllers.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Promotion;
import tn.esprit.pidevspringbootbackend.Services.Classes.Oms.PromotionService;
import tn.esprit.pidevspringbootbackend.Services.Interfaces.Massoud.IUserService;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
@CrossOrigin("*")

public class PromotionRest {


    @Autowired
    private IUserService userService;
    @Autowired
    private PromotionService promotionService;
    @PostMapping
    public Promotion addPromotion(@RequestBody Promotion promotion) {
        return promotionService.addPromotion(promotion);
    }

    @GetMapping("/{id}")
    public Promotion getPromotionById(@PathVariable Long id) {
        return promotionService.getPromotionById(id);
    }

    @PutMapping("/{id}")
    public Promotion updatePromotion(@PathVariable Long id, @RequestBody Promotion promotion) {
        return promotionService.updatePromotion(id, promotion);
    }

    @DeleteMapping("/{id}")
    public void deletePromotion(@PathVariable Long id) {
        promotionService.deletePromotion(id);
    }

    @PostMapping("/update-expired")
    public void updateExpiredPromoCodes() {
        promotionService.updateExpiredPromoCodes();
    }


    @PostMapping("/apply/{code}/{idC}")
    public ResponseEntity<String> applyDiscount(@PathVariable String code, @PathVariable long idC) {
        try {
            promotionService.applyDiscount(code, idC);
            return ResponseEntity.ok("Discount applied successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to apply discount: " + e.getMessage());
        }
    }
@GetMapping("/discount")
public float discount(@RequestParam String code, @RequestParam float total) {
      return  promotionService.discount(code,total);
    }


    @GetMapping("/PromotionValalble")
    public List<Promotion> getAllPromotionsValable() {
        return promotionService.getAllPromotionsValable();
    }
    @GetMapping
    public List<Promotion> getAllPromotions() {
        return promotionService.getAllPromotions();
    }
}
