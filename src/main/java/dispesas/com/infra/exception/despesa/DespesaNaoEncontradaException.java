package dispesas.com.infra.exception.despesa;

public class DespesaNaoEncontradaException extends RuntimeException {
    public DespesaNaoEncontradaException(String message) {
        super(message);
    }
}
