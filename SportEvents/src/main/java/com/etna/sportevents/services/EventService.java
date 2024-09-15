package com.etna.sportevents.services;

import com.etna.sportevents.exceptions.InvalidArgException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.exceptions.ForbiddenException;
import com.etna.sportevents.models.EventEntity;
import com.etna.sportevents.models.SportEntity;
import com.etna.sportevents.models.UserEntity;
import com.etna.sportevents.models.UserRole;
import com.etna.sportevents.payloads.request.CreateEventPayload;
import com.etna.sportevents.payloads.request.UpdateEventPayload;
import com.etna.sportevents.payloads.response.Event;
import com.etna.sportevents.payloads.response.Sport;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.repositories.EventRepository;
import com.etna.sportevents.repositories.SportRepository;
import com.etna.sportevents.repositories.UserRepository;
import com.etna.sportevents.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private UserRepository userRepository;

    private Event mapEventEntityToEvent(EventEntity eventEntity) {
        Event event = new Event();
        event.setId(eventEntity.getId());
        event.setCreationDate(eventEntity.getCreationDate());
        event.setUpdatedDate(eventEntity.getUpdatedDate());
        event.setDateStart(eventEntity.getDateStart());
        event.setDateEnd(eventEntity.getDateEnd());
        event.setLatitude(eventEntity.getLatitude());
        event.setLongitude(eventEntity.getLongitude());
        event.setMax_participants(eventEntity.getMax_participants());
        event.setLocation(eventEntity.getLocation());
        event.setTitle(eventEntity.getTitle());
        Sport sport = new Sport();
        sport.setId(eventEntity.getSport().getId());
        sport.setName(eventEntity.getSport().getName());
        event.setSport(sport);
        User organizer = new User();
        organizer.setId(eventEntity.getOrganizer().getId());
        organizer.setUsername(eventEntity.getOrganizer().getUsername());
        organizer.setEmail(eventEntity.getOrganizer().getEmail());
        organizer.setRole(eventEntity.getOrganizer().getRole());
        organizer.setUpdatedDate(eventEntity.getOrganizer().getUpdatedDate());
        organizer.setCreationDate(eventEntity.getOrganizer().getCreationDate());
        event.setOrganizer(organizer);
        return event;
    }

    public Page<Event> getAllEvents(
            int cursor,
            int size
    ) {
        Pageable pageable = PageRequest.of(
            0,
            size,
            Sort.by("id").ascending()
        );
        Page<EventEntity> events = eventRepository.findAllByIdGreaterThan(cursor, pageable);
        return events.map(this::mapEventEntityToEvent);
    }

    public Event getEventById(int id) throws NotFoundException {
        EventEntity existing = eventRepository.findById(id).orElse(null);
        if(existing == null) {
            throw new NotFoundException("Event not found");
        }
        return mapEventEntityToEvent(existing);
    }

    public Page<Event> getEventsByOrganizerId(
        int userId,
        int cursor,
        int size
    ) throws NotFoundException {
        UserEntity user = userRepository.findById(userId).orElse(null);
        if(user == null) {
            throw new NotFoundException("User not found");
        }
        Pageable pageable = PageRequest.of(0,size, Sort.by("id").ascending());
        Page<EventEntity> events = eventRepository.findAllByIdGreaterThanAndOrganizerId(cursor, userId, pageable);
        return events.map(this::mapEventEntityToEvent);
    }

    public Page<Event> getEventsAroundLocation(
            double latitude,
            double longitude,
            double radius,
            int cursor,
            int size
    ) {
        Pageable pageable = PageRequest.of(0,size, Sort.by("id_event").ascending());
        Page<EventEntity> events = eventRepository.findEventsAroundLocation(cursor, latitude, longitude, radius, pageable);
        return events.map(this::mapEventEntityToEvent);
    }

    public Page<Event> getEventsBySportId(
            int sportId,
            int cursor,
            int size
    ) {
        Pageable pageable = PageRequest.of(0,size, Sort.by("id").ascending());
        Page<EventEntity> events = eventRepository.findAllByIdGreaterThanAndSportId(cursor, sportId, pageable);
        return events.map(this::mapEventEntityToEvent);
    }

    public Event createEvent(
            Integer organizerId,
            CreateEventPayload createEventPayload
    ) throws NotFoundException {
        SportEntity sport = sportRepository.findById(createEventPayload.getSportId()).orElse(null);
        if(sport == null) {
            throw new NotFoundException("Sport not found");
        }
        UserEntity organizer = userRepository.findById(organizerId).orElse(null);
        if(organizer == null) {
            throw new NotFoundException("User not found");
        }
        EventEntity eventEntity = new EventEntity();
        eventEntity.setSport(sport);
        eventEntity.setOrganizer(organizer);
        eventEntity.setDateStart(createEventPayload.getDateStart());
        eventEntity.setDateEnd(createEventPayload.getDateEnd());
        eventEntity.setLatitude(createEventPayload.getLatitude());
        eventEntity.setLongitude(createEventPayload.getLongitude());
        eventEntity.setMax_participants(createEventPayload.getMaxParticipants());
        eventEntity.setLocation(createEventPayload.getLocation());
        eventEntity.setTitle(createEventPayload.getTitle());
        EventEntity created = eventRepository.save(eventEntity);
        return mapEventEntityToEvent(created);
    }

    public Event updateEventById(
            Integer eventId,
            UpdateEventPayload updateEventPayload,
            JwtUserDetails jwtUserDetails
    ) throws NotFoundException, InvalidArgException, ForbiddenException {
        EventEntity eventEntity = eventRepository.findById(eventId).orElse(null);
        if(eventEntity == null) {
            throw new NotFoundException("Event not found");
        }
        if(!eventEntity.getOrganizer().getId().equals(jwtUserDetails.getId()) && jwtUserDetails.getRole() != UserRole.ROLE_ADMIN) {
            throw new ForbiddenException("You are not the organizer of this event");
        }
        if(updateEventPayload.getTitle() != null) {
            eventEntity.setTitle(updateEventPayload.getTitle());
        }
        if(updateEventPayload.getLocation() != null) {
            eventEntity.setLocation(updateEventPayload.getLocation());
        }
        if(updateEventPayload.getMaxParticipants() != null) {
            eventEntity.setMax_participants(updateEventPayload.getMaxParticipants());
        }
        if(updateEventPayload.getDateStart() != null) {
            eventEntity.setDateStart(updateEventPayload.getDateStart());
        }
        if(updateEventPayload.getDateEnd() != null) {
            eventEntity.setDateEnd(updateEventPayload.getDateEnd());
        }
        if(updateEventPayload.getLatitude() != null) {
            eventEntity.setLatitude(updateEventPayload.getLatitude());
        }
        if(updateEventPayload.getLongitude() != null) {
            eventEntity.setLongitude(updateEventPayload.getLongitude());
        }
        if(eventEntity.getDateStart().after(eventEntity.getDateEnd())) {
            throw new InvalidArgException("dateStart must be before dateEnd");
        }
        EventEntity updated = eventRepository.save(eventEntity);
        return mapEventEntityToEvent(updated);
    }

    public void deleteEventById(Integer eventId, JwtUserDetails jwtUserDetails) throws NotFoundException, ForbiddenException {
        EventEntity eventEntity = eventRepository.findById(eventId).orElse(null);
        if(eventEntity == null) {
            throw new NotFoundException("Event not found");
        }
        if(!eventEntity.getOrganizer().getId().equals(jwtUserDetails.getId()) && jwtUserDetails.getRole() != UserRole.ROLE_ADMIN) {
            throw new ForbiddenException("You are not the organizer of this event");
        }
        eventRepository.delete(eventEntity);
    }
}
