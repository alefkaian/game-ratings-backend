package alef.gameRatingsAPI.infra.security;

import alef.gameRatingsAPI.domain.user.LoginRequestDTO;
import alef.gameRatingsAPI.domain.user.LoginResponseDTO;
import alef.gameRatingsAPI.domain.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationService(AuthenticationManager authenticationManager,
                                 TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public LoginResponseDTO login(LoginRequestDTO credentials) {
        UsernamePasswordAuthenticationToken usernamePassword =
                new UsernamePasswordAuthenticationToken(credentials.username(),
                        credentials.password());
        Authentication authentication =
                authenticationManager.authenticate(usernamePassword);
        User user = (User) authentication.getPrincipal();
        String token = tokenService.generateToken(user);
        return new LoginResponseDTO(token);
    }
}
