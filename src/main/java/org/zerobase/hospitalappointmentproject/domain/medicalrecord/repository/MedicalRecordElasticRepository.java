package org.zerobase.hospitalappointmentproject.domain.medicalrecord.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.medicalrecord.document.MedicalRecordDocument;

@Repository
public interface MedicalRecordElasticRepository extends ElasticsearchRepository<MedicalRecordDocument, Long> {

}
