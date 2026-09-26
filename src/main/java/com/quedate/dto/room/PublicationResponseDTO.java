package com.quedate.dto.room;

import com.quedate.entity.PublicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PublicationResponseDTO {

    private Long id;
    private Long roomId;
    private String roomTitle;
    private PublicationStatus status;
    private LocalDateTime publishedAt;
    private LocalDateTime archivedAt;
}