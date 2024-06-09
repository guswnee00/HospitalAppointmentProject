package org.zerobase.hospitalappointmentproject.domain.hospital.repository;

import java.util.List;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;

@Repository
public interface HospitalElasticRepository extends ElasticsearchRepository<HospitalDocument, Long> {

  List<HospitalDocument> findByNameContaining(String name);

}
