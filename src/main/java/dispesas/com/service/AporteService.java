package dispesas.com.service;

import dispesas.com.Repository.AporteRepository;
import dispesas.com.Repository.InvestimentoRepository;
import dispesas.com.dto.aporteDTO.AporteRequest;
import dispesas.com.dto.aporteDTO.AporteResponse;
import dispesas.com.model.Aporte;
import dispesas.com.model.Investimento;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AporteService {

    private final AporteRepository aporteRepository;
    private final InvestimentoRepository investimentoRepository;

    private AporteResponse toResponse(Aporte aporte) {
        return new AporteResponse(
                aporte.getId(),
                aporte.getValor(),
                aporte.getData(),
                aporte.getCreatedAt()
        );
    }

    @Transactional
    public AporteResponse registrarAporte(Long investimentoId, AporteRequest request) {
        Investimento investimento = investimentoRepository.findById(investimentoId)
                .orElseThrow(() -> new EntityNotFoundException("Investimento não encontrado"));

        Aporte aporte = new Aporte();
        aporte.setInvestimento(investimento);
        aporte.setValor(request.valor());
        aporte.setData(request.data());

        Aporte salvo = aporteRepository.save(aporte);

        // Atualiza o valorAtual do investimento somando o novo aporte
        investimento.setValorAtual(investimento.getValorAtual().add(request.valor()));
        investimentoRepository.save(investimento);

        return toResponse(salvo);
    }

    public List<AporteResponse> listarAportesPorInvestimento(Long investimentoId) {
        if (!investimentoRepository.existsById(investimentoId)) {
            throw new EntityNotFoundException("Investimento não encontrado");
        }
        return aporteRepository.findByInvestimentoIdOrderByDataDesc(investimentoId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void deletarAporte(Long aporteId) {
        Aporte aporte = aporteRepository.findById(aporteId)
                .orElseThrow(() -> new EntityNotFoundException("Aporte não encontrado"));

        // Remove o valor do aporte do valorAtual do investimento
        Investimento investimento = aporte.getInvestimento();
        investimento.setValorAtual(investimento.getValorAtual().subtract(aporte.getValor()));
        investimentoRepository.save(investimento);

        aporteRepository.delete(aporte);
    }
}