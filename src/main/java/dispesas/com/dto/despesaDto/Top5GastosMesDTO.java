package dispesas.com.dto.despesaDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record Top5GastosMesDTO(
        String descricao,
        BigDecimal total

){
}
