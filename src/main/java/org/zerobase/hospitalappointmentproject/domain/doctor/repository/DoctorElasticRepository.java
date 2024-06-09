package org.zerobase.hospitalappointmentproject.domain.doctor.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.doctor.document.DoctorDocument;

@Repository
public interface DoctorElasticRepository extends ElasticsearchRepository<DoctorDocument, Long> {

}
