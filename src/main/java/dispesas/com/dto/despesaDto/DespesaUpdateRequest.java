package dispesas.com.dto.despesaDto;

import dispesas.com.model.enumModel.Category;
import dispesas.com.model.enumModel.PaymentMethod;
import dispesas.com.model.enumModel.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaUpdateRequest(

        String description,
        BigDecimal value,
        Category category,
        PaymentMethod paymentMethod,
        Status status,
        LocalDate expenseDate,
        Integer installments,
        Integer installmentNumber,
        Boolean recurrent
) {
}
