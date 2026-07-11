// ExtratoService.java
package dispesas.com.service;

import dispesas.com.Repository.DespesaRepository;
import dispesas.com.dto.extratoDto.ExtratoItemResponse;
import dispesas.com.dto.extratoDto.ExtratoMesResponse;
import dispesas.com.dto.extratoDto.ExtratoParcelaFuturaResponse;
import dispesas.com.model.Despesa;
import dispesas.com.model.enumModel.Status;
import dispesas.com.security.utilSecurity.GetUserById;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExtratoService {

    private final DespesaRepository despesaRepository;
    private final GetUserById getUserById;

    @Transactional(readOnly = true)
    public ExtratoMesResponse gerarExtrato(Integer mes, Integer ano) {

        Long usuarioId = getUserById.getUserById().getId();

        // 1. Busca lançamentos do mês
        List<Despesa> lancamentos = despesaRepository
                .findLancamentosCredito(usuarioId, mes, ano);

        // 2. Busca totais
        BigDecimal totalPago = despesaRepository
                .sumCreditoByStatus(usuarioId, Status.PAGO, mes, ano);
        BigDecimal totalEmAberto = despesaRepository
                .sumCreditoByStatus(usuarioId, Status.PENDENTE, mes, ano);
        BigDecimal totalFatura = totalPago.add(totalEmAberto);

        // 3. Conta parcelados
        long quantidadeParcelados = lancamentos.stream()
                .filter(d -> d.getInstallments() != null && d.getInstallments() > 1)
                .count();

        // 4. Busca parcelas futuras comprometidas
        List<Despesa> futuras = despesaRepository
                .findParcelasFuturas(usuarioId, mes, ano);

        // 5. Monta lista de parcelas futuras
        List<ExtratoParcelaFuturaResponse> parcelasFuturas = futuras.stream()
                .map(this::toParcelaFuturaResponse)
                .toList();

        // 6. Soma total comprometido
        BigDecimal totalComprometido = parcelasFuturas.stream()
                .map(ExtratoParcelaFuturaResponse::totalRestante)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new ExtratoMesResponse(
                mes,
                ano,
                lancamentos.stream().map(this::toItemResponse).toList(),
                totalFatura,
                totalPago,
                totalEmAberto,
                lancamentos.size(),
                (int) quantidadeParcelados,
                parcelasFuturas,
                totalComprometido
        );
    }

    private ExtratoItemResponse toItemResponse(Despesa d) {
        return new ExtratoItemResponse(
                d.getId(),
                d.getDescription(),
                d.getValue(),
                d.getCategory(),
                d.getStatus(),
                d.getExpenseDate(),
                d.getInstallmentNumber(),
                d.getInstallments(),
                d.getInstallments() != null && d.getInstallments() > 1
        );
    }

    private ExtratoParcelaFuturaResponse toParcelaFuturaResponse(Despesa d) {
        int parcelaAtual = d.getInstallmentNumber() != null ? d.getInstallmentNumber() : 1;
        int totalParcelas = d.getInstallments() != null ? d.getInstallments() : 1;
        int parcelasRestantes = totalParcelas - parcelaAtual;
        BigDecimal totalRestante = d.getValue()
                .multiply(BigDecimal.valueOf(parcelasRestantes));

        return new ExtratoParcelaFuturaResponse(
                d.getId(),
                d.getDescription(),
                d.getCategory(),
                d.getValue(),
                parcelaAtual,
                totalParcelas,
                parcelasRestantes,
                totalRestante,
                d.getExpenseDate()
        );
    }
}