package dispesas.com.security.config;

import dispesas.com.infra.exception.auth.UsuarioNaoAutenticadoException;
import dispesas.com.security.model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static Long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new UsuarioNaoAutenticadoException(
                    "Nenhum usuário autenticado na requisição atual."
            );
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getId();
        }

        if (principal instanceof UserDetails) {
            throw new UsuarioNaoAutenticadoException(
                    "Usuário autenticado sem ID disponível. Certifique-se de utilizar CustomUserDetails."
            );
        }

        throw new UsuarioNaoAutenticadoException(
                "Tipo de principal não suportado: " + principal.getClass().getName()
        );
    }

    public static String getCurrentUserEmail() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        throw new UsuarioNaoAutenticadoException(
                "Nenhum usuário autenticado."
        );
    }
}