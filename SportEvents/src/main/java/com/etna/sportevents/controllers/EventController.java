package com.etna.sportevents.controllers;

import com.etna.sportevents.exceptions.HttpException;
import com.etna.sportevents.exceptions.InvalidArgException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.exceptions.ForbiddenException;
import com.etna.sportevents.payloads.request.CreateEventPayload;
import com.etna.sportevents.payloads.request.UpdateEventPayload;
import com.etna.sportevents.payloads.response.CursorMeta;
import com.etna.sportevents.payloads.response.Event;
import com.etna.sportevents.payloads.response.HttpResponse;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/event")
@Validated
public class EventController {
	@Autowired
	EventService eventService;

	@GetMapping()
	public ResponseEntity<HttpResponse<List<Event>>> getAllEvents(
			@RequestParam(defaultValue = "0") Integer cursor,
			@RequestParam(defaultValue = "10") Integer size
	) {
		Page<Event> events = eventService.getAllEvents(cursor, size);
		HttpResponse<List<Event>> httpResponse = new HttpResponse<>();
		httpResponse.setMessage("Events retrieved successfully");
		httpResponse.setData(events.getContent());
		httpResponse.setCode("OK");
		httpResponse.setMeta(new CursorMeta(events, cursor, size));
		return ResponseEntity.ok(httpResponse);
	}

	@GetMapping("{id}")
	public ResponseEntity<HttpResponse<Event>> getEventById(
			@PathVariable("id") int id
	) throws HttpException {
		try {
			Event event = eventService.getEventById(id);
			HttpResponse<Event> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Event retrieved successfully");
			httpResponse.setData(event);
			httpResponse.setCode("OK");
			return ResponseEntity.ok(httpResponse);
		} catch (NotFoundException e) {
			throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("sport/{id}")
	public ResponseEntity<HttpResponse<List<Event>>> getEventsBySportId(
			@PathVariable("id") int id,
			@RequestParam(defaultValue = "0") Integer cursor,
			@RequestParam(defaultValue = "10") Integer size
	) throws HttpException {
		Page<Event> events = eventService.getEventsBySportId(
				id,
				cursor,
				size
		);
		HttpResponse<List<Event>> httpResponse = new HttpResponse<>();
		httpResponse.setMessage("Events retrieved successfully");
		httpResponse.setData(events.getContent());
		httpResponse.setCode("OK");
		httpResponse.setMeta(new CursorMeta(events, cursor, size));
		return ResponseEntity.ok(httpResponse);
	}

	@GetMapping("user/{id}")
	public ResponseEntity<HttpResponse<List<Event>>> getEventsByUserId(
			@PathVariable("id") int id,
			@RequestParam(defaultValue = "0") Integer cursor,
			@RequestParam(defaultValue = "10") Integer size
	) throws HttpException {
		try {
			Page<Event> events = eventService.getEventsByOrganizerId(
					id,
					cursor,
					size
			);
			HttpResponse<List<Event>> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Events retrieved successfully");
			httpResponse.setData(events.getContent());
			httpResponse.setCode("OK");
			httpResponse.setMeta(new CursorMeta(events, cursor, size));
			return ResponseEntity.ok(httpResponse);
		} catch (NotFoundException e) {
			throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("localisation")
	public ResponseEntity<HttpResponse<List<Event>>> getEventsAround(
			@RequestParam("latitude") double lat,
			@RequestParam("longitude") double lng,
			@RequestParam("radius") double radius,
			@RequestParam(defaultValue = "0") Integer cursor,
			@RequestParam(defaultValue = "10") Integer size
	) {
		Page<Event> events = eventService.getEventsAroundLocation(
				lat,
				lng,
				radius,
				cursor,
				size
		);
		HttpResponse<List<Event>> httpResponse = new HttpResponse<>();
		httpResponse.setMessage("Events retrieved successfully");
		httpResponse.setData(events.getContent());
		httpResponse.setCode("OK");
		httpResponse.setMeta(new CursorMeta(events, cursor, size));
		return ResponseEntity.ok(httpResponse);
	}

	@PostMapping()
	public ResponseEntity<HttpResponse<Event>> createEvent(
			Authentication authentication,
			@Valid @RequestBody CreateEventPayload createEventPayload
	) throws HttpException {
		try {
			JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
			Event event = eventService.createEvent(jwtUserDetails.getId(), createEventPayload);
			HttpResponse<Event> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Event created successfully");
			httpResponse.setData(event);
			httpResponse.setCode("CREATED");
			return ResponseEntity.status(HttpStatus.CREATED).body(httpResponse);
		} catch (NotFoundException e) {
			throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("{id}")
	public ResponseEntity<HttpResponse<Event>> updateEvent(
			@PathVariable("id") int id,
			@Valid @RequestBody UpdateEventPayload updateEventPayload,
			Authentication authentication
	) throws HttpException {
		try {
			JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
			Event event = eventService.updateEventById(id, updateEventPayload, jwtUserDetails);
			HttpResponse<Event> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Event updated successfully");
			httpResponse.setData(event);
			httpResponse.setCode("OK");
			return ResponseEntity.ok(httpResponse);
		} catch (NotFoundException e) {
			throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (InvalidArgException e) {
			throw new HttpException(e.getMessage(), HttpStatus.BAD_REQUEST);
		} catch (ForbiddenException e) {
			throw new HttpException(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<HttpResponse<Void>> deleteEvent(
			@PathVariable("id") int id,
			Authentication authentication
	) throws HttpException {
		try {
			JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
			eventService.deleteEventById(id, jwtUserDetails);
			HttpResponse<Void> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Event deleted successfully");
			httpResponse.setCode("OK");
			return ResponseEntity.ok(httpResponse);
		} catch (NotFoundException e) {
			throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (ForbiddenException e) {
			throw new HttpException(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}

	@ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException e) {
        HashMap<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach(constraintViolation -> {
            String propertyPath = constraintViolation.getPropertyPath().toString();
            String message = constraintViolation.getMessage();
            errors.put(propertyPath, message);
        });
        HttpResponse<Void> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Validation failed");
        httpResponse.setErrors(errors);
        httpResponse.setCode("BAD_REQUEST");
        return new ResponseEntity<>(httpResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpException.class)
    ResponseEntity<?> handleException(HttpException e) {
        HttpResponse<Void> httpResponse = new HttpResponse<>();
        httpResponse.setMessage(e.getMessage());
        httpResponse.setCode("UNEXPECTED_ERROR");
        httpResponse.setErrors(e.getMessage());
        return new ResponseEntity<>(httpResponse, e.getStatus());
    }
}
