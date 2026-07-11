package dispesas.com.controller;

import dispesas.com.dto.extratoDto.ExtratoMesResponse;
import dispesas.com.service.ExtratoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/despesas")
@RequiredArgsConstructor
public class ExtratoController {

    private final ExtratoService extratoService;

    @GetMapping("/extrato")
    public ResponseEntity<ExtratoMesResponse> gerarExtrato(
            @RequestParam Integer mes,
            @RequestParam Integer ano
    ) {
        return ResponseEntity.ok(extratoService.gerarExtrato(mes, ano));
    }
}