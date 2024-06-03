package org.zerobase.hospitalappointmentproject.domain.appointment.mapper;

import org.mapstruct.Mapper;
import org.zerobase.hospitalappointmentproject.domain.appointment.dto.AppointmentDto;
import org.zerobase.hospitalappointmentproject.domain.appointment.entity.AppointmentEntity;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

  AppointmentDto toDto(AppointmentEntity entity);

}
