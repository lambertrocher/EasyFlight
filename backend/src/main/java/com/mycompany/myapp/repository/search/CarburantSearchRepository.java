package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Carburant;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Carburant entity.
 */
public interface CarburantSearchRepository extends ElasticsearchRepository<Carburant, Long> {
}
