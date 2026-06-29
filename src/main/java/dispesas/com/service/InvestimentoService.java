package dispesas.com.service;

import dispesas.com.Repository.InvestimentoRepository;
import dispesas.com.Repository.UserRepository;
import dispesas.com.dto.investimentoDto.InvestimentoResponse;
import dispesas.com.dto.investimentoDto.InvestimentosRequest;
import dispesas.com.model.Investimento;
import dispesas.com.security.model.User;
import dispesas.com.security.utilSecurity.GetUserById;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvestimentoService {

    private final InvestimentoRepository investimentoRepository;
    private final GetUserById getUserById;
    private final UserRepository userRepository;


    private InvestimentoResponse investimentoResponse(Investimento investimento){
        return new InvestimentoResponse(
                investimento.getId(),
                investimento.getNome(),
                investimento.getTipo(),
                investimento.getValorInicial(),
                investimento.getValorAtual(),
                investimento.getDataInicio(),
                investimento.getDataVencimento(),
                investimento.getInstituicao(),
                investimento.getStatus(),
                investimento.getCriadoEm(),
                investimento.getAtualizadoEm()

        );
    }

    @Transactional
    public InvestimentoResponse criarInvestimento(InvestimentosRequest request){

        //Usuario Mocado para testes, depois alterar
        User user = userRepository.findById(1L)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Investimento investimento = new Investimento(
                null,
                request.nome(),
                request.tipo(),
                request.valorInicial(),
                request.valorAtual(),
                request.dataInicio(),
                request.dataVencimento(),
                request.instituicao(),
                request.status(),
                user,
                request.criadoEm(),
                null
        );

        investimentoRepository.save(investimento);
        return investimentoResponse(investimento);
    }


    public Page<InvestimentoResponse> listarInvestimentos(Pageable pageable){
        return investimentoRepository.findAll(pageable).map(this::investimentoResponse);
    }

    public InvestimentoResponse listarInvestimentoPorId(Long idInvestimento){
        return investimentoRepository.findById(idInvestimento)
                .map(this::investimentoResponse)
                .orElseThrow(() -> new RuntimeException("Investimento não encontrado"));
    }


}
