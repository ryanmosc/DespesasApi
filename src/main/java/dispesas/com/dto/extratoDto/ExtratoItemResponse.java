// ExtratoItemResponse.java
package dispesas.com.dto.extratoDto;

import dispesas.com.model.enumModel.Category;
import dispesas.com.model.enumModel.Status;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExtratoItemResponse(
        Long id,
        String descricao,
        BigDecimal valor,
        Category categoria,
        Status status,
        LocalDate data,
        Integer numeroParcela,    // null se não for parcelado
        Integer totalParcelas,    // null se não for parcelado
        boolean parcelado         // true se installments > 1
) {}