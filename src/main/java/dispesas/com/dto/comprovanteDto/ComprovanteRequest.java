package dispesas.com.dto.comprovanteDto;

import dispesas.com.model.Despesa;

public record ComprovanteRequest(

        Despesa despesa,
        String fileName,
        String fileType,
        byte[] file


) {
}
