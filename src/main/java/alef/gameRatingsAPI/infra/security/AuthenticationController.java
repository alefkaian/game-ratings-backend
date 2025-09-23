package alef.gameRatingsAPI.infra.security;

import alef.gameRatingsAPI.domain.user.LoginRequestDTO;
import alef.gameRatingsAPI.domain.user.LoginResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@Tag(name = "login")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping
    @Operation(summary = "Login with credentials", description = "Returns a JWT token")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO credentials) {
        LoginResponseDTO response = authenticationService.login(credentials);
        return ResponseEntity.ok(response);
    }
}
