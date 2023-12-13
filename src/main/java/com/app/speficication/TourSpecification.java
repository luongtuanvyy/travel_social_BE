package com.app.speficication;

import com.app.entity.Tour;
import com.app.payload.request.TourQueryParam;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.util.Date;

@Component
public class TourSpecification {

    public Specification<Tour> hasIdEqual(Integer id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }
    public Specification<Tour> hasVehicleLike(String vehicle) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("vehicle"),  "%"+vehicle+"%");
    }

    public Specification<Tour> hasNameLike(String name) {
        return (Root<Tour> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            String nameWithoutDiacritics = removeDiacritics(name);
            String nameUpperCase = nameWithoutDiacritics.toUpperCase();
            Predicate likePredicate = criteriaBuilder.like(
                    criteriaBuilder.upper(root.get("name.trim()")),
                    "%" + nameUpperCase + "%"
            );
            return likePredicate;
        };
    }
    public static Specification<Tour> isNewlyPosted() {
        return (root, query, criteriaBuilder) -> {
            LocalDate today = LocalDate.now();
            return criteriaBuilder.equal(root.get("createdAt").as(LocalDate.class), today);
        };
    }
    private String removeDiacritics(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public Specification<Tour> priceGreaterThan(BigDecimal minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public Specification<Tour> priceLessThan(BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
    }

    public Specification<Tour> startDateGreaterThanOrEqualTo(LocalDate startDate) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.greaterThanOrEqualTo(root.get("start_date"), startDate);
        };
    }

    public void sortRegistered(){

        return  ;
    }

    public Specification<Tour> getTourSpecification(TourQueryParam tourQueryParam) {
        Specification<Tour> spec = Specification.where(null);
        if (tourQueryParam.getId() != null) {
            spec = spec.and(hasIdEqual(tourQueryParam.getId()));
        }
        if (tourQueryParam.getName() != null) {
            spec = spec.and(hasNameLike(tourQueryParam.getName()));
        }
        if (tourQueryParam.getVehicle() != null) {
            spec = spec.and(hasVehicleLike(tourQueryParam.getVehicle()));
        }
        if (tourQueryParam.getMinPrice() != null) {
            spec = spec.and(priceGreaterThan(tourQueryParam.getMinPrice()));
        }
        if (tourQueryParam.getMaxPrice() != null) {
            spec = spec.and(priceLessThan(tourQueryParam.getMaxPrice()));
        }
        if (tourQueryParam.getStart_date() != null) {
            spec = spec.and(startDateGreaterThanOrEqualTo(tourQueryParam.getStart_date()));
        }
            return spec;
    }
}
