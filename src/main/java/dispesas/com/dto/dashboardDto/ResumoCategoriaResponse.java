package dispesas.com.dto.dashboardDto;


import dispesas.com.model.enumModel.Category;
import java.math.BigDecimal;

public record ResumoCategoriaResponse(
        Category category,
        BigDecimal total
) {}