package dispesas.com.security.service;

import dispesas.com.Repository.CodigoVerificacaoRepository;
import dispesas.com.Repository.UserRepository;
import dispesas.com.security.Enums.Role;
import dispesas.com.security.config.SecurityUtil;
import dispesas.com.security.dto.ChangePasswordDTO;
import dispesas.com.security.dto.SolicitarCodigoDTO;
import dispesas.com.security.dto.UserDTO;
import dispesas.com.security.dto.UserResponseDTO;
import dispesas.com.security.model.CodigoVerificacao;
import dispesas.com.security.model.User;
import dispesas.com.security.utilSecurity.GetUserById;
import dispesas.com.utils.EmailSender;
import dispesas.com.utils.GenereteCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailSender emailSender;
    private final GenereteCode genereteCode;
    private final GetUserById getUserById;
    private final CodigoVerificacaoRepository codigoVerificacaoRepository;

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


    // PASSO 1 — usuário pede o código
    @Transactional
    public void solicitarCodigoTrocaSenha() {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Erro: Usuário inválido"));

        String codigo = genereteCode.gerarCodigoValidacao(user.getEmailCandidato());

        CodigoVerificacao verificacao = new CodigoVerificacao();
        verificacao.setUserId(userId);
        verificacao.setCodigo(codigo);
        verificacao.setExpiraEm(LocalDateTime.now().plusMinutes(10));
        verificacao.setUsado(false);

        codigoVerificacaoRepository.save(verificacao);
    }

    // PASSO 2 — usuário envia o código + nova senha
    @Transactional
    public void changePassword(ChangePasswordDTO dto) {
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Erro: Usuário inválido"));

        CodigoVerificacao verificacao = codigoVerificacaoRepository
                .findTopByUserIdAndUsadoFalseOrderByIdDesc(userId)
                .orElseThrow(() -> new RuntimeException("Erro: Nenhum código solicitado"));

        if (verificacao.isUsado()) {
            throw new RuntimeException("Erro: Código já utilizado");
        }

        if (verificacao.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Erro: Código expirado");
        }

        if (!verificacao.getCodigo().equals(dto.codigo())) {
            throw new RuntimeException("Erro: Código inválido");
        }

        if (dto.newPassword().length() < 8) {
            throw new RuntimeException("Erro: Senha precisa ter pelo menos 8 caracteres");
        }

        user.setSenha(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);

        verificacao.setUsado(true); // invalida o código após o uso
        codigoVerificacaoRepository.save(verificacao);
    }

    //Somente ADMIN
    public void deletarUsuario(Long id){
        userRepository.deleteById(id);
    }
}
