package dispesas.com.security.model;

import dispesas.com.security.Enums.Role;
import dispesas.com.security.Enums.StatusCandidato;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Erro: O nome completo é obrigatório")
    @Column(name = "nome_completo", nullable = false)
    private String nomeCompleto;

    @NotBlank(message = "Erro: O e-mail é obrigatório")
    @Email(message = "Erro: O formato do e-mail é inválido")
    @Column(name = "email", unique = true, nullable = false)
    private String emailCandidato;

    @NotBlank(message = "Erro: A senha é obrigatória")
    @Column(name = "senha_usuario", nullable = false)
    private String senha;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;


    @Column(name = "status_usuario")
    @Enumerated(EnumType.STRING)
    private StatusCandidato statusUsuario;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;


    @PrePersist
    public void perPersist(){
        this.statusUsuario = StatusCandidato.ATIVO;
        dataCadastro = LocalDateTime.now();
    }

}
