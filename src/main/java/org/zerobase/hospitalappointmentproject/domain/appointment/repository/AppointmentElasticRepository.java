package org.zerobase.hospitalappointmentproject.domain.appointment.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.zerobase.hospitalappointmentproject.domain.appointment.document.AppointmentDocument;

@Repository
public interface AppointmentElasticRepository extends ElasticsearchRepository<AppointmentDocument, Long> {

}
