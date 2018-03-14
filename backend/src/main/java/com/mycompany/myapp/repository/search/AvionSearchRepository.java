package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Avion;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Avion entity.
 */
public interface AvionSearchRepository extends ElasticsearchRepository<Avion, Long> {
}
