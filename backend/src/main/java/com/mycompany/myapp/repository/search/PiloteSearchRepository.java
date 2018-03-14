package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Pilote;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Pilote entity.
 */
public interface PiloteSearchRepository extends ElasticsearchRepository<Pilote, Long> {
}
