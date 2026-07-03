package dispesas.com.controller;

import dispesas.com.dto.aporteDTO.AporteRequest;
import dispesas.com.dto.aporteDTO.AporteResponse;
import dispesas.com.service.AporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investimentos")
@RequiredArgsConstructor
public class AporteController {

    private final AporteService aporteService;

    @PostMapping("/{id}/aportes")
    public ResponseEntity<AporteResponse> registrarAporte(
            @PathVariable Long id,
            @RequestBody AporteRequest request
    ) {
        AporteResponse response = aporteService.registrarAporte(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}/aportes")
    public ResponseEntity<List<AporteResponse>> listarAportes(@PathVariable Long id) {
        return ResponseEntity.ok(aporteService.listarAportesPorInvestimento(id));
    }

    @DeleteMapping("/aportes/{aporteId}")
    public ResponseEntity<Void> deletarAporte(@PathVariable Long aporteId) {
        aporteService.deletarAporte(aporteId);
        return ResponseEntity.noContent().build();
    }
}