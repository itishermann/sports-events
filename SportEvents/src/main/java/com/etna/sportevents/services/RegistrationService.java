package com.etna.sportevents.services;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.exceptions.ForbiddenException;
import com.etna.sportevents.models.EventEntity;
import com.etna.sportevents.models.RegistrationEntity;
import com.etna.sportevents.payloads.response.Event;
import com.etna.sportevents.payloads.response.Registration;
import com.etna.sportevents.payloads.response.Sport;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.repositories.EventRepository;
import com.etna.sportevents.repositories.RegistrationRepository;
import com.etna.sportevents.repositories.UserRepository;
import com.etna.sportevents.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

@Service
public class RegistrationService {
	@Autowired
	private RegistrationRepository registrationRepository;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private UserRepository userRepository;

	private Registration mapRegistrationEntityToRegistration(RegistrationEntity registrationEntity) {
		Event event = new Event();
		event.setId(registrationEntity.getEvent().getId());
		event.setCreationDate(registrationEntity.getEvent().getCreationDate());
		event.setUpdatedDate(registrationEntity.getEvent().getUpdatedDate());
		event.setDateStart(registrationEntity.getEvent().getDateStart());
		event.setDateEnd(registrationEntity.getEvent().getDateEnd());
		event.setLatitude(registrationEntity.getEvent().getLatitude());
		event.setLongitude(registrationEntity.getEvent().getLongitude());
		event.setMax_participants(registrationEntity.getEvent().getMax_participants());
		event.setLocation(registrationEntity.getEvent().getLocation());
		event.setTitle(registrationEntity.getEvent().getTitle());
		Sport sport = new Sport();
		sport.setId(registrationEntity.getEvent().getSport().getId());
		sport.setName(registrationEntity.getEvent().getSport().getName());
		event.setSport(sport);
		User user = new User();
		user.setId(registrationEntity.getUser().getId());
		user.setCreationDate(registrationEntity.getUser().getCreationDate());
		user.setUpdatedDate(registrationEntity.getUser().getUpdatedDate());
		user.setEmail(registrationEntity.getUser().getEmail());
		user.setUsername(registrationEntity.getUser().getUsername());
		user.setRole(registrationEntity.getUser().getRole());
		Registration registration = new Registration();
		registration.setId(registrationEntity.getId());
		registration.setCreationDate(registrationEntity.getCreationDate());
		registration.setUpdatedDate(registrationEntity.getUpdatedDate());
		registration.setEvent(event);
		registration.setUser(user);
		registration.setDate(registrationEntity.getDate());
		registration.setCreationDate(registrationEntity.getCreationDate());
		registration.setUpdatedDate(registrationEntity.getUpdatedDate());
		return registration;
	}

	public Registration getRegistrationById(int id) throws NotFoundException {
		RegistrationEntity registrationEntity = registrationRepository.findById(id).orElse(null);
		if (registrationEntity == null) {
			throw new NotFoundException("Registration not found");
		}
		return mapRegistrationEntityToRegistration(registrationEntity);
	}

	public Page<Registration> getRegistrationsByEventId(
			int id,
			int cursor,
      int size
	) throws NotFoundException {
		Pageable pageable = PageRequest.of(
			0,
			size,
			Sort.by("id").ascending()
		);
		Page<RegistrationEntity> registrationEntities = registrationRepository.findAllByIdGreaterThanAndEventIdIs(cursor, id, pageable);
		return registrationEntities.map(this::mapRegistrationEntityToRegistration);
	}

	public Page<Registration> getRegistrationsByUserId(
			int id,
			int cursor,
			int size
	) throws NotFoundException {
		Pageable pageable = PageRequest.of(
			0,
			size,
			Sort.by("id").ascending()
		);
		Page<RegistrationEntity> registrationEntities = registrationRepository.findAllByIdGreaterThanAndUserIdIs(cursor, id, pageable);
		return registrationEntities.map(this::mapRegistrationEntityToRegistration);
	}

	public boolean isUserRegistered(int eventId, int userId) {
		return registrationRepository.existsByEventIdAndUserId(eventId, userId);
	}

	public int getAvailablePlaces(int eventId) {
		EventEntity eventEntity = eventRepository.findById(eventId).orElse(null);
		if(eventEntity == null) {
			return 0;
		}
		return (int) (eventEntity.getMax_participants() - registrationRepository.countByEventId(eventId));
	}

	public Registration createRegistration(int eventId, JwtUserDetails jwtUserDetails) throws AlreadyExistsException, NotFoundException {
		EventEntity eventEntity = eventRepository.findById(eventId).orElse(null);
		if(eventEntity == null) {
			throw new NotFoundException("Event not found");
		}
		if(isUserRegistered(eventId, jwtUserDetails.getId())) {
			throw new AlreadyExistsException("User is already registered to this event");
		}
		RegistrationEntity registrationEntity = new RegistrationEntity();
		registrationEntity.setEvent(eventEntity);
		registrationEntity.setUser(jwtUserDetails.getUser());
		registrationEntity.setDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
		RegistrationEntity created = registrationRepository.save(registrationEntity);
		return mapRegistrationEntityToRegistration(created);
	}

	public void deleteRegistration(int id, JwtUserDetails jwtUserDetails) throws NotFoundException, ForbiddenException {
		RegistrationEntity registrationEntity = registrationRepository.findById(id).orElse(null);
		if (registrationEntity == null) {
			throw new NotFoundException("Registration not found");
		}
		if (!Objects.equals(registrationEntity.getUser().getId(), jwtUserDetails.getId()) && !jwtUserDetails.isAdmin()) {
			throw new ForbiddenException("You are not allowed to delete this registration");
		}
		registrationRepository.delete(registrationEntity);
	}

	public void deleteRegistration(int eventId, int userId, JwtUserDetails jwtUserDetails) throws NotFoundException, ForbiddenException {
		RegistrationEntity registrationEntity = registrationRepository.findByEventIdAndUserId(eventId, userId).orElse(null);
		if (registrationEntity == null) {
			throw new NotFoundException("Registration not found");
		}
		if (
				!Objects.equals(registrationEntity.getUser().getId(), jwtUserDetails.getId())
						&& !jwtUserDetails.isAdmin()
						&& !Objects.equals(registrationEntity.getEvent().getOrganizer().getId(), jwtUserDetails.getId())
		) {
			throw new ForbiddenException("You are not allowed to delete this registration");
		}
		registrationRepository.delete(registrationEntity);
	}
}
