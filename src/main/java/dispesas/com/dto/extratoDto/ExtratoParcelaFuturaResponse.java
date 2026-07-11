// ExtratoParcelaFuturaResponse.java
package dispesas.com.dto.extratoDto;

import dispesas.com.model.enumModel.Category;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ExtratoParcelaFuturaResponse(
        Long despesaId,
        String descricao,
        Category categoria,
        BigDecimal valorParcela,
        Integer parcelaAtual,       // ex: 4
        Integer totalParcelas,      // ex: 12
        Integer parcelasRestantes,  // ex: 9
        BigDecimal totalRestante,   // valorParcela × parcelasRestantes
        LocalDate proximoVencimento // data da próxima parcela
) {}