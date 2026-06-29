package dispesas.com.model;

import dispesas.com.model.enumModel.StatusInvestimento;
import dispesas.com.model.enumModel.TipoInvestimento;
import dispesas.com.security.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "investimentos")
public class Investimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoInvestimento tipo;

    @Column(nullable = false)
    private BigDecimal valorInicial;

    @Column(nullable = false)
    private BigDecimal valorAtual;

    @Column(nullable = false)
    private LocalDate dataInicio;

    private LocalDate dataVencimento;

    @Column(nullable = false)
    private String instituicao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusInvestimento status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false, updatable = false)
    private LocalDateTime criadoEm;

    @Column(nullable = false)
    private LocalDateTime atualizadoEm;

    @PrePersist
    public void prePersist() {
        LocalDateTime agora = LocalDateTime.now();
        criadoEm = agora;
        atualizadoEm = agora;
    }

    @PreUpdate
    public void preUpdate() {
        atualizadoEm = LocalDateTime.now();
    }
}
