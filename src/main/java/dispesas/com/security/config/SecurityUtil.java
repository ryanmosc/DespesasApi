package dispesas.com.security.config;


import dispesas.com.security.model.CustomUserDetails;
import dispesas.com.security.service.UserDetailsServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Nenhum usuário autenticado na requisição atual");
        }

        Object principal = authentication.getPrincipal();

        // Tipo correto: CustomUserDetails (não o Service!)
        if (principal instanceof CustomUserDetails userDetailsService) {
            return userDetailsService.getId();
        }

        // Fallback (se por algum motivo ainda usar o User padrão)
        if (principal instanceof UserDetails) {
            throw new RuntimeException("Usuário autenticado sem ID disponível. Certifique-se de usar CustomUserDetails.");
        }

        throw new RuntimeException("Tipo de principal não suportado: " + principal.getClass().getName());
    }

    public static String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        throw new RuntimeException("Nenhum usuário autenticado");
    }
}
