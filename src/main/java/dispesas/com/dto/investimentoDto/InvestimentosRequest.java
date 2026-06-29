package dispesas.com.dto.investimentoDto;

import dispesas.com.model.enumModel.StatusInvestimento;
import dispesas.com.model.enumModel.TipoInvestimento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record InvestimentosRequest(



        String nome,
        TipoInvestimento tipo,
        BigDecimal valorInicial,
        BigDecimal valorAtual,
        LocalDate dataInicio,
        LocalDate dataVencimento,
        String instituicao,
        StatusInvestimento status,
        Long usuarioId,
        LocalDateTime criadoEm,
        LocalDateTime atualizadoEm
) {
}
