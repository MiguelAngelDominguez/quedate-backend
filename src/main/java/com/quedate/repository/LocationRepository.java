package com.quedate.repository;

import com.quedate.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    List<Location> findByDistrictIgnoreCase(String district);
}