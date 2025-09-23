package alef.gameRatingsAPI.infra.config;

import alef.gameRatingsAPI.domain.user.User;
import alef.gameRatingsAPI.domain.user.UserRepository;
import alef.gameRatingsAPI.domain.user.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${api.admin.default-username}")
    private String adminUsername;
    @Value("${api.admin.default-password}")
    private String adminPassword;

    public DataSeeder(UserRepository userRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (!userRepository.existsByUsername(adminUsername)) {
            System.out.println("User 'admin' doesn't exist.");
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setRole(UserRole.ADMIN);
            userRepository.save(adminUser);
            System.out.println("User 'admin' created successfully.");
        }
    }
}
