package dispesas.com.service;

import dispesas.com.Repository.DespesaRepository;
import dispesas.com.dto.dashboardDto.ResumoCategoriaResponse;
import dispesas.com.dto.dashboardDto.ResumoMensalResponse;
import dispesas.com.dto.dashboardDto.SaldoResponse;
import dispesas.com.dto.despesaDto.GastosPorMesDTO;
import dispesas.com.dto.despesaDto.Top5CategoriasMesDTO;
import dispesas.com.dto.despesaDto.Top5GastosMesDTO;
import dispesas.com.model.enumModel.Category;
import dispesas.com.model.enumModel.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResumoService {

    private final DespesaRepository despesaRepository;

    public ResumoMensalResponse resumoMensal(Integer mes, Integer ano) {
        BigDecimal totalDespesas = despesaRepository.sumByTypeAndMesAndAno(Type.DESPESA, mes, ano);
        BigDecimal totalReceitas = despesaRepository.sumByTypeAndMesAndAno(Type.RECEITA, mes, ano);
        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new ResumoMensalResponse(mes, ano, totalDespesas, totalReceitas, saldo);
    }

    public List<ResumoCategoriaResponse> resumoPorCategoria(Integer mes, Integer ano) {
        return despesaRepository.sumByCategoriaAndMesAndAno(mes, ano)
                .stream()
                .map(row -> new ResumoCategoriaResponse(
                        (Category) row[0],
                        (BigDecimal) row[1]
                ))
                .toList();
    }

    public SaldoResponse saldoGeral() {
        BigDecimal totalReceitas = despesaRepository.sumByType(Type.RECEITA);
        BigDecimal totalDespesas = despesaRepository.sumByType(Type.DESPESA);
        BigDecimal saldo = totalReceitas.subtract(totalDespesas);

        return new SaldoResponse(totalReceitas, totalDespesas, saldo);
    }


    public List<GastosPorMesDTO> listarGastoPorMes() {

        return despesaRepository.gastosPorMes()
                .stream()
                .map(r -> new GastosPorMesDTO(
                        ((Number) r[0]).intValue(),
                        ((Number) r[1]).intValue(),
                        (BigDecimal) r[2]
                ))
                .toList();
    }


    public List<Top5GastosMesDTO> listarTop5MaioresGastosMes(Integer mes, Integer ano) {
        return despesaRepository.top5MaioresGastosMes(mes, ano)
                .stream()
                .map(r -> new Top5GastosMesDTO(
                        (String) r[0],
                        (BigDecimal) r[1]
                ))
                .toList();
    }

    public List<Top5CategoriasMesDTO> listarTop5CategoriasMes(Integer mes, Integer ano) {
        return despesaRepository.top5CategoriasMes(mes, ano)
                .stream()
                .map(r -> new Top5CategoriasMesDTO(
                        r[0].toString(),
                        (BigDecimal) r[1]
                ))
                .toList();
    }
}