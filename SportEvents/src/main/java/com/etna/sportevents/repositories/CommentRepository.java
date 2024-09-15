package com.etna.sportevents.repositories;

import com.etna.sportevents.models.CommentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<CommentEntity, Integer> {
	Page<CommentEntity> findAllByIdGreaterThanAndApprovedIsTrueAndEventId(Integer cursor, Integer eventId, Pageable pageable);

	Page<CommentEntity> findAllByIdGreaterThanAndApprovedIsFalseAndEventId(Integer cursor, Integer eventId, Pageable pageable);

	Page<CommentEntity> findAllByIdGreaterThanAndApprovedIsFalse(Integer cursor, Pageable pageable);

	boolean existsByUserIdAndEventId(Integer userId, Integer eventId);

	@Query(value = "SELECT IFNULL(AVG(c.rating),0) FROM comments c WHERE c.id_event = :event_id" , nativeQuery = true)
	float avgRatingByEventId(
			@Param("event_id") Integer id
	);
}
