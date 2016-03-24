package iut.montreuil.repository.search;

import iut.montreuil.domain.Itinary;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Itinary entity.
 */
public interface ItinarySearchRepository extends ElasticsearchRepository<Itinary, Long> {
}
