package org.zerobase.hospitalappointmentproject.domain.hospital.mapper;

import org.mapstruct.Mapper;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;

@Mapper(componentModel = "spring")
public interface HospitalMapper {

  HospitalDto toDto(HospitalEntity entity);

}
