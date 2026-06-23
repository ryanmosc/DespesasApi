package dispesas.com.dto.dashboardDto;

import java.math.BigDecimal;

public record SaldoResponse(
        BigDecimal totalReceitas,
        BigDecimal totalDespesas,
        BigDecimal saldo
) {}
