package org.zerobase.hospitalappointmentproject.domain.specialty.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.specialty.entity.SpecialtyEntity;

@Repository
public interface SpecialtyRepository extends JpaRepository<SpecialtyEntity, Long> {

  Optional<SpecialtyEntity> findByName(String name);

}
