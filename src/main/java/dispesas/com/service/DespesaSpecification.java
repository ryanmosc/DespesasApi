package dispesas.com.service;

import dispesas.com.model.Despesa;
import dispesas.com.model.enumModel.Category;
import dispesas.com.model.enumModel.OrdenacaoDespesa;
import dispesas.com.model.enumModel.Status;
import dispesas.com.model.enumModel.Type;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DespesaSpecification {

    public static Specification<Despesa> filtrar(
            Long userId,
            Type type,
            Category category,
            Status status,
            LocalDate dataInicio,
            LocalDate dataFim,
            OrdenacaoDespesa ordenacaoDespesa
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            predicates.add(
                    cb.equal(root.get("user").get("id"), userId)
            );

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

            if (ordenacaoDespesa != null) {
                switch (ordenacaoDespesa) {
                    case MAIS_ANTIGAS ->
                            query.orderBy(cb.asc(root.get("createdAt")));

                    case MAIS_RECENTES ->
                            query.orderBy(cb.desc(root.get("createdAt")));

                    case MAIOR_VALOR ->
                            query.orderBy(cb.desc(root.get("value")));

                    case MENOR_VALOR ->
                            query.orderBy(cb.asc(root.get("value")));
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}