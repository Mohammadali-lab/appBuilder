package ir.samin.appbuilder.specification;

import ir.samin.appbuilder.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@NoArgsConstructor
@AllArgsConstructor
public class GeneralSpecificationBuilder<T extends BaseEntity> {

    private List<SearchCriteria> params = new ArrayList<>();

    public GeneralSpecificationBuilder<T> with(String key, String operation, Object value, boolean isOrPredicate) {
        params.add(new SearchCriteria(key, operation, value, isOrPredicate));
        return this;
    }

    public Specification<T> build() {
        if (params==null || params.isEmpty()) {
            return null;
        }
        List<Specification<T>> specs = params.stream()
                .map(EntitySpecification<T>::new)
                .collect(Collectors.toList());

        Specification<T> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i)
                    .isOrPredicate()
                    ? Objects.requireNonNull(Specification.where(result)).or(specs.get(i))
                    : Objects.requireNonNull(Specification.where(result)).and(specs.get(i));
        }

        return result;
    }
}
