package org.zerobase.hospitalappointmentproject.domain.patient.mapper;

import org.mapstruct.Mapper;
import org.zerobase.hospitalappointmentproject.domain.patient.dto.PatientDto;
import org.zerobase.hospitalappointmentproject.domain.patient.entity.PatientEntity;

@Mapper(componentModel = "spring")
public interface PatientMapper {

  PatientDto toDto(PatientEntity entity);

}
