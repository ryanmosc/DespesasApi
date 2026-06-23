package dispesas.com.dto.despesaDto;

import java.math.BigDecimal;

public record Top5CategoriasMesDTO(
        String categoria,
        BigDecimal total
) {
}
