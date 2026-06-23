package dispesas.com.service;

import dispesas.com.Repository.ComprovanteRepository;
import dispesas.com.Repository.DespesaRepository;
import dispesas.com.model.Comprovante;
import dispesas.com.model.Despesa;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ComprovanteService {
    private  final DespesaRepository despesaRepository;
    private final ComprovanteRepository comprovanteRepository;

    @Transactional
    public void salvarComprovante(MultipartFile file, Long idDespesa){

        //Validações iniciais
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Nenhum arquivo enviado");
        }
        if (file.getSize() > 5L * 1024 * 1024){
            throw new RuntimeException("Arquivo muito grande. Máximo 5MB");
        }
        Despesa despesa = despesaRepository.findById(idDespesa).orElseThrow(() -> new RuntimeException("Nenhum despesa encontrada"));

        try{
            Comprovante comprovante = new Comprovante();

            comprovante.setDespesa(despesa);
            comprovante.setFileName(file.getOriginalFilename());
            comprovante.setFileType(file.getContentType());
            comprovante.setFile(file.getBytes());
            comprovante.setFileSizeBytes(file.getSize());


            despesa.getComprovantes().add(comprovante);
            comprovanteRepository.save(comprovante);
            despesaRepository.save(despesa);

        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //Listar comprovante por id
    public ResponseEntity<byte[]> visualizarComprovante(Long idComprovante){
       Comprovante comprovante  = comprovanteRepository.findById(idComprovante).orElseThrow(() -> new RuntimeException("Erro: Nenhum comprovante localizado com este id"));
        byte[] data = comprovante.getFile();
        if (data == null || data.length == 0){
            return ResponseEntity.noContent().build();
        }

        String nomeArquivo = comprovante.getFileName() != null ? comprovante.getFileName() : "comprovante";


        String fileType = comprovante.getFileType();

        MediaType mediaType;

        try {
            mediaType = MediaType.parseMediaType(fileType);
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + nomeArquivo + "\"")
                .contentLength(data.length)
                .body(data);
    }


    @Transactional
    public void deletarComprovante(Long id){
        if (!comprovanteRepository.existsById(id)){
            throw new RuntimeException("Comprovante não existe");
        }
        comprovanteRepository.deleteById(id);
    }

}
