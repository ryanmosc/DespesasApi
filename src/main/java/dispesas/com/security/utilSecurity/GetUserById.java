package dispesas.com.security.utilSecurity;

import dispesas.com.Repository.UserRepository;
import dispesas.com.security.config.SecurityUtil;
import dispesas.com.security.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserById {
    private final UserRepository userRepository;

    public User getUserById(){
        Long userId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return user;

    }

    //        Long  userId = getUserById.getUserById().getId();
}
