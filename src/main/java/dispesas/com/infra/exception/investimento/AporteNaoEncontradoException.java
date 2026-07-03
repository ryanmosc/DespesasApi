package dispesas.com.infra.exception.investimento;

public class AporteNaoEncontradoException extends RuntimeException {
    public AporteNaoEncontradoException(String message) {
        super(message);
    }
}
