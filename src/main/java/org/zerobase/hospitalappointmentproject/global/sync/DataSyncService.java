package org.zerobase.hospitalappointmentproject.global.sync;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;
import org.zerobase.hospitalappointmentproject.domain.hospital.entity.HospitalEntity;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalElasticRepository;
import org.zerobase.hospitalappointmentproject.domain.hospital.repository.HospitalRepository;

@Service
@RequiredArgsConstructor
public class DataSyncService {

  private static final int INITIAL_PAGE = 0;
  private static final int PAGE_SIZE = 100;

  private final HospitalRepository hospitalRepository;
  private final HospitalElasticRepository hospitalElasticRepository;

  @Transactional(readOnly = true)
  public void syncHospitals() {

    Pageable pageable = PageRequest.of(INITIAL_PAGE, PAGE_SIZE);

    Page<HospitalEntity> hospitalPage;
    do {
      hospitalPage = hospitalRepository.findAll(pageable);
      List<HospitalDocument> hospitalDocuments = hospitalPage.getContent()
                                                             .stream()
                                                             .map(this::convertHospitals)
                                                             .toList();
      hospitalElasticRepository.saveAll(hospitalDocuments);
      pageable = pageable.next(); // 다음 페이지로 이동
    } while (hospitalPage.hasNext());

  }

  private HospitalDocument convertHospitals(HospitalEntity hospital) {

    return HospitalDocument.builder()
        .id(hospital.getId())
        .name(hospital.getName())
        .address(hospital.getAddress())
        .location(new GeoPoint(hospital.getLatitude(), hospital.getLongitude()))
        .contactNumber(hospital.getContactNumber())
        .description(hospital.getDescription())
        .openTime(hospital.getOpenTime())
        .closeTime(hospital.getCloseTime())
        .lunchStartTime(hospital.getLunchStartTime())
        .lunchEndTime(hospital.getLunchEndTime())
        .specialties(hospital.getDoctors()
                              .stream()
                              .map(doctor -> doctor.getSpecialty().getName())
                              .collect(Collectors.toSet()))
        .build();

  }

}
