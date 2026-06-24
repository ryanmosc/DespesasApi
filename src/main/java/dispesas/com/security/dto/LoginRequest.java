package dispesas.com.security.dto;

public record LoginRequest(
        String email,
        String senha
) {
}
