package dispesas.com.utils.investimentoUtil;

import java.math.BigDecimal;

public record RestApiDTO(
        String nome,
        BigDecimal valor

) {
}
