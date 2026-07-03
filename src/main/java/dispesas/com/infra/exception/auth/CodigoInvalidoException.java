package dispesas.com.infra.exception.auth;

public class CodigoInvalidoException extends RuntimeException {
    public CodigoInvalidoException(String message) {
        super(message);
    }
}
