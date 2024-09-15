package com.etna.sportevents.repositories;

import com.etna.sportevents.models.RegistrationEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface RegistrationRepository extends PagingAndSortingRepository<RegistrationEntity, Integer> {
	public Page<RegistrationEntity> findAllByIdGreaterThanAndEventIdIs(int id, int eventId, Pageable pageable);

	public Page<RegistrationEntity> findAllByIdGreaterThanAndUserIdIs(int id, int userId, Pageable pageable);

	public long countByEventId(int eventId);
	public boolean existsByEventIdAndUserId(int eventId, int userId);

	public Optional<RegistrationEntity> findByEventIdAndUserId(int eventId, int userId);
}
