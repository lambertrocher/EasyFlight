package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Reservoir;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Reservoir entity.
 */
public interface ReservoirSearchRepository extends ElasticsearchRepository<Reservoir, Long> {
}
