package dispesas.com.service;

import dispesas.com.Repository.DespesaRepository;
import dispesas.com.dto.dashboardDto.ResumoCategoriaResponse;
import dispesas.com.dto.dashboardDto.ResumoMensalResponse;
import dispesas.com.dto.dashboardDto.SaldoResponse;
import dispesas.com.dto.despesaDto.ComparativoMensalResponseDTO;
import dispesas.com.dto.despesaDto.GastosPorMesDTO;
import dispesas.com.dto.despesaDto.Top5CategoriasMesDTO;
import dispesas.com.dto.despesaDto.Top5GastosMesDTO;
import dispesas.com.model.enumModel.Category;
import dispesas.com.model.enumModel.Type;
import dispesas.com.security.utilSecurity.GetUserById;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumoService {

    private final DespesaRepository despesaRepository;
    private final GetUserById getUserById;

    public ResumoMensalResponse resumoMensal(Integer mes, Integer ano) {
        Long userId = getUserById.getUserById().getId();

        BigDecimal totalDespesas = despesaRepository.sumByTypeAndMesAndAno(Type.DESPESA, mes, ano, userId);
        BigDecimal totalReceitas = despesaRepository.sumByTypeAndMesAndAno(Type.RECEITA, mes, ano, userId);
        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new ResumoMensalResponse(mes, ano, totalDespesas, totalReceitas, saldo);
    }

    public List<ResumoCategoriaResponse> resumoPorCategoria(Integer mes, Integer ano) {
        Long userId = getUserById.getUserById().getId();
        return despesaRepository.sumByCategoriaAndMesAndAno(mes, ano, userId)
                .stream()
                .map(row -> new ResumoCategoriaResponse(
                        (Category) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public SaldoResponse saldoGeral() {
        Long userId = getUserById.getUserById().getId();
        BigDecimal totalReceitas = despesaRepository.sumByType(Type.RECEITA, userId);
        BigDecimal totalDespesas = despesaRepository.sumByType(Type.DESPESA, userId);
        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new SaldoResponse(totalReceitas, totalDespesas, saldo);
    }


    public List<GastosPorMesDTO> listarGastoPorMes() {
        Long userId = getUserById.getUserById().getId();
        return despesaRepository.gastosPorMes(userId)
                .stream()
                .map(r -> new GastosPorMesDTO(
                        ((Number) r[0]).intValue(),
                        ((Number) r[1]).intValue(),
                        (BigDecimal) r[2]
                ))
                .toList();
    }


    public List<Top5GastosMesDTO> listarTop5MaioresGastosMes(Integer mes, Integer ano) {
        Long userId = getUserById.getUserById().getId();
        return despesaRepository.top5MaioresGastosMes(mes, ano, userId)
                .stream()
                .map(r -> new Top5GastosMesDTO(
                        (String) r[0],
                        (BigDecimal) r[1]
                ))
                .toList();
    }

    public List<Top5CategoriasMesDTO> listarTop5CategoriasMes(Integer mes, Integer ano) {
        Long userId = getUserById.getUserById().getId();
        return despesaRepository.top5CategoriasMes(mes, ano, userId)
                .stream()
                .map(r -> new Top5CategoriasMesDTO(
                        r[0].toString(),
                        (BigDecimal) r[1]
                ))
                .toList();
    }



    public ComparativoMensalResponseDTO compararMeses(

            Integer mesA, Integer anoA,
            Integer mesB, Integer anoB
    ) {

        Long userId = getUserById.getUserById().getId();
        // Período A
        BigDecimal despesasA = despesaRepository.sumByTypeAndMesAndAno(Type.DESPESA, mesA, anoA, userId);
        BigDecimal receitasA = despesaRepository.sumByTypeAndMesAndAno(Type.RECEITA, mesA, anoA, userId);
        BigDecimal saldoA = receitasA.subtract(despesasA);

        // Período B
        BigDecimal despesasB = despesaRepository.sumByTypeAndMesAndAno(Type.DESPESA, mesB, anoB, userId);
        BigDecimal receitasB = despesaRepository.sumByTypeAndMesAndAno(Type.RECEITA, mesB, anoB, userId);
        BigDecimal saldoB = receitasB.subtract(despesasB);

        // Diferenças absolutas (B - A)
        BigDecimal diferencaDespesas = despesasB.subtract(despesasA);
        BigDecimal diferencaReceitas = receitasB.subtract(receitasA);
        BigDecimal diferencaSaldo = saldoB.subtract(saldoA);

        // Variação percentual — evita divisão por zero
        BigDecimal variacaoDespesas = calcularVariacao(despesasA, despesasB);
        BigDecimal variacaoReceitas = calcularVariacao(receitasA, receitasB);

        return new ComparativoMensalResponseDTO(
                mesA, anoA, despesasA, receitasA, saldoA,
                mesB, anoB, despesasB, receitasB, saldoB,
                diferencaDespesas, diferencaReceitas, diferencaSaldo,
                variacaoDespesas, variacaoReceitas
        );
    }

    // Calcula quanto B variou em relação a A em percentual
    private BigDecimal calcularVariacao(BigDecimal anterior, BigDecimal atual) {
        if (anterior == null || anterior.compareTo(BigDecimal.ZERO) == 0) {
            return null; // evita divisão por zero — sem base de comparação
        }
        return atual.subtract(anterior)
                .divide(anterior, 4, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}