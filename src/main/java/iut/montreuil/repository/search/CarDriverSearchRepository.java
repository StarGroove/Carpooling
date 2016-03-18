package iut.montreuil.repository.search;

import iut.montreuil.domain.CarDriver;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the CarDriver entity.
 */
public interface CarDriverSearchRepository extends ElasticsearchRepository<CarDriver, Long> {
}
