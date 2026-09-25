package com.quedate.service.auth;

import com.quedate.dto.auth.AuthResponseDTO;
import com.quedate.dto.auth.LoginRequestDTO;
import com.quedate.dto.auth.RefreshTokenRequestDTO;
import com.quedate.dto.auth.RegisterRequestDTO;
import com.quedate.dto.user.UserResponseDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.Role;
import com.quedate.entity.Student;
import com.quedate.entity.User;
import com.quedate.entity.enums.RoleName;
import com.quedate.event.UserRegisteredEvent;
import com.quedate.exception.EmailAlreadyExistsException;
import com.quedate.exception.InvalidOperationException;
import com.quedate.exception.InvalidTokenException;
import com.quedate.exception.UnauthorizedException;
import com.quedate.repository.LandlordRepository;
import com.quedate.repository.RoleRepository;
import com.quedate.repository.StudentRepository;
import com.quedate.repository.UserRepository;
import com.quedate.security.JwtService;
import com.quedate.security.UserPrincipal;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private static final Pattern UPPERCASE_PATTERN = Pattern.compile("[A-Z]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("[0-9]");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final LandlordRepository landlordRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            StudentRepository studentRepository,
            LandlordRepository landlordRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            ApplicationEventPublisher eventPublisher
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.landlordRepository = landlordRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException(
                    "Email is already registered: " + dto.getEmail());
        }
        validatePassword(dto.getPassword());
        if (dto.getRole() != RoleName.USER && dto.getRole() != RoleName.LANDLORD) {
            throw new InvalidOperationException(
                    "Only USER or LANDLORD roles can be assigned on registration");
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRoles(Set.of(resolveRole(dto.getRole())));
        userRepository.save(user);

        createProfile(user, dto.getRole());

        eventPublisher.publishEvent(new UserRegisteredEvent(user));

        return buildAuthResponse(user);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.getEmail(), dto.getPassword()));
            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
            User user = userRepository.findByEmail(principal.getUsername())
                    .orElseThrow(() -> new UnauthorizedException(
                            "Invalid email or password"));
            return buildAuthResponse(user);
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Invalid email or password");
        }
    }

    public AuthResponseDTO refresh(RefreshTokenRequestDTO dto) {
        if (!jwtService.isValid(dto.getRefreshToken())
                || jwtService.isExpired(dto.getRefreshToken())) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }
        String email = jwtService.extractEmail(dto.getRefreshToken());
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidTokenException(
                        "Invalid or expired refresh token"));
        return buildAuthResponse(user);
    }

    public UserResponseDTO getCurrentUser(UserPrincipal principal) {
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new UnauthorizedException(
                        "Authenticated user no longer exists"));
        return buildUserResponse(user);
    }

    private void validatePassword(String password) {
        if (!UPPERCASE_PATTERN.matcher(password).find()
                || !DIGIT_PATTERN.matcher(password).find()) {
            throw new InvalidOperationException(
                    "Password must contain at least one uppercase letter and one number");
        }
    }

    private Role resolveRole(RoleName name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new InvalidOperationException(
                        "Role does not exist: " + name));
    }

    private void createProfile(User user, RoleName roleName) {
        if (roleName == RoleName.LANDLORD) {
            Landlord landlord = new Landlord();
            landlord.setUser(user);
            landlordRepository.save(landlord);
        } else {
            Student student = new Student();
            student.setUser(user);
            studentRepository.save(student);
        }
    }

    private AuthResponseDTO buildAuthResponse(User user) {
        UserPrincipal principal = new UserPrincipal(user);
        return AuthResponseDTO.builder()
                .accessToken(jwtService.generateToken(principal))
                .refreshToken(jwtService.generateRefreshToken(principal))
                .userInfo(buildUserResponse(user))
                .build();
    }

    private UserResponseDTO buildUserResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .collect(Collectors.toSet()))
                .build();
    }
}