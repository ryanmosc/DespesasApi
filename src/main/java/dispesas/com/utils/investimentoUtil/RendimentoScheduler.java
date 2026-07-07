package dispesas.com.utils.investimentoUtil;

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
    private final RestApiCdi restApiCdi;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void calcularRendimentoDiario() {
        DayOfWeek hoje = LocalDate.now().getDayOfWeek();
        if (hoje == DayOfWeek.SATURDAY || hoje == DayOfWeek.SUNDAY) {
            log.info("Final de semana — rendimento não calculado");
            return;
        }

        // Busca CDI e Selic UMA vez para todos os investimentos
        BigDecimal cdiAtual = restApiCdi.getCdi();
        BigDecimal selicAtual = restApiCdi.getSelic();

        if (cdiAtual == null || cdiAtual.compareTo(BigDecimal.ZERO) == 0) {
            log.error("CDI não encontrado na BrasilAPI — cálculo abortado");
            return;
        }
        if (selicAtual == null || selicAtual.compareTo(BigDecimal.ZERO) == 0) {
            log.warn("Selic não encontrada na BrasilAPI — investimentos sem percentualCdi usarão CDI como fallback");
            selicAtual = cdiAtual; // fallback: CDI ≈ Selic na prática
        }

        log.info("CDI: {}% | Selic: {}%", cdiAtual, selicAtual);

        // Busca TODOS os investimentos ativos — com ou sem percentualCdi
        List<Investimento> investimentos = investimentoRepository
                .findByStatus(StatusInvestimento.ATIVO);

        for (Investimento investimento : investimentos) {
            try {
                BigDecimal taxaReal = resolverTaxaReal(investimento, cdiAtual, selicAtual);

                // Se não conseguiu resolver taxa, pula o investimento
                if (taxaReal == null || taxaReal.compareTo(BigDecimal.ZERO) == 0) {
                    log.info("id={} sem taxa configurada — pulando", investimento.getId());
                    continue;
                }

                BigDecimal taxaDiaria = calcularTaxaDiaria(taxaReal);
                BigDecimal rendimentoDia = investimento.getValorAtual()
                        .multiply(taxaDiaria)
                        .setScale(2, RoundingMode.HALF_UP);

                investimento.setValorAtual(investimento.getValorAtual().add(rendimentoDia));
                investimento.setUltimoRendimentoCalculado(LocalDateTime.now());
                investimentoRepository.save(investimento);

                log.info("id={} tipo={} taxaBase={} taxaReal={}% rendimento={} novoValor={}",
                        investimento.getId(),
                        investimento.getTipo(),
                        resolverDescricaoTaxa(investimento),
                        taxaReal.setScale(4, RoundingMode.HALF_UP),
                        rendimentoDia,
                        investimento.getValorAtual());

            } catch (Exception e) {
                log.error("Erro no investimento id={}: {}", investimento.getId(), e.getMessage());
            }
        }

        log.info("Cálculo de rendimento diário finalizado");
    }

    // Decide qual taxa usar para cada investimento
    private BigDecimal resolverTaxaReal(Investimento investimento, BigDecimal cdi, BigDecimal selic) {
        boolean temPercentualCdi = investimento.getPercentualCdi() != null
                && investimento.getPercentualCdi().compareTo(BigDecimal.ZERO) > 0;

        if (temPercentualCdi) {
            // Tem percentual configurado → usa CDI × percentual
            // ex: CDI 10.65% × 115% = 12.2475%
            return cdi
                    .multiply(investimento.getPercentualCdi())
                    .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        }

        // Sem percentual → usa Selic direto (100% da Selic)
        // Aplica para TESOURO_SELIC e qualquer outro sem percentual
        return selic;
    }

    // Só para o log ficar mais claro
    private String resolverDescricaoTaxa(Investimento investimento) {
        boolean temPercentualCdi = investimento.getPercentualCdi() != null
                && investimento.getPercentualCdi().compareTo(BigDecimal.ZERO) > 0;
        return temPercentualCdi
                ? investimento.getPercentualCdi() + "% CDI"
                : "100% Selic";
    }

    // (1 + taxaAnual/100)^(1/252) - 1
    private BigDecimal calcularTaxaDiaria(BigDecimal taxaAnual) {
        double taxa = taxaAnual
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .doubleValue();
        double taxaDiaria = Math.pow(1 + taxa, 1.0 / 252) - 1;
        return BigDecimal.valueOf(taxaDiaria).setScale(10, RoundingMode.HALF_UP);
    }
}