package com.etna.sportevents.services;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.repositories.SportRepository;
import com.etna.sportevents.models.SportEntity;
import com.etna.sportevents.payloads.request.CreateSportPayload;
import com.etna.sportevents.payloads.request.UpdateSportPayload;
import com.etna.sportevents.payloads.response.Sport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SportService {
	@Autowired
	private SportRepository sportRepository;

	private Sport mapSportEntityToSport(SportEntity sportEntity) {
		Sport sport = new Sport();
		sport.setId(sportEntity.getId());
		sport.setName(sportEntity.getName());
		return sport;
	}

	public Page<Sport> getAllSports(
			int cursor,
			int size,
			String query
	) {
		Page<SportEntity> sports = sportRepository.findAllByIdGreaterThanAndNameContainingIgnoreCase(
			cursor,
			query.toLowerCase(),
			PageRequest.of(0, size, Sort.by("id").ascending())
		);
		return sports.map(this::mapSportEntityToSport);
	}

	public Sport getSportById(int id) throws NotFoundException {
		SportEntity existing = sportRepository.findById(id).orElse(null);
		if (existing == null) {
			throw new NotFoundException("Sport not found");
		}
		return mapSportEntityToSport(existing);
	}

	public Sport createSport(CreateSportPayload createSportPayload) throws AlreadyExistsException {
		if (sportRepository.existsByName(createSportPayload.getName().toLowerCase())) {
			throw new AlreadyExistsException("Sport already exists");
		}
		SportEntity sport = new SportEntity();
		sport.setName(createSportPayload.getName().toLowerCase());
		SportEntity created = sportRepository.save(sport);
		return mapSportEntityToSport(created);
	}

	public Sport updateSportById(int id, UpdateSportPayload updateSportPayload) throws NotFoundException, AlreadyExistsException {
		SportEntity sport = sportRepository.findById(id).orElse(null);
		if (sport == null) {
			throw new NotFoundException("Sport not found");
		}
		if (sportRepository.existsByName(updateSportPayload.getName().toLowerCase())) {
			throw new AlreadyExistsException("Sport already exists");
		}
		sport.setName(updateSportPayload.getName().toLowerCase());
		SportEntity updated = sportRepository.save(sport);
		return mapSportEntityToSport(updated);
	}

	public void deleteSportById(int id) throws NotFoundException {
		SportEntity sport = sportRepository.findById(id).orElse(null);
		if (sport == null) {
			throw new NotFoundException("Sport not found");
		}
		sportRepository.delete(sport);
	}
}
