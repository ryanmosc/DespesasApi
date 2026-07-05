package dispesas.com.utils.investimentoUtil;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class RestApiCdi {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String URI = "https://brasilapi.com.br/api/taxas/v1";

    public List<RestApiDTO> taxas() {
        ResponseEntity<List<RestApiDTO>> response = restTemplate.exchange(
                URI,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RestApiDTO>>() {}
        );
        return response.getBody();
    }

    public BigDecimal getCdi() {
        List<RestApiDTO> lista = taxas();

        if (lista != null) {
            return lista.stream()
                    .filter(taxa -> "CDI".equalsIgnoreCase(taxa.nome()))
                    .map(RestApiDTO::valor)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
        }

        return BigDecimal.ZERO;
    }

    public BigDecimal getSelic() {
        List<RestApiDTO> lista = taxas();

        if (lista != null) {
            return lista.stream()
                    .filter(taxa -> "Selic".equalsIgnoreCase(taxa.nome()))
                    .map(RestApiDTO::valor)
                    .findFirst()
                    .orElse(BigDecimal.ZERO);
        }

        return BigDecimal.ZERO;
    }
}