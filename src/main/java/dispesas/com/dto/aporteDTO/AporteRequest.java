package dispesas.com.dto.aporteDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AporteRequest(
        BigDecimal valor,
        LocalDate data
) {
}
