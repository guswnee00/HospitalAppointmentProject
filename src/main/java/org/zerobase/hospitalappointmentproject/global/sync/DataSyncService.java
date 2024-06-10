package org.zerobase.hospitalappointmentproject.global.sync;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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

  private final HospitalRepository hospitalRepository;
  private final HospitalElasticRepository hospitalElasticRepository;

  @Transactional(readOnly = true)
  public void syncHospitals() {

    List<HospitalEntity> hospitalEntities = hospitalRepository.findAll();
    List<HospitalDocument> hospitalDocuments = hospitalEntities.stream()
                                                               .map(this::convertHospitals)
                                                               .toList();
    hospitalElasticRepository.saveAll(hospitalDocuments);

  }

  private HospitalDocument convertHospitals(HospitalEntity hospital) {

    return HospitalDocument.builder()
        .id(hospital.getId())
        .name(hospital.getName())
        .address(hospital.getAddress())
        .location(new GeoPoint(hospital.getLatitude(), hospital.getLongitude()))
        .contactNumber(hospital.getContactNumber())
        .description(hospital.getDescription())
        .openTime(hospital.getOpenTime().toString())
        .closeTime(hospital.getCloseTime().toString())
        .lunchStartTime(hospital.getLunchStartTime().toString())
        .lunchEndTime(hospital.getLunchEndTime().toString())
        .specialties(hospital.getDoctors()
                              .stream()
                              .map(doctor -> doctor.getSpecialty().getName())
                              .collect(Collectors.toSet()))
        .build();

  }

}
