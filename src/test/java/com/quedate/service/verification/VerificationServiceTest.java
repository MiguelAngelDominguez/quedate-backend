package com.quedate.service.verification;

import com.quedate.dto.verification.VerificationDecisionDTO;
import com.quedate.dto.verification.VerificationRequestDTO;
import com.quedate.entity.Landlord;
import com.quedate.entity.User;
import com.quedate.entity.Verification;
import com.quedate.entity.enums.VerificationStatus;
import com.quedate.entity.enums.VerificationType;
import com.quedate.exception.InvalidOperationException;
import com.quedate.repository.LandlordRepository;
import com.quedate.repository.UserRepository;
import com.quedate.repository.VerificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationServiceTest {

    @Mock
    private VerificationRepository verificationRepository;
    @Mock
    private LandlordRepository landlordRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VerificationService verificationService;

    private Landlord landlord(Long id) {
        Landlord landlord = new Landlord();
        landlord.setId(id);
        return landlord;
    }

    private Verification pendingVerification(Long id, Landlord landlord) {
        return Verification.builder()
                .type(VerificationType.DNI)
                .documentReference("DNI-71234567")
                .status(VerificationStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .landlord(landlord)
                .build();
    }

    @Test
    void requestLandlordIdentity_persistsPending() {
        VerificationRequestDTO dto = new VerificationRequestDTO();
        dto.setType(VerificationType.DNI);
        dto.setDocumentReference("DNI-71234567");

        when(verificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = verificationService.requestLandlordIdentity(landlord(1L), dto);

        assertEquals(VerificationStatus.PENDING, result.getStatus());
        assertEquals(VerificationType.DNI, result.getType());
    }

    @Test
    void decide_approvedMarksLandlordVerified() {
        Landlord landlord = landlord(1L);
        Verification verification = pendingVerification(5L, landlord);
        User admin = new User();
        admin.setId(9L);

        VerificationDecisionDTO dto = new VerificationDecisionDTO();
        dto.setStatus(VerificationStatus.APPROVED);

        when(verificationRepository.findById(5L)).thenReturn(Optional.of(verification));
        when(userRepository.findById(9L)).thenReturn(Optional.of(admin));
        when(verificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var result = verificationService.decide(9L, 5L, dto);

        assertEquals(VerificationStatus.APPROVED, result.getStatus());
        assertEquals(9L, result.getReviewedById());
        assertEquals(true, landlord.isVerified());
        verify(landlordRepository).save(landlord);
    }

    @Test
    void decide_rejectsNonDecisionStatus() {
        Verification verification = pendingVerification(5L, landlord(1L));
        VerificationDecisionDTO dto = new VerificationDecisionDTO();
        dto.setStatus(VerificationStatus.PENDING);

        when(verificationRepository.findById(5L)).thenReturn(Optional.of(verification));

        assertThrows(InvalidOperationException.class,
                () -> verificationService.decide(9L, 5L, dto));
        verify(landlordRepository, never()).save(any());
    }

    @Test
    void decide_approvedLandlordBecomesVerified() {
        Verification verification = pendingVerification(5L, landlord(1L));
        User admin = new User();
        admin.setId(9L);
        VerificationDecisionDTO dto = new VerificationDecisionDTO();
        dto.setStatus(VerificationStatus.APPROVED);

        when(verificationRepository.findById(5L)).thenReturn(Optional.of(verification));
        when(userRepository.findById(9L)).thenReturn(Optional.of(admin));
        when(verificationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        verificationService.decide(9L, 5L, dto);

        assertEquals(true, verification.getLandlord().isVerified());
        assertEquals(VerificationStatus.APPROVED, verification.getStatus());
    }
}