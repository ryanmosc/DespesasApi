package dispesas.com.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import static org.hibernate.annotations.UuidGenerator.Style.RANDOM;

@Service
@RequiredArgsConstructor
public class GenereteCode {

    private final EmailSender emailSender;
    public static final String CHARS = "0123456789";
    public static final SecureRandom RANDOM = new SecureRandom();
    public static final int CODE_LENGHT = 6;

    public  String gerarCodigoValidacao(String email) {
        StringBuilder stringBuilder = new StringBuilder(CODE_LENGHT);


            for (int i = 0; i < CODE_LENGHT; i++) {
                int index = RANDOM.nextInt(CHARS.length());
                stringBuilder.append(CHARS.charAt(index));

            }
            System.out.println("Atualizar senha de usuario");
            System.out.println(stringBuilder.toString());
            emailSender.enviarEmail(email, "Segue em anexo o código para validação de troca de senha." , stringBuilder.toString() );
            return stringBuilder.toString();


    }
}
