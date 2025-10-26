package alef.gameRatingsAPI.infra.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final ApplicationUserDetailsService userDetailsService;

    public SecurityFilter(TokenService tokenService,
                          ApplicationUserDetailsService userDetailsService) {
        this.tokenService = tokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = recoverToken(request);
        System.out.println(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")) +  " " +
                "Received " +
                "token: " + token);
        if (token != null) {
            String username = tokenService.validateToken(token);

            UserDetails user =
                    userDetailsService.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken usernamePassword =
                    new UsernamePasswordAuthenticationToken(user, null,
                            user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(usernamePassword);

        }
        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null) return null;
        return authorizationHeader.replace("Bearer ", "");
    }
}
