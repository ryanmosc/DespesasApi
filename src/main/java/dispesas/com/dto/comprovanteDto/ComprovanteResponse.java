package dispesas.com.dto.comprovanteDto;

import java.time.LocalDateTime;

public record ComprovanteResponse(

        Long id,
        String fileName,
        String fileType,
        Long fileSizeBytes,
        LocalDateTime createdAt

) {}