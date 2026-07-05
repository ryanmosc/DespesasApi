package dispesas.com.utils.investimentoUtil;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class RestApiCdi {

    RestTemplate restTemplate = new RestTemplate();
    private static String uri = "https://brasilapi.com.br/api/taxas/v1";

    public List<RestApiDTO> taxas() {
        ResponseEntity<List<RestApiDTO>> response = restTemplate.exchange(uri,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<RestApiDTO>>() {
                });
        return response.getBody();
    }
}
