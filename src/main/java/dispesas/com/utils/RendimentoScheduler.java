package dispesas.com.utils;

import dispesas.com.Repository.InvestimentoRepository;
import dispesas.com.model.Investimento;
import dispesas.com.model.enumModel.StatusInvestimento;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RendimentoScheduler {

    private final InvestimentoRepository investimentoRepository;

    @Scheduled(cron = "0 0 0 * * *") // todo dia meia-noite
    @Transactional
    public void calcularRendimentoDiario() {

        // Pula fins de semana — CDB só rende em dias úteis
        DayOfWeek hoje = LocalDate.now().getDayOfWeek();
        if (hoje == DayOfWeek.SATURDAY || hoje == DayOfWeek.SUNDAY) {
            log.info("Final de semana — rendimento não calculado");
            return;
        }

        log.info("Iniciando cálculo de rendimento diário");

        List<Investimento> investimentos = investimentoRepository
                .findByStatusAndTaxaRendimentoAnualIsNotNull(StatusInvestimento.ATIVO);

        for (Investimento investimento : investimentos) {
            try {
                BigDecimal taxaDiaria = calcularTaxaDiaria(investimento.getTaxaRendimentoAnual());

                BigDecimal rendimentoDia = investimento.getValorAtual()
                        .multiply(taxaDiaria)
                        .setScale(2, RoundingMode.HALF_UP);

                investimento.setValorAtual(investimento.getValorAtual().add(rendimentoDia));
                investimento.setUltimoRendimentoCalculado(LocalDateTime.now());
                investimentoRepository.save(investimento);

                log.info("id={} rendimento={} novoValor={}",
                        investimento.getId(), rendimentoDia, investimento.getValorAtual());

            } catch (Exception e) {
                log.error("Erro no investimento id={}: {}", investimento.getId(), e.getMessage());
            }
        }

        log.info("Cálculo de rendimento diário finalizado");
    }

    // Fórmula de juros compostos: (1 + taxaAnual)^(1/252) - 1
    private BigDecimal calcularTaxaDiaria(BigDecimal taxaAnual) {
        double taxa = taxaAnual
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .doubleValue();
        double taxaDiaria = Math.pow(1 + taxa, 1.0 / 252) - 1;
        return BigDecimal.valueOf(taxaDiaria).setScale(10, RoundingMode.HALF_UP);
    }
}