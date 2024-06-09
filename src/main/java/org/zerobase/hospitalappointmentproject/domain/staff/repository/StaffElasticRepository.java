package org.zerobase.hospitalappointmentproject.domain.staff.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.staff.document.StaffDocument;

@Repository
public interface StaffElasticRepository extends ElasticsearchRepository<StaffDocument, Long> {

}
