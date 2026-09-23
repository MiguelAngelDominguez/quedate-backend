package com.quedate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    private String description;

    @NotNull
    @Min(1)
    private BigDecimal price;

    @Min(1)
    private Integer capacity;

    private Double sizeM2;

    @ElementCollection
    @CollectionTable(
            name = "room_images",
            joinColumns = @JoinColumn(name = "room_id")
    )
    @Column(name = "image")
    private List<String> images = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status = RoomStatus.AVAILABLE;

    private boolean isVerified = false;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private Landlord owner;

    @ManyToOne
    @JoinColumn(name = "university_id")
    private University university;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
