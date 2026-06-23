package dispesas.com.dto.despesaDto;

import java.math.BigDecimal;

public record GastosPorMesDTO(

        Integer ano,
        Integer mes,
        BigDecimal total
) {
}
