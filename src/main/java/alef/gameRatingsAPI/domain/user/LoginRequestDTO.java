package alef.gameRatingsAPI.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequestDTO(
        @NotBlank(message = "Username can't be blank.")
        @Size(min = 4, message = "Login must have at least 4 characters.")
        String username,
        @NotBlank(message = "Password can't be blank.")
        @Size(min = 4, message = "Password must have at least 4 characters.")
        String password) {

}
