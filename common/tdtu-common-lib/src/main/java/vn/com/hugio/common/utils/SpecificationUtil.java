package vn.com.hugio.common.utils;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SpecificationUtil {

    public static <T> Specification<T> equalAnd(Map<String, Objects> condition, Class<T> clz) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            condition.keySet().forEach(key -> {
                predicates.add(cb.equal(root.get(key), condition.get(key)));
            });
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static <T> Specification<T> equalOr(Map<String, Objects> condition, Class<T> clz) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            condition.keySet().forEach(key -> {
                predicates.add(cb.equal(root.get(key), condition.get(key)));
            });
            return cb.or(predicates.toArray(Predicate[]::new));
        };
    }

    public static <T> Specification<T> notEqualAnd(Map<String, Objects> condition, Class<T> clz) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            condition.keySet().forEach(key -> {
                predicates.add(cb.notEqual(root.get(key), condition.get(key)));
            });
            return cb.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static <T> Specification<T> notEqualOr(Map<String, Objects> condition, Class<T> clz) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            condition.keySet().forEach(key -> {
                predicates.add(cb.notEqual(root.get(key), condition.get(key)));
            });
            return cb.or(predicates.toArray(Predicate[]::new));
        };
    }

    public static <T> Specification<T> in(String fieldName, Object data, Class<?> subclass, String subClassFieldName, Class<T> clz) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Subquery<?> subquery = query.subquery(subclass);
            Root<?> subqueryRoot = subquery.from(subclass);
            Predicate inList = cb.equal(subqueryRoot.get(subClassFieldName), subqueryRoot.get(subClassFieldName).getJavaType().cast(data));
            subquery.select(subqueryRoot.get(fieldName)).where(inList);
            Expression<String> stringExpression = root.get(fieldName);
            Predicate predicateIn = stringExpression.in(subquery);
            return cb.and(predicateIn);
        };
    }

    public static <T> Specification<T> isNull(String fieldName, Class<T> clz) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> cb.isNull(root.get(fieldName));
    }
}
