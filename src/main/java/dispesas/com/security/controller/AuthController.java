package dispesas.com.security.controller;

import dispesas.com.security.dto.*;
import dispesas.com.security.service.JwtService;
import dispesas.com.security.service.UserDetailsServiceImpl;
import dispesas.com.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtService jwtService;

    @PostMapping("/registro")
    public ResponseEntity<UserResponseDTO> registro(@RequestBody UserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.criarUsuario(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        // Valida email e senha — lança exceção se inválido
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.senha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.email());
        String token = jwtService.gerarToken(userDetails);

        return ResponseEntity.ok(new LoginResponse(
                token,
                userDetails.getUsername(),
                userDetails.getUsername(),
                userDetails.getAuthorities().iterator().next().getAuthority()
        ));
    }


    @PostMapping("/solicitar-codigo")
    public ResponseEntity<Void> solicitarCodigo(@RequestBody SolicitarCodigoDTO dto) {
        userService.solicitarCodigoTrocaSenha(dto);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/trocar-senha")
    public ResponseEntity<Void> trocarSenha(@RequestBody ChangePasswordDTO dto) {
        userService.changePassword(dto);
        return ResponseEntity.noContent().build();
    }


/*
    @DeleteMapping("/usuario/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        userService.deletarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    */
}