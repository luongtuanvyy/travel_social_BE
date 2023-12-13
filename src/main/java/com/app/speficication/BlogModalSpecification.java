package com.app.speficication;

import com.app.modal.BlogModal;
import com.app.payload.request.BlogModalQueryParam;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.Normalizer;

@Component
public class BlogModalSpecification {
    public Specification<BlogModal> hasNameLike(String keyword) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("createdBy"), "%" + keyword + "%");
    }

    public Specification<BlogModal> hasIdEqual(Integer id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id);
    }

    public Specification<BlogModal> getBlogModalSpecification(BlogModalQueryParam blogModalQueryParam) {
        Specification<BlogModal> spec = Specification.where(null);
        if (blogModalQueryParam.getId() != null) {
            spec = spec.and(hasIdEqual(blogModalQueryParam.getId()));
        }
        if (blogModalQueryParam.getCreatedBy() != null) {
            spec = spec.and(hasNameLike(blogModalQueryParam.getCreatedBy()));
        }
        return spec;
    }

//    public Specification<BlogModal> hasNameLikeBlog(String name) {
//        return (Root<BlogModal> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
//            String nameWithoutDiacritics = removeDiacritics(name.trim());
//            String nameUpperCase = nameWithoutDiacritics.toUpperCase();
//            Predicate likePredicate = criteriaBuilder.like(
//                    criteriaBuilder.upper(root.get("title")),
//                    "%" + nameUpperCase + "%");
//            return likePredicate;
//        };
//    }

    private String removeDiacritics(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
