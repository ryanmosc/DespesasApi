package dispesas.com.Repository;

import dispesas.com.security.model.CodigoVerificacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodigoVerificacaoRepository extends JpaRepository<CodigoVerificacao, Long> {
    Optional<CodigoVerificacao> findTopByUserIdAndUsadoFalseOrderByIdDesc(Long userId);
}
