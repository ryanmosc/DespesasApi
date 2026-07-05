package dispesas.com.Repository;

import dispesas.com.model.Investimento;
import dispesas.com.model.enumModel.StatusInvestimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {

    Page<Investimento> findByUsuarioId(Long userId, Pageable pageable);
    Optional<Investimento> findByIdAndUsuarioId(Long id, Long userId);
    void deleteByIdAndUsuarioId(Long id, Long userId);
    // Busca só investimentos ativos que têm taxa cadastrada
    List<Investimento> findByStatus(StatusInvestimento status);
    List<Investimento> findByStatusAndPercentualCdiIsNotNull(StatusInvestimento status);
}
