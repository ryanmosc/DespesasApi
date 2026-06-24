package dispesas.com.security.controller;

import dispesas.com.security.dto.UserDTO;
import dispesas.com.security.dto.UserResponseDTO;
import dispesas.com.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponseDTO criarUsuario(@RequestBody UserDTO dto){
        UserResponseDTO reponse = userService.criarUsuario(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reponse).getBody();
    }
}
