package tn.esprit.pidevspringbootbackend.Services.Classes.Oms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.pidevspringbootbackend.DAO.Entities.Ons.Promotion;
import tn.esprit.pidevspringbootbackend.DAO.Entities.SM.CarpoolingOffer;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.Oms.PromotionRepo;
import tn.esprit.pidevspringbootbackend.DAO.Repositories.SM.RepoCarpoolingOffer;

import java.time.LocalDate;
import java.util.List;
@Service
public class PromotionService {


    @Autowired
    PromotionRepo promotionRepository;
    @Autowired
    RepoCarpoolingOffer repoCarpoolingOffer ;


    public Promotion addPromotion (Promotion promotion){
        promotion.setExpire(true);
        return promotionRepository.save(promotion);
    }
    @Scheduled(cron = "0 0 0 * * *")
    public void updateExpiredPromoCodes() {
        LocalDate today = LocalDate.now();
        List<Promotion> expiredPromoCodes = promotionRepository.findByExpirationDate(today);
        for (Promotion promoCode : expiredPromoCodes) {
            promoCode.setExpire(false);
            promotionRepository.save(promoCode);
        }
    }

    public Promotion updatePromotion(Long id, Promotion promotion) {
        if (promotionRepository.existsById((long) Math.toIntExact(id))) {
            promotion.setIdPromotion(id);
            return promotionRepository.save(promotion);
        }
        return null; // Gérer l'erreur si l'ID n'existe pas
    }

    public Void deletePromotion(Long id) {
        promotionRepository.deleteById((long) Math.toIntExact(id));
        return null;
    }

    public List<Promotion> getAllPromotionsValable() {
        return promotionRepository.findByExpireTrue();
    }


    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found for id: " + id));
    }




    public float discount(String code, float total){
        Promotion promotion = new Promotion();
        promotion= promotionRepository.findByCode(code);
        double discountPercentage = promotion.getDiscountPercentage();
        float discount = (float) ((total* discountPercentage)/100);
        return discount;
    }
    public void applyDiscount(String code, long idC){
        CarpoolingOffer o = repoCarpoolingOffer.findById(idC).orElse(null);
        Promotion promotion = promotionRepository.findByCode(code);

        if (o != null && promotion != null) {
            double discountPercentage = promotion.getDiscountPercentage();
            double discount = (o.getPrice() * discountPercentage) / 100;
            double discountedPrice = o.getPrice() - discount;
            o.setPriceApres(discountedPrice);
        }
        else{
            o.setPriceApres(o.getPrice());
        }
        repoCarpoolingOffer.save(o);

    }




}
