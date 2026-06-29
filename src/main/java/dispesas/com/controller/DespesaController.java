package dispesas.com.controller;

import dispesas.com.dto.despesaDto.DespesaRequest;
import dispesas.com.dto.despesaDto.DespesaResponse;
import dispesas.com.dto.despesaDto.DespesaUpdateRequest;
import dispesas.com.dto.despesaDto.DespesasComParcelasEmAbertoDTO;
import dispesas.com.model.enumModel.Category;
import dispesas.com.model.enumModel.Status;
import dispesas.com.model.enumModel.Type;
import dispesas.com.service.DespesaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/despesas")
@RequiredArgsConstructor
public class DespesaController {

    private final DespesaService despesaService;

    @PostMapping
    public ResponseEntity<List<DespesaResponse>> criarDespesa(@RequestBody DespesaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(despesaService.criarDespesa(request));
    }

    @GetMapping
    public ResponseEntity<Page<DespesaResponse>> listarDespesas(
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {

        Page<DespesaResponse> responses = despesaService.listarDespesas(pageable);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DespesaResponse> listarDespesaId(@PathVariable Long id){
        DespesaResponse response = despesaService.listarDespesaId(id);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> atualzizarDespesa(@PathVariable Long id, @RequestBody DespesaUpdateRequest request){
        despesaService.atualizarDespesa(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarDespesa(@PathVariable Long id){
        despesaService.deletarDespesa(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/filtrar")
    public ResponseEntity<List<DespesaResponse>> filtrarDespesas(
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) Status status,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim
    ) {
        return ResponseEntity.ok(despesaService.filtrarDespesas(type, category, status, dataInicio, dataFim));
    }

    @PostMapping("/{id}/duplicar")
    public ResponseEntity<DespesaResponse> duplicarDespesa(@PathVariable Long id){
        DespesaResponse response = despesaService.duplicarDespesa(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/parcelas-em-aberto")
    public ResponseEntity<List<DespesasComParcelasEmAbertoDTO>> listarDespesasComParcelasEmAberto(){
        return ResponseEntity.ok(despesaService.despesasComParcelasEmAberto());
    }

}
