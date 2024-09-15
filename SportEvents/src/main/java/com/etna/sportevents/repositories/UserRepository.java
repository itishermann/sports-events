package com.etna.sportevents.repositories;

import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.models.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserRepository extends PagingAndSortingRepository<UserEntity, Integer> {
    Boolean existsByUsername(String username);

    UserEntity findByUsername(String username);

    Page<UserEntity> findAllByIdGreaterThan(int id, Pageable pageable);

    UserEntity findFirstByIdGreaterThan(int id);
    long countAllByRoleIs(UserRole role);
}
