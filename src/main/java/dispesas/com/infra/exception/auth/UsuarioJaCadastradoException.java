package dispesas.com.infra.exception.auth;

public class UsuarioJaCadastradoException extends RuntimeException {
    public UsuarioJaCadastradoException(String message) {
        super(message);
    }
}
