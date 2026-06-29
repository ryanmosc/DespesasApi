package dispesas.com.Repository;

import dispesas.com.model.Investimento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvestimentoRepository extends JpaRepository<Investimento, Long> {

    Page<Investimento> findByUsuarioId(Long userId, Pageable pageable);
    Optional<Investimento> findByIdAndUsuarioId(Long id, Long userId);
    void deleteByIdAndUsuarioId(Long id, Long userId);
}
