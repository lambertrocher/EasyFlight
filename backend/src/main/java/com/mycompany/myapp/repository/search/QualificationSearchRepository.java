package com.mycompany.myapp.repository.search;

import com.mycompany.myapp.domain.Qualification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Qualification entity.
 */
public interface QualificationSearchRepository extends ElasticsearchRepository<Qualification, Long> {
}
