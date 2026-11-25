package task_manager.service;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import task_manager.dto.LoginRequest;
import task_manager.dto.LoginResponse;
import task_manager.dto.RegisterRequest;
import task_manager.model.AppUser;
import task_manager.repository.UserRepository;
import task_manager.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        AppUser user = new AppUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                "USER"
        );

        userRepository.save(user);

        return "User registered successfully";
    }

    public LoginResponse login(LoginRequest request) {

        AppUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(
                request.getPassword(), user.getPassword())) {

            throw new BadCredentialsException("Invalid username or password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new LoginResponse(token);
    }
}