package org.zerobase.hospitalappointmentproject.domain.doctor.mapper;

import org.mapstruct.Mapper;
import org.zerobase.hospitalappointmentproject.domain.doctor.dto.DoctorDto;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

  DoctorDto toDto(DoctorEntity entity);

}
