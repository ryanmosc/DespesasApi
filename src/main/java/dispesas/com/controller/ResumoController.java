package dispesas.com.controller;

import dispesas.com.dto.dashboardDto.ResumoCategoriaResponse;
import dispesas.com.dto.dashboardDto.ResumoMensalResponse;
import dispesas.com.dto.dashboardDto.SaldoResponse;
import dispesas.com.dto.despesaDto.ComparativoMensalResponseDTO;
import dispesas.com.dto.despesaDto.GastosPorMesDTO;
import dispesas.com.dto.despesaDto.Top5CategoriasMesDTO;
import dispesas.com.dto.despesaDto.Top5GastosMesDTO;
import dispesas.com.service.ResumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/despesas")
@RequiredArgsConstructor
public class ResumoController {

    private final ResumoService resumoService;

    @GetMapping("/resumo/mensal")
    public ResponseEntity<ResumoMensalResponse> resumoMensal(
            @RequestParam Integer mes,
            @RequestParam Integer ano
    ) {
        return ResponseEntity.ok(resumoService.resumoMensal(mes, ano));
    }

    @GetMapping("/resumo/por-categoria")
    public ResponseEntity<List<ResumoCategoriaResponse>> resumoPorCategoria(
            @RequestParam Integer mes,
            @RequestParam Integer ano
    ) {
        return ResponseEntity.ok(resumoService.resumoPorCategoria(mes, ano));
    }

    @GetMapping("/resumo/saldo")
    public ResponseEntity<SaldoResponse> saldoGeral() {
        return ResponseEntity.ok(resumoService.saldoGeral());
    }

    @GetMapping("/resumo/relatorios/gastos-por-mes")
    public ResponseEntity<List<GastosPorMesDTO>> gastoPorMes(){
        return ResponseEntity.ok(resumoService.listarGastoPorMes());
    }


    @GetMapping("/resumo/top-5-gastos")
    public ResponseEntity<List<Top5GastosMesDTO>> top5GastosMes(
            @RequestParam Integer mes,
            @RequestParam Integer ano
    ) {
        return ResponseEntity.ok(resumoService.listarTop5MaioresGastosMes(mes, ano));
    }

    @GetMapping("/resumo/top-5-categorias")
    public ResponseEntity<List<Top5CategoriasMesDTO>> top5CategoriasMes(
            @RequestParam Integer mes,
            @RequestParam Integer ano
    ) {
        return ResponseEntity.ok(resumoService.listarTop5CategoriasMes(mes, ano));
    }

    @GetMapping("/resumo/comparativo")
    public ResponseEntity<ComparativoMensalResponseDTO> compararMeses(
            @RequestParam Integer mesA,
            @RequestParam Integer anoA,
            @RequestParam Integer mesB,
            @RequestParam Integer anoB
    ) {
        return ResponseEntity.ok(resumoService.compararMeses(mesA, anoA, mesB, anoB));
    }
}