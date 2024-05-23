package org.zerobase.hospitalappointmentproject.domain.staff.mapper;

import org.mapstruct.Mapper;
import org.zerobase.hospitalappointmentproject.domain.staff.dto.StaffDto;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;

@Mapper(componentModel = "spring")
public interface StaffMapper {

  StaffDto toDto(StaffEntity entity);

}
