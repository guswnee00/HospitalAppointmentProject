package org.zerobase.hospitalappointmentproject.domain.patient.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.patient.document.PatientDocument;

@Repository
public interface PatientElasticRepository extends ElasticsearchRepository<PatientDocument, Long> {

}
