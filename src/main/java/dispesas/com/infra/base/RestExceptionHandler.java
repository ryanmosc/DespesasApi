package dispesas.com.infra.base;

import dispesas.com.infra.exception.auth.*;
import dispesas.com.infra.exception.despesa.ComprovanteNaoEncontradoException;
import dispesas.com.infra.exception.despesa.DespesaNaoEncontradaException;
import dispesas.com.infra.exception.geral.AcessoNegadoException;
import dispesas.com.infra.exception.geral.RecursoNaoEncontradoException;
import dispesas.com.infra.exception.investimento.AporteNaoEncontradoException;
import dispesas.com.infra.exception.investimento.InvestimentoNaoEncontradoException;
import dispesas.com.security.utilSecurity.GetUserById;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private final GetUserById getUserById;

    private RestErrorMessage buildError(
            HttpStatus status,
            String msg,
            WebRequest request
    ){

        Long userId = null;
        String  userName = null;

        try {
            userId = getUserById.getUserById().getId();
            userName = getUserById.getUserById().getNomeCompleto();
        }catch (Exception ignored){
            //Usuario não autenticado.
        }


        return new RestErrorMessage(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                msg,
                request.getDescription(false).replace("uri=", ""),
                userName,
                userId
        );
    }

    //AUTH

    // ========================= AUTH =========================

    @ExceptionHandler(CodigoExpiradoException.class)
    public ResponseEntity<RestErrorMessage> handleCodigoExpirado(
            CodigoExpiradoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(CodigoInvalidoException.class)
    public ResponseEntity<RestErrorMessage> handleCodigoInvalido(
            CodigoInvalidoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(SenhaFracaException.class)
    public ResponseEntity<RestErrorMessage> handleSenhaFraca(
            SenhaFracaException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request));
    }

    @ExceptionHandler(UsuarioJaCadastradoException.class)
    public ResponseEntity<RestErrorMessage> handleUsuarioJaCadastrado(
            UsuarioJaCadastradoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError(HttpStatus.CONFLICT, ex.getMessage(), request));
    }

    @ExceptionHandler(UsuarioNaoAutenticadoException.class)
    public ResponseEntity<RestErrorMessage> handleUsuarioNaoAutenticado(
            UsuarioNaoAutenticadoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(buildError(HttpStatus.UNAUTHORIZED, ex.getMessage(), request));
    }

// ========================= DESPESA =========================

    @ExceptionHandler(DespesaNaoEncontradaException.class)
    public ResponseEntity<RestErrorMessage> handleDespesaNaoEncontrada(
            DespesaNaoEncontradaException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(ComprovanteNaoEncontradoException.class)
    public ResponseEntity<RestErrorMessage> handleComprovanteNaoEncontrado(
            ComprovanteNaoEncontradoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

// ========================= INVESTIMENTO =========================

    @ExceptionHandler(InvestimentoNaoEncontradoException.class)
    public ResponseEntity<RestErrorMessage> handleInvestimentoNaoEncontrado(
            InvestimentoNaoEncontradoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(AporteNaoEncontradoException.class)
    public ResponseEntity<RestErrorMessage> handleAporteNaoEncontrado(
            AporteNaoEncontradoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

// ========================= GERAL =========================

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<RestErrorMessage> handleRecursoNaoEncontrado(
            RecursoNaoEncontradoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public ResponseEntity<RestErrorMessage> handleAcessoNegado(
            AcessoNegadoException ex,
            WebRequest request
    ){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildError(HttpStatus.FORBIDDEN, ex.getMessage(), request));
    }

// ========================= EXCEÇÃO GENÉRICA =========================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestErrorMessage> handleGeneric(
            Exception ex,
            WebRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Erro interno no servidor",
                        request
                ));
    }



}
