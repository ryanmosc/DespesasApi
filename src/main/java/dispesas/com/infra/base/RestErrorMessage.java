package dispesas.com.infra.base;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class RestErrorMessage {

    private LocalDateTime timestamp;
    private Integer status;
    private String error;
    private String message;
    private String path;
    private String userName;
    private Long userId;
    private Map<String, String> fieldErrors;

    public RestErrorMessage(LocalDateTime timestamp,
                            Integer status,
                            String error,
                            String message,
                            String path,
                            String userName,
                            Long userId) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.userName = userName;
        this.userId = userId;
    }
}