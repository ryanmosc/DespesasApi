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
import java.util.Optional;

public interface DespesaRepository extends JpaRepository<Despesa, Long>, JpaSpecificationExecutor<Despesa> {



    Page<Despesa> findByUserId(Long userId, Pageable pageable);

    Optional<Despesa> findByIdAndUserId(Long id, Long userId);

    void deleteByIdAndUserId(Long id, Long userId);

    boolean existsByIdAndUserId(Long id, Long userId);

    // Soma total por tipo em um mês/ano — só conta PAGO
    @Query("""
        SELECT COALESCE(SUM(d.value), 0)
        FROM Despesa d
        WHERE d.type = :type
        AND d.user.id = :userId
        AND (d.installments <= 1 OR d.status = 'PAGO')
        AND EXTRACT(MONTH FROM d.expenseDate) = :mes
        AND EXTRACT(YEAR FROM d.expenseDate) = :ano
        """)
    BigDecimal sumByTypeAndMesAndAno(
            @Param("type") Type type,
            @Param("mes") Integer mes,
            @Param("ano") Integer ano,
            @Param("userId") Long userId
    );

    // Soma agrupada por categoria em um mês/ano
    @Query("""
            SELECT d.category, SUM(d.value)
            FROM Despesa d
            WHERE d.type = 'DESPESA'
            AND d.user.id = :userId
            AND EXTRACT(MONTH FROM d.expenseDate) = :mes
            AND EXTRACT(YEAR FROM d.expenseDate) = :ano
            GROUP BY d.category
            """)
    List<Object[]> sumByCategoriaAndMesAndAno(
            @Param("mes") Integer mes,
            @Param("ano") Integer ano,
            @Param("userId") Long userId
    );


    // Soma total por tipo (saldo geral) — só conta PAGO
    @Query("""
        SELECT COALESCE(SUM(d.value), 0)
        FROM Despesa d
        WHERE d.type = :type
        AND d.user.id = :userId
        AND (d.installments <= 1 OR d.status = 'PAGO')
        """)
    BigDecimal sumByType(@Param("type") Type type,
                         @Param("userId") Long userId);



    @Query(value = """
    SELECT
        EXTRACT(YEAR FROM expense_date) AS ano,
        EXTRACT(MONTH FROM expense_date) AS mes,
        SUM(value) AS total
    FROM despesas
    WHERE type = 'DESPESA'
    AND usuario_id = :userId
    GROUP BY EXTRACT(YEAR FROM expense_date), EXTRACT(MONTH FROM expense_date)
    ORDER BY EXTRACT(YEAR FROM expense_date), EXTRACT(MONTH FROM expense_date)
""", nativeQuery = true)
    List<Object[]> gastosPorMes(@Param("userId") Long userId);



    // Repository — adicionar parâmetros
    @Query(value = """
    SELECT description, value, expense_date
    FROM despesas
    WHERE type = 'DESPESA'
      AND usuario_id = :userId
      AND EXTRACT(YEAR FROM expense_date) = :ano
      AND EXTRACT(MONTH FROM expense_date) = :mes
    ORDER BY value DESC
    LIMIT 5
""", nativeQuery = true)
    List<Object[]> top5MaioresGastosMes(@Param("mes") Integer mes, @Param("ano") Integer ano, @Param("userId") Long userId
    );

    @Query(value = """
    SELECT category, SUM(value) AS total
    FROM despesas
    WHERE type = 'DESPESA'
      AND usuario_id = :userId
      AND EXTRACT(YEAR FROM expense_date) = :ano
      AND EXTRACT(MONTH FROM expense_date) = :mes
    GROUP BY category
    ORDER BY total DESC
    LIMIT 5
""", nativeQuery = true)
    List<Object[]> top5CategoriasMes(@Param("mes") Integer mes, @Param("ano") Integer ano, @Param("userId") Long userId
    );


    @Query(value = """
    SELECT
        MIN(id) AS id,
        SPLIT_PART(MIN(description), ' (', 1) AS description,
        SUM(value) AS total,
        payment_method,
        installments,
        COUNT(CASE WHEN status = 'PAGO' THEN 1 END) AS parcelas_pagas
    FROM despesas
    WHERE installments > 1
    AND usuario_id = :userId
    GROUP BY SPLIT_PART(description, ' (', 1), payment_method, installments
    ORDER BY parcelas_pagas ASC
""", nativeQuery = true)
    List<Object[]> despesasComParcelasEmAberto(@Param("userId") Long userId);
}
