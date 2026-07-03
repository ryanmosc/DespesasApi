package dispesas.com.infra.exception.auth;

public class UsuarioNaoAutenticadoException extends RuntimeException {
    public UsuarioNaoAutenticadoException(String msg) {
       super(msg);
    }
}
