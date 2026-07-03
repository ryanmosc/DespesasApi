package dispesas.com.dto.aporteDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AporteResponse(Long id,
                             BigDecimal valor,
                             LocalDate data,
                             LocalDateTime createdAt) {
}
