package dispesas.com.infra.exception.investimento;

public class InvestimentoNaoEncontradoException extends RuntimeException {
    public InvestimentoNaoEncontradoException(String message) {
        super(message);
    }
}
