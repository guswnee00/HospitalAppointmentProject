package org.zerobase.hospitalappointmentproject.domain.hospital.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;

@Repository
public interface HospitalRepository extends JpaRepository<HospitalEntity, Long> {

  Optional<HospitalEntity> findByName(String name);
  boolean existsByName(String name);

}
