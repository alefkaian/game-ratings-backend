package alef.gameRatingsAPI.infra.security;

import alef.gameRatingsAPI.domain.user.LoginRequestDTO;
import alef.gameRatingsAPI.domain.user.LoginResponseDTO;
import alef.gameRatingsAPI.domain.user.User;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity login(@RequestBody @Valid LoginRequestDTO credentials) {
        //LoginResponseDTO response = authenticationService.login(credentials);
        var usernamePassword =
                new UsernamePasswordAuthenticationToken(credentials.username(),
                        credentials.password());
        var authentication =
                authenticationManager.authenticate(usernamePassword);
        var user = (User) authentication.getPrincipal();
        var token = tokenService.generateToken(user);
        return ResponseEntity.ok(new LoginResponseDTO(token));
        //return ResponseEntity.ok(response);
    }
}
