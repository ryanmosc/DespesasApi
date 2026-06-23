package dispesas.com.service;

import dispesas.com.model.Despesa;
import dispesas.com.model.enumModel.Category;
import dispesas.com.model.enumModel.Status;
import dispesas.com.model.enumModel.Type;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DespesaSpecification {

    public static Specification<Despesa> filtrar(
            Type type,
            Category category,
            Status status,
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (type != null)
                predicates.add(cb.equal(root.get("type"), type));

            if (category != null)
                predicates.add(cb.equal(root.get("category"), category));

            if (status != null)
                predicates.add(cb.equal(root.get("status"), status));

            if (dataInicio != null && dataFim != null)
                predicates.add(cb.between(root.get("expenseDate"), dataInicio, dataFim));
            else if (dataInicio != null)
                predicates.add(cb.greaterThanOrEqualTo(root.get("expenseDate"), dataInicio));
            else if (dataFim != null)
                predicates.add(cb.lessThanOrEqualTo(root.get("expenseDate"), dataFim));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}