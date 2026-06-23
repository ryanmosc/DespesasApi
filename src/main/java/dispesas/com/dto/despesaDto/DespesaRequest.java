package dispesas.com.dto.despesaDto;

import dispesas.com.model.enumModel.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record DespesaRequest(

        String description,
        BigDecimal value,
        Type type,
        Category category,
        PaymentMethod paymentMethod,
        Status status,
        LocalDate expenseDate,
        Integer installments,
        Integer installmentNumber,
        boolean recurrent

) {}