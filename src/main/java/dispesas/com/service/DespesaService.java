package dispesas.com.service;

import dispesas.com.Repository.DespesaRepository;
import dispesas.com.dto.comprovanteDto.ComprovanteResponse;
import dispesas.com.dto.despesaDto.DespesaResponse;
import dispesas.com.dto.despesaDto.DespesaRequest;
import dispesas.com.dto.despesaDto.DespesaUpdateRequest;
import dispesas.com.dto.despesaDto.DespesasComParcelasEmAbertoDTO;
import dispesas.com.model.Comprovante;
import dispesas.com.model.Despesa;
import dispesas.com.model.enumModel.Category;
import dispesas.com.model.enumModel.PaymentMethod;
import dispesas.com.model.enumModel.Status;
import dispesas.com.model.enumModel.Type;
import dispesas.com.security.config.SecurityUtil;
import dispesas.com.security.model.User;
import dispesas.com.security.utilSecurity.GetUserById;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DespesaService {

    private final DespesaRepository despesaRepository;
    private final GetUserById getUserById;



    private ComprovanteResponse toComprovanteResponse(Comprovante comprovante) {
        return new ComprovanteResponse(
                comprovante.getId(),
                comprovante.getFileName(),
                comprovante.getFileType(),
                comprovante.getFileSizeBytes(),
                comprovante.getCreatedAt()
        );
    }

    //Metodo auxiliar para conversão de entidade em response
    private DespesaResponse toResponse(Despesa despesa) {

        List<ComprovanteResponse> comprovantes =
                despesa.getComprovantes()
                        .stream()
                        .map(this::toComprovanteResponse)
                        .toList();

        return new DespesaResponse(
                despesa.getId(),
                despesa.getDescription(),
                despesa.getValue(),
                despesa.getType(),
                despesa.getCategory(),
                despesa.getPaymentMethod(),
                despesa.getStatus(),
                despesa.getExpenseDate(),
                despesa.getInstallments(),
                despesa.getInstallmentNumber(),
                despesa.isRecurrent(),
                comprovantes,
                despesa.getCreatedAt(),
                despesa.getUpdatedAt()
        );
    }

    @Transactional
    public DespesaResponse criarDespesa(DespesaRequest request) {
    User user = getUserById.getUserById();
        Despesa despesa = new Despesa(
                null,
                user,
                request.description(),
                request.value(),
                request.type(),
                request.category(),
                request.paymentMethod(),
                request.status(),
                request.expenseDate(),
                request.installments(),
                request.installmentNumber(),
                request.recurrent(),
                List.of(),
                null,
                null
        );

        Despesa salva = despesaRepository.save(despesa);

        return toResponse(salva);
    }

    //Metodo para listar todas as despesas
    public Page<DespesaResponse> listarDespesas(Pageable pageable){
        Long  userId = getUserById.getUserById().getId();
        return despesaRepository.findByUserId(userId, pageable).map(this::toResponse);
    }

    //Metodo para listar despesas por ID
    public DespesaResponse listarDespesaId(Long id){
        Long  userId = getUserById.getUserById().getId();
        return despesaRepository.findByIdAndUserId(id, userId)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
    }


    @Transactional
    //Metodo para atualizar uma despesa
    public void atualizarDespesa(Long id, DespesaUpdateRequest request){
        Long  userId = getUserById.getUserById().getId();
        Despesa despesa = despesaRepository.findById(id).orElseThrow(() -> new RuntimeException("Despesa não encontrada"));
        if(despesa.getUser().getId().equals(userId)){
            if (request.description() != null)
                despesa.setDescription(request.description());

            if (request.value() != null)
                despesa.setValue(request.value());

            if (request.category() != null)
                despesa.setCategory(request.category());

            if (request.paymentMethod() != null)
                despesa.setPaymentMethod(request.paymentMethod());

            if (request.status() != null)
                despesa.setStatus(request.status());

            if (request.expenseDate() != null)
                despesa.setExpenseDate(request.expenseDate());

            if (request.installments() != null)
                despesa.setInstallments(request.installments());

            if (request.installmentNumber() != null)
                despesa.setInstallmentNumber(request.installmentNumber());

            if (request.recurrent() != null)
                despesa.setRecurrent(request.recurrent());

            despesaRepository.save(despesa);
        }
        else {
            throw new RuntimeException("Erro: Você não é dono desta despesa");
        }


    }


    @Transactional
    public void deletarDespesa(Long id) {
        Long userId = SecurityUtil.getCurrentUserId();

        if (!despesaRepository.existsByIdAndUserId(id, userId)) {
            throw new EntityNotFoundException("Despesa não encontrada");
        }

        despesaRepository.deleteByIdAndUserId(id, userId);
    }



    //Filtros
    public List<DespesaResponse> filtrarDespesas(
            Type type,
            Category category,
            Status status,
            LocalDate dataInicio,
            LocalDate dataFim
    ) {
        return despesaRepository
                .findAll(DespesaSpecification.filtrar(type, category, status, dataInicio, dataFim))
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Transactional
    public DespesaResponse duplicarDespesa(Long idDespesa) {
        Long  userId = getUserById.getUserById().getId();
        Despesa despesa = despesaRepository.findById(idDespesa).orElseThrow(() -> new RuntimeException("Despesa não encontrada com o id"));
       if (!despesa.getUser().getId().equals(userId)){
           throw new RuntimeException("Erro: Candidato não é o correto");
       }

        Despesa despesaDuplicada = new Despesa();

        despesaDuplicada.setDescription(despesa.getDescription());
        despesaDuplicada.setValue(despesa.getValue());
        despesaDuplicada.setType(despesa.getType());
        despesaDuplicada.setCategory(despesa.getCategory());
        despesaDuplicada.setPaymentMethod(despesa.getPaymentMethod());
        despesaDuplicada.setStatus(despesa.getStatus());
        despesaDuplicada.setExpenseDate(despesa.getExpenseDate());
        despesaDuplicada.setInstallments(despesa.getInstallments());
        despesaDuplicada.setInstallmentNumber(despesa.getInstallmentNumber());
        despesaDuplicada.setRecurrent(despesa.isRecurrent());
        despesaDuplicada.setComprovantes(new ArrayList<>());

        Despesa salva = despesaRepository.save(despesaDuplicada);
        return toResponse(salva);
    }


    public List<DespesasComParcelasEmAbertoDTO> despesasComParcelasEmAberto(){
        Long  userId = getUserById.getUserById().getId();
        return despesaRepository.despesasComParcelasEmAberto(userId)
                .stream()
                .map(r -> new DespesasComParcelasEmAbertoDTO(
                        ((Number) r[0]).longValue(),
                        r[1].toString(),
                        (BigDecimal) r[2],
                        PaymentMethod.valueOf(r[3].toString()),
                        r[4] == null ? 0 : ((Number) r[4]).intValue(),
                        r[5] == null ? 0 : ((Number) r[5]).intValue()

                ))
                .toList();
    }
}