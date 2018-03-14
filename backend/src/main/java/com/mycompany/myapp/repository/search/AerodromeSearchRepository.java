package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Aerodrome;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Aerodrome entity.
 */
public interface AerodromeSearchRepository extends ElasticsearchRepository<Aerodrome, Long> {
}
