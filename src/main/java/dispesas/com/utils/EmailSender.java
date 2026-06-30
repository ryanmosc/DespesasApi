package dispesas.com.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
//Observação, nunca utilizar o nome da classe como "MailSender", se não da erro, ok Ryan do futuro?
public class EmailSender {

    private final JavaMailSender mailSender;
    private final String remetente = "contato@voluntariosdasaude.com.br";


    public void enviarEmail(String destinatario, String assunto, String corpo) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(remetente);
        mailMessage.setTo(destinatario);
        mailMessage.setSubject(assunto);
        mailMessage.setText(corpo);

        mailSender.send(mailMessage);
    }
}
