package dispesas.com.security.service;

import dispesas.com.Repository.UserRepository;
import dispesas.com.security.Enums.Role;
import dispesas.com.security.dto.UserDTO;
import dispesas.com.security.dto.UserResponseDTO;
import dispesas.com.security.model.User;
import dispesas.com.utils.EmailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;

    private UserResponseDTO toResponseUser(User user){
        return new UserResponseDTO(
                user.getId(),
                user.getNomeCompleto(),
                user.getEmailCandidato()
        );
    }


    public UserResponseDTO criarUsuario(UserDTO dto){

        if (userRepository.existsByEmailCandidato(dto.emailUsuario())){
            throw new RuntimeException("Erro: Usuario ja cadastrado");
        }
        if (dto.senha().length() < 8){
            throw new RuntimeException("Erro: Senha precisa ser maior que 8");
        }

        User user = new User();
        user.setNomeCompleto(dto.nomeCompleto());
        user.setEmailCandidato(dto.emailUsuario());
        user.setSenha(passwordEncoder.encode(dto.senha()));
        user.setRole(Role.ROLE_ADMIN);
        userRepository.save(user);
        emailSender.enviarEmail(user.getEmailCandidato(), "Obrigado por usar o DisPesas. Seu cadastro foi concluido com sucesso.", "Obrigado " + user.getNomeCompleto());

        return toResponseUser(user);
    }

    //Somente ADMIN
    public void deletarUsuario(Long id){
        userRepository.deleteById(id);
    }
}
