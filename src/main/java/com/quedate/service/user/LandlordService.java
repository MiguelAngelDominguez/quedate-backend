package com.quedate.service.user;

import com.quedate.dto.landlord.LandlordResponseDTO;
import com.quedate.dto.landlord.LandlordUpdateDTO;
import com.quedate.entity.Landlord;
import com.quedate.exception.ResourceNotFoundException;
import com.quedate.repository.LandlordRepository;
import com.quedate.security.UserPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LandlordService {

    private final LandlordRepository landlordRepository;

    public LandlordService(LandlordRepository landlordRepository) {
        this.landlordRepository = landlordRepository;
    }

    @Transactional(readOnly = true)
    public LandlordResponseDTO getProfileByUserId(Long userId) {
        return buildResponse(findByUserId(userId));
    }

    @Transactional
    public LandlordResponseDTO updateProfile(UserPrincipal actor, LandlordUpdateDTO dto) {
        Landlord landlord = findByUserId(actor.getUserId());
        if (dto.getDni() != null) {
            landlord.setDni(dto.getDni());
        }
        if (dto.getBio() != null) {
            landlord.setBio(dto.getBio());
        }
        return buildResponse(landlord);
    }

    public LandlordResponseDTO getById(Long id) {
        return buildResponse(landlordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Landlord not found: " + id)));
    }

    private Landlord findByUserId(Long userId) {
        return landlordRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Landlord profile not found for user: " + userId));
    }

    private LandlordResponseDTO buildResponse(Landlord landlord) {
        return LandlordResponseDTO.builder()
                .id(landlord.getId())
                .userId(landlord.getUser().getId())
                .dni(landlord.getDni())
                .bio(landlord.getBio())
                .verified(landlord.isVerified())
                .build();
    }
}