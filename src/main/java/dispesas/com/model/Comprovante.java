package dispesas.com.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comprovantes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Comprovante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "despesa_id", nullable = false)
    private Despesa despesa;

    @Column(name = "file_name", nullable = false)
    private String fileName;


    @Column(name = "file_type", nullable = false)
    private String fileType;

    @Column(name = "file", columnDefinition = "BYTEA")
    private byte[] file;

    @Column(name = "file_size_bytes")
    private Long fileSizeBytes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}