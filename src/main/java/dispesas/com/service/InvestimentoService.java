package dispesas.com.service;

import dispesas.com.Repository.InvestimentoRepository;
import dispesas.com.Repository.UserRepository;
import dispesas.com.dto.investimentoDto.InvestimentoResponse;
import dispesas.com.dto.investimentoDto.InvestimentosRequest;
import dispesas.com.infra.exception.geral.AcessoNegadoException;
import dispesas.com.infra.exception.investimento.InvestimentoNaoEncontradoException;
import dispesas.com.model.Investimento;
import dispesas.com.model.enumModel.StatusInvestimento;
import dispesas.com.security.model.User;
import dispesas.com.security.utilSecurity.GetUserById;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final GetUserById getUserById;
    private final UserRepository userRepository;


    private InvestimentoResponse investimentoResponse(Investimento investimento) {

        BigDecimal rendimentoAbsoluto = investimento.getValorAtual()
                .subtract(investimento.getValorInicial());

        BigDecimal rendimentoPercent = investimento.getValorInicial()
                .compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : rendimentoAbsoluto
                .divide(investimento.getValorInicial(), 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);

        return new InvestimentoResponse(
                investimento.getId(),
                investimento.getNome(),
                investimento.getTipo(),
                investimento.getValorInicial(),
                investimento.getValorAtual(),
                investimento.getPercentualCdi(),
                rendimentoAbsoluto,
                rendimentoPercent,
                investimento.getDataInicio(),
                investimento.getDataVencimento(),
                investimento.getInstituicao(),
                investimento.getStatus(),
                investimento.getUltimoRendimentoCalculado(),
                investimento.getCriadoEm(),
                investimento.getAtualizadoEm()
        );
    }

    @Transactional
    public InvestimentoResponse criarInvestimento(InvestimentosRequest request){

        User user = getUserById.getUserById();

        Investimento investimento = new Investimento(
                null,
                request.nome(),
                request.tipo(),
                request.valorInicial(),
                request.valorAtual(),
                request.dataInicio(),
                request.dataVencimento(),
                request.instituicao(),
                request.percentualCdi(),
                null,
                request.status(),
                user,
                request.criadoEm(),
                null
        );

        investimentoRepository.save(investimento);
        return investimentoResponse(investimento);
    }


    public Page<InvestimentoResponse> listarInvestimentos(Pageable pageable){
        Long userId = getUserById.getUserById().getId();
        return investimentoRepository.findByUsuarioId(userId, pageable).map(this::investimentoResponse);
    }

    public InvestimentoResponse listarInvestimentoPorId(Long idInvestimento){
        Long userId = getUserById.getUserById().getId();
        return investimentoRepository.findByIdAndUsuarioId(idInvestimento, userId)
                .map(this::investimentoResponse)
                .orElseThrow(() ->  new InvestimentoNaoEncontradoException("Investimento não encontrado."));
    }

    @Transactional
    public void atualizarInvestimento(InvestimentosRequest request, Long investimentoId) {
    Investimento investimento = investimentoRepository.findById(investimentoId).orElseThrow(() ->  new InvestimentoNaoEncontradoException("Investimento não encontrado."));
    Long userId = getUserById.getUserById().getId();

    if (!investimento.getUsuario().getId().equals(userId)){
        throw new AcessoNegadoException("Você não possui acesso a este investimento.");
    }

    if (request.nome() != null) {
        investimento.setNome(request.nome());
    }
    if (request.tipo() != null) {
        investimento.setTipo(request.tipo());
    }
    if (request.valorInicial() != null) {
        investimento.setValorInicial(request.valorInicial());
    }
    if (request.valorAtual() != null) {
        investimento.setValorAtual(request.valorAtual());
    }
    if (request.dataInicio() != null) {
        investimento.setDataInicio(request.dataInicio());
    }
    if (request.dataVencimento() != null) {
        investimento.setDataVencimento(request.dataVencimento());
    }
    if (request.instituicao() != null) {
        investimento.setInstituicao(request.instituicao());
    }
    if (request.percentualCdi() != null)
            investimento.setPercentualCdi(request.percentualCdi());
    if (request.status() != null) {
        investimento.setStatus(request.status());
    }
    investimento.setAtualizadoEm(LocalDateTime.now());
    investimentoRepository.save(investimento);
  }

  @Transactional
  public void modificarStatus(StatusInvestimento statusInvestimento, Long idInvestimento){
      Investimento investimento = investimentoRepository.findById(idInvestimento).orElseThrow(() -> new InvestimentoNaoEncontradoException("Investimento não encontrado."));
      Long userId = getUserById.getUserById().getId();

      if (!investimento.getUsuario().getId().equals(userId)){
          throw new AcessoNegadoException("Você não possui acesso a este investimento.");
      }
      investimento.setStatus(statusInvestimento);
      investimentoRepository.save(investimento);
  }

  @Transactional
  public void deletarInvestimento(Long idInvestimento){
        Long userId = getUserById.getUserById().getId();
        investimentoRepository.deleteByIdAndUsuarioId(idInvestimento, userId);
  }
}
