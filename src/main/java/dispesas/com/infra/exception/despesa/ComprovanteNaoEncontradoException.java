package dispesas.com.infra.exception.despesa;

public class ComprovanteNaoEncontradoException extends RuntimeException {
    public ComprovanteNaoEncontradoException(String message) {
        super(message);
    }
}
