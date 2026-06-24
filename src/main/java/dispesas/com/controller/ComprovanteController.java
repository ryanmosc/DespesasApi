package dispesas.com.controller;

import dispesas.com.model.Comprovante;
import dispesas.com.service.ComprovanteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comprovante")
public class ComprovanteController {

    private final ComprovanteService comprovanteService;

    @PostMapping("/{id}/despesa")
    public ResponseEntity<Void> salvarComprovante(@RequestParam("comprovante")MultipartFile comprovante, @PathVariable Long id){
        comprovanteService.salvarComprovante(comprovante, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> visualizarComprovanteId(@PathVariable Long id){
        return comprovanteService.visualizarComprovante(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarComprovante(@PathVariable Long id){
        comprovanteService.deletarComprovante(id);
        return ResponseEntity.noContent().build();
    }
}
