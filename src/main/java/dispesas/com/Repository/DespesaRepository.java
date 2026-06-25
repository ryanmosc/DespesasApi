package dispesas.com.Repository;

import dispesas.com.model.Despesa;
import dispesas.com.model.enumModel.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface DespesaRepository extends JpaRepository<Despesa, Long>, JpaSpecificationExecutor<Despesa> {



    Page<Despesa> findByUserId(Long userId, Pageable pageable);


    // Soma total por tipo em um mês/ano
    @Query("""
            SELECT COALESCE(SUM(d.value), 0)
            FROM Despesa d
            WHERE d.type = :type
            AND EXTRACT(MONTH FROM d.expenseDate) = :mes
            AND EXTRACT(YEAR FROM d.expenseDate) = :ano
            """)
    BigDecimal sumByTypeAndMesAndAno(
            @Param("type") Type type,
            @Param("mes") Integer mes,
            @Param("ano") Integer ano
    );

    // Soma agrupada por categoria em um mês/ano
    @Query("""
            SELECT d.category, SUM(d.value)
            FROM Despesa d
            WHERE d.type = 'DESPESA'
            AND EXTRACT(MONTH FROM d.expenseDate) = :mes
            AND EXTRACT(YEAR FROM d.expenseDate) = :ano
            GROUP BY d.category
            """)
    List<Object[]> sumByCategoriaAndMesAndAno(
            @Param("mes") Integer mes,
            @Param("ano") Integer ano
    );

    // Soma total por tipo (para saldo geral)
    @Query("""
            SELECT COALESCE(SUM(d.value), 0)
            FROM Despesa d
            WHERE d.type = :type
            """)
    BigDecimal sumByType(@Param("type") Type type);



    @Query(value = """
    SELECT
        EXTRACT(YEAR FROM expense_date) AS ano,
        EXTRACT(MONTH FROM expense_date) AS mes,
        SUM(value) AS total
    FROM despesas
    WHERE type = 'DESPESA'
    GROUP BY ano, mes
    ORDER BY ano, mes
""", nativeQuery = true)
    List<Object[]> gastosPorMes();



    // Repository — adicionar parâmetros
    @Query(value = """
    SELECT description, value, expense_date
    FROM despesas
    WHERE type = 'DESPESA'
      AND EXTRACT(YEAR FROM expense_date) = :ano
      AND EXTRACT(MONTH FROM expense_date) = :mes
    ORDER BY value DESC
    LIMIT 5
""", nativeQuery = true)
    List<Object[]> top5MaioresGastosMes(@Param("mes") Integer mes, @Param("ano") Integer ano);

    @Query(value = """
    SELECT category, SUM(value) AS total
    FROM despesas
    WHERE type = 'DESPESA'
      AND EXTRACT(YEAR FROM expense_date) = :ano
      AND EXTRACT(MONTH FROM expense_date) = :mes
    GROUP BY category
    ORDER BY total DESC
    LIMIT 5
""", nativeQuery = true)
    List<Object[]> top5CategoriasMes(@Param("mes") Integer mes, @Param("ano") Integer ano);


    @Query(value = """
    SELECT id, description, value, payment_method, installments, installment_number
    FROM despesas
    WHERE  installments > 0
    ORDER BY installments DESC
""", nativeQuery = true)
    List<Object[]> despesasComParcelasEmAberto();
}
