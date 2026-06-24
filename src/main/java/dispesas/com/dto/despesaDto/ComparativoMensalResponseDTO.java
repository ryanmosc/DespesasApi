package dispesas.com.dto.despesaDto;

import java.math.BigDecimal;

public record ComparativoMensalResponseDTO(

        // Período A
        Integer mesA,
        Integer anoA,
        BigDecimal totalDespesasA,
        BigDecimal totalReceitasA,
        BigDecimal saldoA,

        // Período B
        Integer mesB,
        Integer anoB,
        BigDecimal totalDespesasB,
        BigDecimal totalReceitasB,
        BigDecimal saldoB,

        // Diferenças (B - A)
        BigDecimal diferencaDespesas,
        BigDecimal diferencaReceitas,
        BigDecimal diferencaSaldo,

        // Variação percentual
        BigDecimal variacaoDespesasPercent,
        BigDecimal variacaoReceitasPercent
) {
}
