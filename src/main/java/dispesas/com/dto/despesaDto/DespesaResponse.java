package dispesas.com.dto.despesaDto;

import dispesas.com.dto.comprovanteDto.ComprovanteResponse;
import dispesas.com.model.enumModel.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record DespesaResponse(

        Long id,
        String description,
        BigDecimal value,
        Type type,
        Category category,
        PaymentMethod paymentMethod,
        Status status,
        LocalDate expenseDate,
        Integer installments,
        Integer installmentNumber,
        boolean recurrent,
        List<ComprovanteResponse> comprovantes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt

) {}