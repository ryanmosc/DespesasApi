package dispesas.com.infra.exception.auth;

public class SenhaFracaException extends RuntimeException {
    public SenhaFracaException(String message) {
        super(message);
    }
}
