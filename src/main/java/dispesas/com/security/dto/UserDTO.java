package dispesas.com.security.dto;

public record UserDTO(
        String nomeCompleto,
        String emailUsuario,
        String senha
) {
}
