package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Maintenance;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Maintenance entity.
 */
public interface MaintenanceSearchRepository extends ElasticsearchRepository<Maintenance, Long> {
}
