package org.zerobase.hospitalappointmentproject.domain.hospital.repository;

import java.util.List;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.geo.Distance;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;

@Repository
public interface HospitalElasticRepository extends ElasticsearchRepository<HospitalDocument, Long> {

  List<HospitalDocument> findByNameContaining(String hospitalName);
  List<HospitalDocument> findByLocationNear(GeoPoint location, Distance distance);
  List<HospitalDocument> findBySpecialtiesContaining(String specialtyName);

}
