package org.zerobase.hospitalappointmentproject.domain.hospital.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.hospital.document.HospitalDocument;

@Repository
public interface HospitalElasticRepository extends ElasticsearchRepository<HospitalDocument, Long> {

}
