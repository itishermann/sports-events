package com.etna.sportevents.controllers;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.HttpException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.exceptions.ForbiddenException;
import com.etna.sportevents.payloads.response.CursorMeta;
import com.etna.sportevents.payloads.response.HttpResponse;
import com.etna.sportevents.payloads.response.Registration;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/registration")
@Validated
public class RegistrationController {
	@Autowired
	private RegistrationService registrationService;

	@GetMapping("/{id}")
	public ResponseEntity<HttpResponse<Registration>> getRegistrationById(
			@PathVariable("id") int id
	) throws HttpException {
		try {
			Registration registration = registrationService.getRegistrationById(id);
			HttpResponse<Registration> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Registration retrieved successfully");
			httpResponse.setData(registration);
			httpResponse.setCode("OK");
			return ResponseEntity.ok(httpResponse);
		} catch (NotFoundException e) {
			throw HttpException.notFound(e.getMessage());
		}
	}

	@GetMapping("/event/{event_id}/user")
	public ResponseEntity<HttpResponse<List<Registration>>> getAllRegistrationsByEventId(
			@PathVariable("event_id") @Min(1) int event_id,
			@RequestParam(defaultValue = "0") Integer cursor,
			@RequestParam(defaultValue = "10") Integer size
	) throws HttpException {
		try {
			Page<Registration> registrations = registrationService.getRegistrationsByEventId(event_id, cursor, size);
			HttpResponse<List<Registration>> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Registrations retrieved successfully");
			httpResponse.setData(registrations.getContent());
			httpResponse.setCode("OK");
			httpResponse.setMeta(new CursorMeta(registrations, cursor, size));
			return ResponseEntity.ok(httpResponse);
		} catch (NotFoundException e) {
			throw HttpException.notFound(e.getMessage());
		}
	}

	@GetMapping("/event/{event_id}/available-places")
	public ResponseEntity<HttpResponse<Integer>> isEventAvailable(
			@PathVariable("event_id") @Min(1) int event_id
	) throws HttpException {
		int available = registrationService.getAvailablePlaces(event_id);
		HttpResponse<Integer> httpResponse = new HttpResponse<>();
		httpResponse.setMessage("Event availability retrieved successfully");
		httpResponse.setData(available);
		httpResponse.setCode("OK");
		return ResponseEntity.ok(httpResponse);
	}

	@GetMapping("user/{user_id}")
	public ResponseEntity<HttpResponse<List<Registration>>> getAllRegistrationsByUserId(
			@PathVariable("user_id") @Min(1) int user_id,
			@RequestParam(defaultValue = "0") Integer cursor,
			@RequestParam(defaultValue = "10") Integer size
	) throws HttpException {
		try {
			Page<Registration> registrations = registrationService.getRegistrationsByUserId(user_id, cursor, size);
			HttpResponse<List<Registration>> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Registrations retrieved successfully");
			httpResponse.setData(registrations.getContent());
			httpResponse.setCode("OK");
      httpResponse.setMeta(new CursorMeta(registrations, cursor, size));
			return ResponseEntity.ok(httpResponse);
		} catch (NotFoundException e) {
			throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("event/{event_id}")
	public ResponseEntity<HttpResponse<Registration>> createRegistration(
      @PathVariable("event_id") @Min(1) int event_id,
      Authentication authentication
  ) throws HttpException {
		try {
      JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
      Registration registration = registrationService.createRegistration(event_id, jwtUserDetails);
      HttpResponse<Registration> httpResponse = new HttpResponse<>();
      httpResponse.setMessage("Registration created successfully");
      httpResponse.setData(registration);
      httpResponse.setCode("CREATED");
      return new ResponseEntity<HttpResponse<Registration>>(httpResponse, HttpStatus.CREATED);
    } catch (NotFoundException e) {
      throw HttpException.notFound(e.getMessage());
    } catch (AlreadyExistsException e) {
      throw new HttpException(e.getMessage(), HttpStatus.CONFLICT);
    }
	}

	@GetMapping("event/{event_id}/user/{user_id}")
	public ResponseEntity<HttpResponse<Boolean>> getRegistrationByEventIdAndUserId(
			@PathVariable("event_id") @Min(1) int event_id,
			@PathVariable("user_id") @Min(1) int user_id
	) {
		boolean registration = registrationService.isUserRegistered(event_id, user_id);
		HttpResponse<Boolean> httpResponse = new HttpResponse<>();
		httpResponse.setMessage("Registration retrieved successfully");
		httpResponse.setData(registration);
		httpResponse.setCode("OK");
		return ResponseEntity.ok(httpResponse);
	}

	@DeleteMapping("event/{event_id}/user/{user_id}")
	public ResponseEntity<HttpResponse<Void>> deleteRegistrationByEventIdAndUserId(
			@PathVariable("event_id") @Min(1) int event_id,
			@PathVariable("user_id") @Min(1) int user_id,
			Authentication authentication
	) throws HttpException {
		try {
			JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
			registrationService.deleteRegistration(event_id, user_id, jwtUserDetails);
			HttpResponse<Void> httpResponse = new HttpResponse<>();
			httpResponse.setMessage("Registration deleted successfully");
			httpResponse.setCode("OK");
			return ResponseEntity.ok(httpResponse);
		} catch (NotFoundException e) {
			throw HttpException.notFound(e.getMessage());
		} catch (ForbiddenException e) {
			throw new HttpException(e.getMessage(), HttpStatus.FORBIDDEN);
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<HttpResponse<Void>> deleteRegistrationById(
			@PathVariable("id") int id,
			Authentication authentication
	) throws HttpException {
		try {
				JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
        registrationService.deleteRegistration(id, jwtUserDetails);
        HttpResponse<Void> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Registration deleted successfully");
        httpResponse.setCode("OK");
        return ResponseEntity.ok(httpResponse);
    } catch (NotFoundException e) {
        throw HttpException.notFound(e.getMessage());
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
