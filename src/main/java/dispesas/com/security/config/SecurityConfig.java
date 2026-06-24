package dispesas.com.security.config;

import dispesas.com.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        // =============================================
                        // AUTH — públicas
                        // =============================================
                        .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/registro").permitAll()

                        // =============================================
                        // AUTH — admin
                        // =============================================
                        .requestMatchers(HttpMethod.DELETE, "/api/auth/usuario/**").hasRole("ADMIN")

                        // =============================================
                        // DESPESAS — admin
                        // =============================================
                        .requestMatchers(HttpMethod.DELETE, "/api/despesas/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,   "/api/despesas/{id}/duplicar").hasRole("ADMIN")

                        // =============================================
                        // DESPESAS — autenticado
                        // =============================================
                        .requestMatchers(HttpMethod.POST,  "/api/despesas").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/api/despesas").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/api/despesas/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/api/despesas/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/api/despesas/filtrar").authenticated()
                        .requestMatchers(HttpMethod.GET,   "/api/despesas/parcelas-em-aberto").authenticated()

                        // =============================================
                        // COMPROVANTE — admin
                        // =============================================
                        .requestMatchers(HttpMethod.DELETE, "/api/comprovante/**").hasRole("ADMIN")

                        // =============================================
                        // COMPROVANTE — autenticado
                        // =============================================
                        .requestMatchers(HttpMethod.POST, "/api/comprovante/**").authenticated()
                        .requestMatchers(HttpMethod.GET,  "/api/comprovante/**").authenticated()

                        // =============================================
                        // DASHBOARD / RESUMO — autenticado
                        // =============================================
                        .requestMatchers(HttpMethod.GET, "/api/despesas/resumo/mensal").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/despesas/resumo/por-categoria").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/despesas/resumo/comparativo").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/despesas/saldo").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/despesas/relatorios/gastos-por-mes").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/despesas/top-5-gastos").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/despesas/top-5-categorias").authenticated()

                        // =============================================
                        // Qualquer outra rota não mapeada — bloqueia
                        // =============================================
                        .anyRequest().denyAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}