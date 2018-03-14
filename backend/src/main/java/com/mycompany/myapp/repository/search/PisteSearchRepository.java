package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Piste;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Piste entity.
 */
public interface PisteSearchRepository extends ElasticsearchRepository<Piste, Long> {
}
