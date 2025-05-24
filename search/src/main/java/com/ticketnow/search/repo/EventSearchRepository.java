package com.ticketnow.search.repo;

import com.ticketnow.search.domain.EventSearch;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Optional;

public interface EventSearchRepository extends ElasticsearchRepository<EventSearch, String> {
    @Query("""
        {
          "bool": {
            "must": {
              "multi_match": {
                "query": "?0",
                "fields": ["*"],
                "type": "phrase_prefix"
              }
            },
            "filter": {
              "term": {
                "public_events_is_approved": true
              }
            }
          }
        }
    """)
    List<EventSearch> searchAllFields(String keyword);

    List<EventSearch> findAllByCategoryAndApproved(String category, Boolean approved);

    @Query("""
        {
          "bool": {
            "must": {
              "more_like_this": {
                "fields": ["public_events_title", "public_events_description"],
                "like": "?0",
                "min_term_freq": 1,
                "min_doc_freq": 1,
                "max_query_terms": 10,
                "minimum_should_match": "30%"
              }
            },
            "filter": {
              "term": {
                "public_events_is_approved": true
              }
            }
          }
        }
    """)
    List<EventSearch> searchSimilarEvents(String title, Pageable pageable);

    @Query(
        """
        {
          "bool": {
            "must": {
              "match": {
                "public_events_id": {
                  "query": "?0",
                  "operator": "and"
                }
              }
            },
            "filter": {
              "term": {
                "public_events_is_approved": true
              }
            }
          }
        }
        """
    )
    Optional<EventSearch> findByEventId(Long id);
}
