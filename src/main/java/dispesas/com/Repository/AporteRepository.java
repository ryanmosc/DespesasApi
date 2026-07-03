package dispesas.com.Repository;

import dispesas.com.model.Aporte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface AporteRepository extends JpaRepository<Aporte, Long> {

    List<Aporte> findByInvestimentoIdOrderByDataDesc(Long investimentoId);

    @Query("""
            SELECT COALESCE(SUM(a.valor), 0)
            FROM Aporte a
            WHERE a.investimento.id = :investimentoId
            """)
    BigDecimal somarAportesPorInvestimento(@Param("investimentoId") Long investimentoId);
}