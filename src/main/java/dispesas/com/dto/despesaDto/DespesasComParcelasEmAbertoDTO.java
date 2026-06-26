package dispesas.com.dto.despesaDto;

import dispesas.com.model.enumModel.PaymentMethod;

import java.math.BigDecimal;

public record DespesasComParcelasEmAbertoDTO(

        Long id,
        String description,
        BigDecimal value,
        PaymentMethod paymentMethod,
        Integer installments,
        Integer parcelasPagas
) {
}
