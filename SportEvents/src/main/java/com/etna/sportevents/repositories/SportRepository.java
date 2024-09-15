package com.etna.sportevents.repositories;

import com.etna.sportevents.models.SportEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SportRepository extends PagingAndSortingRepository<SportEntity, Integer> {
    Page<SportEntity> findAllByIdGreaterThanAndNameContainingIgnoreCase(int id, String name, Pageable pageable);

    Boolean existsByName(String name);

    SportEntity findFirstByIdGreaterThan(int id);
}
