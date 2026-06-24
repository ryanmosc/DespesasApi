package dispesas.com.security.dto;

public record LoginResponse(
        String token,
        String nomeCompleto,
        String email,
        String role
) {
}
