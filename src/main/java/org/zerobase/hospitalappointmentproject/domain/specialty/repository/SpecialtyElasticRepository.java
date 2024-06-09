package org.zerobase.hospitalappointmentproject.domain.specialty.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.specialty.document.SpecialtyDocument;

@Repository
public interface SpecialtyElasticRepository extends ElasticsearchRepository<SpecialtyDocument, Long> {

}
