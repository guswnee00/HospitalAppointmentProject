package org.zerobase.hospitalappointmentproject.domain.hospital.service;

import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.CURRENT_PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_IS_ALREADY_REGISTERED;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.HOSPITAL_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_DOES_NOT_MATCH;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.PASSWORD_IS_REQUIRED_TO_UPDATE_INFO;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.STAFF_NOT_FOUND;
import static org.zerobase.hospitalappointmentproject.global.exception.ErrorCode.THIS_HOSPITAL_NAME_ALREADY_EXISTS;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.doctor.entity.DoctorEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalDto;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalInfoUpdate;
import org.zerobase.hospitalappointmentproject.domain.hospital.dto.HospitalRegister;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;
import org.zerobase.hospitalappointmentproject.domain.specialty.entity.SpecialtyEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.entity.StaffEntity;
import org.zerobase.hospitalappointmentproject.domain.staff.repository.StaffRepository;
import org.zerobase.hospitalappointmentproject.global.exception.CustomException;

@Service
@RequiredArgsConstructor
public class HospitalService {

  private final HospitalRepository hospitalRepository;
  private final StaffRepository staffRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final HospitalElasticRepository hospitalElasticRepository;

  /**
   * 병원 관계자의 병원 등록
   *    1. 병원 관계자가 이미 자신의 병원을 등록했는지 확인
   *    2. 동일한 병원 이름이 존재하는지 확인
   *    3. 병원 관계자 엔티티에 병원 저장
   *    4. 병원 등록 후 dto 반환
   */
  @Transactional
  public HospitalDto register(String username, HospitalRegister.Request request) {

    StaffEntity staff = staffRepository.findByUsername(username)
                                        .orElseThrow(() -> new CustomException(STAFF_NOT_FOUND));

    if (staff.getHospital() != null) {
      throw new CustomException(HOSPITAL_IS_ALREADY_REGISTERED);
    }

    if (hospitalRepository.existsByName(request.getName())) {
      throw new CustomException(THIS_HOSPITAL_NAME_ALREADY_EXISTS);
    }

    HospitalEntity hospital = hospitalRepository.save(HospitalRegister.Request.toEntity(request));

    staff = staff.toBuilder().hospital(hospital).build();
    staffRepository.save(staff);

    return HospitalDto.toDto(hospital);

  }

  /*
   * 병원 관계자의 병원 정보 수정
   *    1. 비밀번호 입력 확인(정보를 변경하기 위해서는 비밀번호 입력해야함)
   *    2. 병원 관계자 엔티티 가져와서 비밀번호 확인
   *    3. 해당 스태프의 엔티티에 연결된 병원 엔티티 가져오기
   *    4. 정보 업데이트 후 dto 반환
   */
  @Transactional
  public HospitalDto updateInfo(String username, HospitalInfoUpdate.Request request) {

    if (request.getPassword() == null) {
      throw new CustomException(PASSWORD_IS_REQUIRED_TO_UPDATE_INFO);
    }

    StaffEntity staff = staffRepository.findByUsername(username)
                                       .orElseThrow(() -> new CustomException(STAFF_NOT_FOUND));

    if (!bCryptPasswordEncoder.matches(request.getPassword(), staff.getPassword())) {
      throw new CustomException(CURRENT_PASSWORD_DOES_NOT_MATCH);
    }

    HospitalEntity hospital = staff.getHospital();
    HospitalEntity updateEntity = hospitalRepository.save(request.toUpdateEntity(hospital));

    return HospitalDto.toDto(updateEntity);

  }

  /**
   * 병원 관계자의 병원 삭제
   *    1. 아이디로 병원 관계자 엔티티 가져오기
   *    2. 해당 관계자의 병원 엔티티 가져오기
   *    3. 비밀번호를 입력했고 그 비밀번호가 일치하는지 확인
   *    4. 병원 관계자 엔티티에서 병원과의 연결을 끊은 뒤
   *    5. 엔티티 삭제
   */
  @Transactional
  public void delete(String username, String password) {

    StaffEntity staff = staffRepository.findByUsername(username)
                                       .orElseThrow(() -> new CustomException(STAFF_NOT_FOUND));

    HospitalEntity hospital = staff.getHospital();

    if (password == null || !bCryptPasswordEncoder.matches(password, staff.getPassword())) {
      throw new CustomException(PASSWORD_DOES_NOT_MATCH);
    }

    staffRepository.save(staff.toBuilder().hospital(null).build());

    hospitalRepository.delete(hospital);

  }

  /**
   * 해당 병원에서 진료 가능한 진료 과목 리스트
   *    1. 병원 이름으로 병원 검색 한 뒤
   *    2. 해당 병원의 의사들을 가져와
   *    3. 의사들의 진료 과목을 list 로 만들어 반환
   */
  @Cacheable(value = "specialties", key = "#hospitalName")
  public List<String> specialtyList(String hospitalName) {

    HospitalEntity hospital = hospitalRepository.findByName(hospitalName)
                                                .orElseThrow(() -> new CustomException(HOSPITAL_NOT_FOUND));

    Set<DoctorEntity> doctors = hospital.getDoctors();

    return doctors.stream()
        .map(DoctorEntity::getSpecialty)
        .filter(Objects::nonNull)
        .map(SpecialtyEntity::getName)
        .distinct()
        .toList();

  }

  /**
   * 병원 이름으로 병원 조회
   */
  public List<HospitalDocument> searchByHospitalName(String hospitalName) {

    return hospitalElasticRepository.findByNameContaining(hospitalName);

  }

  /**
   * 내 위치 근처 병원 조회
   */
  public List<HospitalDocument> searchNearBy(double lat, double lon, double radius) {

    GeoPoint location = new GeoPoint(lat, lon);
    Distance distance = new Distance(radius, Metrics.KILOMETERS);

    return hospitalElasticRepository.findByLocationNear(location, distance);

  }

  /**
   * 진료 과목으로 병원 조회
   */
  public List<HospitalDocument> searchBySpecialtyName(String specialtyName) {

    return hospitalElasticRepository.findBySpecialtiesContaining(specialtyName);

  }

}
