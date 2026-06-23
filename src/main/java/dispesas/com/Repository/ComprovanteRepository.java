package dispesas.com.Repository;

import dispesas.com.model.Comprovante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComprovanteRepository extends JpaRepository<Comprovante, Long> {
}
