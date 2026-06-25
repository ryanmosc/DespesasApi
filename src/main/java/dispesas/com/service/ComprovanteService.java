package dispesas.com.service;

import dispesas.com.Repository.ComprovanteRepository;
import dispesas.com.Repository.DespesaRepository;
import dispesas.com.model.Comprovante;
import dispesas.com.model.Despesa;
import dispesas.com.security.utilSecurity.GetUserById;
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
    private final GetUserById getUserById;

    @Transactional
    public void salvarComprovante(MultipartFile file, Long idDespesa){

        //Validações iniciais
        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Nenhum arquivo enviado");
        }
        if (file.getSize() > 5L * 1024 * 1024){
            throw new RuntimeException("Arquivo muito grande. Máximo 5MB");
        }
        Long userId = getUserById.getUserById().getId();
        Despesa despesa = despesaRepository.findById(idDespesa).orElseThrow(() -> new RuntimeException("Nenhum despesa encontrada"));
        if (!despesa.getUser().getId().equals(userId)){
            throw new RuntimeException("Erro: Usurio não é o correto");
        }

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
        Long userId = getUserById.getUserById().getId();
        if (!comprovante.getDespesa().getUser().getId().equals(userId)){
           throw new RuntimeException("Erro: Usurio não é o correto");
       }

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
        Comprovante comprovante  = comprovanteRepository.findById(id).orElseThrow(() -> new RuntimeException("Erro: Nenhum comprovante localizado com este id"));
        Long userId = getUserById.getUserById().getId();

        if (!comprovante.getDespesa().getUser().getId().equals(userId)){
            throw new RuntimeException("Erro: Usurio não é o correto");
        }

        if (!comprovanteRepository.existsById(id)){
            throw new RuntimeException("Comprovante não existe");
        }

        comprovanteRepository.deleteById(id);
    }

}
