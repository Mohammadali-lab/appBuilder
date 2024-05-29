package ir.samin.appbuilder.specification;


import ir.samin.appbuilder.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;

@NoArgsConstructor
@AllArgsConstructor
public class EntitySpecification <T extends BaseEntity> implements Specification<T> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder
                    .greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
            }
        } else if (criteria.getOperation().equalsIgnoreCase("=")) {
            return criteriaBuilder.equal(
                    root.get(criteria.getKey()), criteria.getValue());
        } else if (criteria.getOperation().equalsIgnoreCase(".")) {
            ArrayList value = (ArrayList) criteria.getValue();
            CriteriaBuilder.In<Object> in = criteriaBuilder.in(
                    root.get(criteria.getKey()));
            for (Object obj :
                    value) {
                in.value(obj);
            }
            return in;
        }
        return null;
    }
}
