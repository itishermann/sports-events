package com.etna.sportevents.repositories;

import com.etna.sportevents.models.EventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

public interface EventRepository extends PagingAndSortingRepository<EventEntity, Integer> {
    Page<EventEntity> findAllByIdGreaterThan(int id, Pageable pageable);

    Page<EventEntity> findAllByIdGreaterThanAndOrganizerId(int id, int organizerId, Pageable pageable);
    @Query(
        value = "SELECT * FROM events WHERE (((acos(sin((:latitude*pi()/180)) * sin((latitude*pi()/180)) + cos((:latitude*pi()/180)) * cos((latitude*pi()/180)) * cos(((:longitude-longitude) * pi()/180)))) * 180/pi()) * 60 * 1.1515 * 1.609344) <= :radius AND events.id_event > :cursor",
        countQuery = "SELECT count(*) FROM events WHERE (((acos(sin((:latitude*pi()/180)) * sin((latitude*pi()/180)) + cos((:latitude*pi()/180)) * cos((latitude*pi()/180)) * cos(((:longitude-longitude) * pi()/180)))) * 180/pi()) * 60 * 1.1515 * 1.609344) <= :radius AND events.id_event > :cursor",
        nativeQuery = true
    )
    Page<EventEntity> findEventsAroundLocation(
            @Param("cursor") int cursor,
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radius,
            Pageable pageable
    );

    Page<EventEntity> findAllByIdGreaterThanAndSportId(int id, int sportId, Pageable pageable);
}
