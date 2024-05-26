package org.zerobase.hospitalappointmentproject.domain.staff.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.global.common.PersonRole;

public class StaffSignup {

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Request {

    private String username;
    private String password;
    private String checkingPassword;

    private String name;
    private String phoneNumber;
    private String email;

    public static StaffEntity toEntity(Request request) {

      return StaffEntity.builder()
          .username(request.getUsername())
          .password(request.getPassword())
          .role(PersonRole.ROLE_STAFF.toString())
          .name(request.getName())
          .phoneNumber(request.getPhoneNumber())
          .email(request.getEmail())
          .build();

    }

  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  @Builder
  public static class Response {

    private String username;

    private String name;
    private String phoneNumber;
    private String email;

    private LocalDateTime createdAt;

    public static Response fromDto(StaffDto dto) {

      return Response.builder()
          .username(dto.getUsername())
          .name(dto.getName())
          .phoneNumber(dto.getPhoneNumber())
          .email(dto.getEmail())
          .createdAt(dto.getCreatedAt())
          .build();

    }

  }

}
