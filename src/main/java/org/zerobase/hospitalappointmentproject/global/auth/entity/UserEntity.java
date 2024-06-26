package org.zerobase.hospitalappointmentproject.global.auth.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@ToString
@SuperBuilder(toBuilder = true)
@MappedSuperclass
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {

  private String username;      // 로그인 아이디
  private String password;
  private String role;          // jwt 역할별 접근 제어

  private String name;          // 실제 이름
  private String phoneNumber;
  private String email;

  @CreatedDate
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @LastModifiedDate
  private LocalDateTime modifiedAt;

}
