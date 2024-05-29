package ir.samin.appbuilder.specification;


import ir.samin.appbuilder.entity.BaseEntity;
import ir.samin.appbuilder.entity.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class GeneralSpecificationList<T extends BaseEntity> {

    public Specification<T> dateSpecification(LocalDateTime startDate, LocalDateTime endDate, String createdAtDateTime) {
        if (startDate != null && endDate == null) {
            endDate = LocalDateTime.now();
        } else if (startDate == null && endDate != null) {
            startDate = LocalDateTime.of(2021, 1, 1, 0, 1);
        } else if (startDate == null && endDate == null) {
            return null;
        }
        LocalDateTime finalStartDate = startDate;
        LocalDateTime finalEndDate = endDate;
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get(createdAtDateTime), finalStartDate, finalEndDate));
    }

    public <E> Specification<T> joinTwoTableValue(Object value, String column, String joinedTableName) {
        if(value == null){
            return null;
        }
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<E, T> joinedTable = root.join(joinedTableName, JoinType.INNER);
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(joinedTable.get(column)),
                    value
            );
        };
    }

    public <E> Specification<T> joinTwoTableValueNotEqual(Object value, String column, String joinedTableName) {
        if(value == null){
            return null;
        }
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<E, T> joinedTable = root.join(joinedTableName, JoinType.INNER);
            return criteriaBuilder.equal(
                    criteriaBuilder.lower(joinedTable.get(column)),
                    value
            ).not();
        };
    }

    public <E,X>Specification<T> joinThreeTableValue(Object value, String column1, String joinedTableName, String joinedTableName1) {
        return (root, criteriaQuery, cb) -> {
            List<Predicate> list = new ArrayList<>();
            Join<E, T> joinedTable = root.join(joinedTableName, JoinType.INNER);
            Join<T, X> joinedTable1 = joinedTable.join(joinedTableName1, JoinType.INNER);
            list.add(cb.and(
                    cb.equal(joinedTable1.get(column1), value)
            ));
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
    }

    public <E> Specification<T> joinTwoTableStringLikeValue(String value, String column, String joinedTableName) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<E, T> accounts = root.join(joinedTableName, JoinType.INNER);
            return criteriaBuilder.like(
                    criteriaBuilder.lower(accounts.get(column)), "%" +
                            value + "%"
            );
        };
    }

    public Specification<T> numberSpecification(Double amountFrom, Double amountTo, String amount) {
        if (amountFrom != null && amountTo == null) {
            amountTo = Double.MAX_VALUE;
        } else if (amountFrom == null && amountTo != null) {
            amountFrom = 0.0;
        } else if (amountFrom == null && amountTo == null) {
            return null;
        }
        Double finalAmountTo = amountTo;
        Double finalAmountFrom = amountFrom;
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.between(root.get(amount), finalAmountFrom, finalAmountTo));
    }

    public Specification<T> byEmailLike(String username) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<T, User> users = root.join("customer", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.lower(users.get("email")), "%" + username + "%");
        };
    }

    public Specification<T> byMobileNumberLike(String username) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            Join<T, User> users = root.join("customer", JoinType.INNER);
            return criteriaBuilder.like(criteriaBuilder.lower(users.get("mobileNumber")), "%" + username + "%");
        };
    }
}
