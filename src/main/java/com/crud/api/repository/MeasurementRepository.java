package com.crud.api.repository;

import com.crud.api.entity.Measurement;
import java.util.List;

import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findAllByUserId(Long id, Sort sort);

    List<Measurement> findAllByUserId(Long id);

    @Query("SELECT m.value FROM  Measurement m "
        + "WHERE m.user.id = ?1 "
        + "and m.type = 'ENERGY_TDEE'"
        + "ORDER BY m.id DESC LIMIT 1")
    Optional<Float> findLastTdeeByUserId(Long userId);

    void deleteByUserId(Long id);

    boolean existsByUserId(Long id);

    @Query("SELECT m.value FROM  Measurement m "
            + "WHERE m.user.id = ?1 "
            + "and m.type = 'ENERGY_SURPLUS'"
            + "ORDER BY m.id DESC LIMIT 1")
    Optional<Float> findLastSurplusByUserId(Long userId);
}
