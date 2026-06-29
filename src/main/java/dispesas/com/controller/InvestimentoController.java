package dispesas.com.controller;

import dispesas.com.dto.investimentoDto.InvestimentoResponse;
import dispesas.com.dto.investimentoDto.InvestimentosRequest;
import dispesas.com.service.InvestimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/investimentos")
@RequiredArgsConstructor
public class InvestimentoController {
    private final InvestimentoService service;

    @PostMapping
    public ResponseEntity<InvestimentoResponse> criarInvestimento(@RequestBody InvestimentosRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(        service.criarInvestimento(request));
    }


    @GetMapping
    public ResponseEntity<Page<InvestimentoResponse>> criarInvestimento(@PageableDefault(size = 10, sort = "id") Pageable pageable){
        Page <InvestimentoResponse> responses = service.listarInvestimentos(pageable);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestimentoResponse> listarInvestimentoId(@PathVariable Long id){
        InvestimentoResponse response = service.listarInvestimentoPorId(id);
        return ResponseEntity.ok(response);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<InvestimentoResponse> atualziarInvestimento(@RequestBody InvestimentosRequest request, @PathVariable Long id){
        service.atualizarInvestimento(request, id);
        return ResponseEntity.noContent().build();
    }
}
