package com.quedate.config;

import com.quedate.entity.Landlord;
import com.quedate.entity.Role;
import com.quedate.entity.Room;
import com.quedate.entity.RoomStatus;
import com.quedate.entity.University;
import com.quedate.entity.User;
import com.quedate.entity.enums.RoleName;
import com.quedate.repository.LandlordRepository;
import com.quedate.repository.RoleRepository;
import com.quedate.repository.RoomRepository;
import com.quedate.repository.UniversityRepository;
import com.quedate.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final LandlordRepository landlordRepository;
    private final UniversityRepository universityRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(
            RoleRepository roleRepository,
            UserRepository userRepository,
            LandlordRepository landlordRepository,
            UniversityRepository universityRepository,
            RoomRepository roomRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.landlordRepository = landlordRepository;
        this.universityRepository = universityRepository;
        this.roomRepository = roomRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedRoles();
        seedAdmin();
        seedLandlord();
        seedUniversity();
        seedRooms();
    }

    private void seedRoles() {
        if (roleRepository.count() > 0) {
            return;
        }
        for (RoleName name : RoleName.values()) {
            Role role = new Role();
            role.setName(name);
            roleRepository.save(role);
        }
    }

    private void seedAdmin() {
        if (userRepository.existsByEmail("admin@quedate.com")) {
            return;
        }
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Quédate");
        admin.setEmail("admin@quedate.com");
        admin.setPassword(passwordEncoder.encode("Admin123!"));
        admin.setRoles(Set.of(roleRepository.findByName(RoleName.ADMIN).orElseThrow()));
        userRepository.save(admin);
    }

    private void seedLandlord() {
        if (landlordRepository.count() > 0 || userRepository.existsByEmail("landlord@quedate.com")) {
            return;
        }
        User landlordUser = new User();
        landlordUser.setFirstName("Carlos");
        landlordUser.setLastName("Ejemplo");
        landlordUser.setEmail("landlord@quedate.com");
        landlordUser.setPassword(passwordEncoder.encode("Demo123!"));
        landlordUser.setRoles(Set.of(
                roleRepository.findByName(RoleName.LANDLORD).orElseThrow()));
        userRepository.save(landlordUser);

        Landlord landlord = new Landlord();
        landlord.setUser(landlordUser);
        landlord.setDni("40123456");
        landlord.setBio("Arrendador demo de Quédate.");
        landlord.setVerified(true);
        landlordRepository.save(landlord);
    }

    private void seedUniversity() {
        boolean exists = universityRepository.findByNameContainingIgnoreCase("UTEC")
                .stream()
                .anyMatch(u -> "UTEC".equalsIgnoreCase(u.getName()));
        if (exists) {
            return;
        }
        University utec = new University();
        utec.setName("UTEC");
        utec.setCampusName("UTEC Miraflores");
        utec.setAddress("Jr. Medrano Silva 165, Barranco, Lima 15063");
        utec.setLatitude(-12.1469);
        utec.setLongitude(-77.0225);
        utec.setWebsite("https://www.utec.edu.pe");
        universityRepository.save(utec);
    }

    private void seedRooms() {
        if (roomRepository.count() > 0 || landlordRepository.count() == 0) {
            return;
        }
        Landlord owner = landlordRepository.findAll().get(0);
        University utec = universityRepository.findByNameContainingIgnoreCase("UTEC")
                .stream()
                .filter(u -> "UTEC".equalsIgnoreCase(u.getName()))
                .findFirst()
                .orElse(null);

        Room room1 = new Room();
        room1.setTitle("Habitación a 10 min de UTEC");
        room1.setDescription("Habitación amoblada con baño compartido y cocina equipada.");
        room1.setPrice(new BigDecimal("1200.00"));
        room1.setCapacity(2);
        room1.setSizeM2(18.0);
        room1.setStatus(RoomStatus.AVAILABLE);
        room1.setVerified(true);
        room1.setOwner(owner);
        room1.setUniversity(utec);
        roomRepository.save(room1);

        Room room2 = new Room();
        room2.setTitle("Departamento en Barranco");
        room2.setDescription("Cuarto privado en departamento con vista al malecón.");
        room2.setPrice(new BigDecimal("1500.00"));
        room2.setCapacity(1);
        room2.setSizeM2(22.0);
        room2.setStatus(RoomStatus.AVAILABLE);
        room2.setVerified(true);
        room2.setOwner(owner);
        room2.setUniversity(utec);
        roomRepository.save(room2);
    }
}