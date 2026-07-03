package dispesas.com.security.service;

import dispesas.com.Repository.CodigoVerificacaoRepository;
import dispesas.com.Repository.UserRepository;
import dispesas.com.infra.exception.auth.CodigoExpiradoException;
import dispesas.com.infra.exception.auth.CodigoInvalidoException;
import dispesas.com.infra.exception.auth.SenhaFracaException;
import dispesas.com.infra.exception.auth.UsuarioJaCadastradoException;
import dispesas.com.infra.exception.auth.UsuarioNaoAutenticadoException;
import dispesas.com.security.Enums.Role;
import dispesas.com.security.config.SecurityUtil;
import dispesas.com.security.dto.ChangePasswordDTO;
import dispesas.com.security.dto.UserDTO;
import dispesas.com.security.dto.UserResponseDTO;
import dispesas.com.security.model.CodigoVerificacao;
import dispesas.com.security.model.User;
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
    private final CodigoVerificacaoRepository codigoVerificacaoRepository;

    private UserResponseDTO toResponseUser(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getNomeCompleto(),
                user.getEmailCandidato()
        );
    }

    public UserResponseDTO criarUsuario(UserDTO dto) {

        if (userRepository.existsByEmailCandidato(dto.emailUsuario())) {
            throw new UsuarioJaCadastradoException("Usuário já cadastrado.");
        }

        if (dto.senha().length() < 8) {
            throw new SenhaFracaException("A senha deve possuir pelo menos 8 caracteres.");
        }

        User user = new User();
        user.setNomeCompleto(dto.nomeCompleto());
        user.setEmailCandidato(dto.emailUsuario());
        user.setSenha(passwordEncoder.encode(dto.senha()));
        user.setRole(Role.ROLE_ADMIN);

        userRepository.save(user);

        emailSender.enviarEmail(
                user.getEmailCandidato(),
                "Obrigado por usar o DisPesas. Seu cadastro foi concluído com sucesso.",
                "Obrigado " + user.getNomeCompleto()
        );

        return toResponseUser(user);
    }

    // PASSO 1 — usuário pede o código
    @Transactional
    public void solicitarCodigoTrocaSenha() {

        Long userId = SecurityUtil.getCurrentUserId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsuarioNaoAutenticadoException("Usuário inválido."));

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
                .orElseThrow(() -> new UsuarioNaoAutenticadoException("Usuário inválido."));

        CodigoVerificacao verificacao = codigoVerificacaoRepository
                .findTopByUserIdAndUsadoFalseOrderByIdDesc(userId)
                .orElseThrow(() -> new CodigoInvalidoException("Nenhum código solicitado."));

        if (verificacao.isUsado()) {
            throw new CodigoInvalidoException("Código já utilizado.");
        }

        if (verificacao.getExpiraEm().isBefore(LocalDateTime.now())) {
            throw new CodigoExpiradoException("Código expirado.");
        }

        if (!verificacao.getCodigo().equals(dto.codigo())) {
            throw new CodigoInvalidoException("Código inválido.");
        }

        if (dto.newPassword().length() < 8) {
            throw new SenhaFracaException("A senha deve possuir pelo menos 8 caracteres.");
        }

        user.setSenha(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);

        verificacao.setUsado(true);
        codigoVerificacaoRepository.save(verificacao);
    }

    // Somente ADMIN
    public void deletarUsuario(Long id) {
        userRepository.deleteById(id);
    }
}