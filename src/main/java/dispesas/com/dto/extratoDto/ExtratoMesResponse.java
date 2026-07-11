// ExtratoMesResponse.java
package dispesas.com.dto.extratoDto;

import java.math.BigDecimal;
import java.util.List;

public record ExtratoMesResponse(

        // Período
        Integer mes,
        Integer ano,

        // Lançamentos do mês
        List<ExtratoItemResponse> lancamentos,

        // Resumo financeiro
        BigDecimal totalFatura,           // tudo do mês no crédito
        BigDecimal totalPago,             // soma dos PAGO
        BigDecimal totalEmAberto,         // soma dos PENDENTE
        Integer quantidadeLancamentos,    // total de itens
        Integer quantidadeParcelados,     // quantos são parcelados

        // Comprometido futuro
        List<ExtratoParcelaFuturaResponse> parcelasFuturas,
        BigDecimal totalComprometido      // soma de todos os totalRestante
) {}