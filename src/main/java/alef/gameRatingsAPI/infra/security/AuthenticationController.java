package alef.gameRatingsAPI.infra.security;

import alef.gameRatingsAPI.domain.user.LoginRequestDTO;
import alef.gameRatingsAPI.domain.user.LoginResponseDTO;
import alef.gameRatingsAPI.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO credentials) {
        LoginResponseDTO response = authenticationService.login(credentials);
        return ResponseEntity.ok(response);
    }
}
