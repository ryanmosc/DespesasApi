package dispesas.com.dto.dashboardDto;

import java.math.BigDecimal;

public record ResumoMensalResponse(
        Integer mes,
        Integer ano,
        BigDecimal totalDespesas,
        BigDecimal totalReceitas,
        BigDecimal saldo
) {}