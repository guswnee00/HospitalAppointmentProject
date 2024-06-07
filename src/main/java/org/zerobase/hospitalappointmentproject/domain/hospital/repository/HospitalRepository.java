package org.zerobase.hospitalappointmentproject.domain.hospital.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {

  Optional<HospitalEntity> findByName(String name);
  boolean existsByName(String name);
  List<HospitalEntity> findDistinctByDoctors_Specialty_Name(String specialtyName);
  @Query(value = "SELECT h.*, " +
      "(6371 * acos(cos(radians(:lat)) * cos(radians(h.latitude)) * cos(radians(h.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(h.latitude)))) AS distance " +
      "FROM Hospital h " +
      "WHERE (6371 * acos(cos(radians(:lat)) * cos(radians(h.latitude)) * cos(radians(h.longitude) - radians(:lon)) + sin(radians(:lat)) * sin(radians(h.latitude)))) < :radius " +
      "ORDER BY distance",
      nativeQuery = true)
  List<HospitalEntity> findNearByHospital(@Param("lat") double lat, @Param("lon") double lon, @Param("radius") double radius);

}
