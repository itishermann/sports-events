package com.etna.sportevents.controllers;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.HttpException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.payloads.request.CreateSportPayload;
import com.etna.sportevents.payloads.request.UpdateSportPayload;
import com.etna.sportevents.payloads.response.CursorMeta;
import com.etna.sportevents.payloads.response.HttpResponse;
import com.etna.sportevents.payloads.response.Sport;
import com.etna.sportevents.services.SportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/sport")
@Validated
public class SportController {
    @Autowired
    SportService sportService;

    @GetMapping()
    public ResponseEntity<HttpResponse<List<Sport>>> getAllSports(
            @RequestParam(defaultValue = "0") Integer cursor,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "") String query
    ) {
        Page<Sport> sports = sportService.getAllSports(cursor, size, query);
        HttpResponse<List<Sport>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Sports retrieved successfully");
        httpResponse.setData(sports.getContent());
        httpResponse.setCode("OK");
        httpResponse.setMeta(new CursorMeta(sports, cursor, size));
        return ResponseEntity.ok(httpResponse);
    }

    @GetMapping("{id}")
    public ResponseEntity<HttpResponse<Sport>> getSportById(
            @PathVariable("id") @Min(1) int id
    ) throws HttpException {
        try {
            Sport sport = sportService.getSportById(id);
            HttpResponse<Sport> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("Sport retrieved successfully");
            httpResponse.setData(sport);
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (NotFoundException e) {
            throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping()
    public ResponseEntity<HttpResponse<Sport>> createSport(
            @Valid @RequestBody CreateSportPayload createSportPayload
            ) throws HttpException {
           try {
               Sport sport = sportService.createSport(createSportPayload);
               HttpResponse<Sport> httpResponse = new HttpResponse<>();
                httpResponse.setMessage("Sport created successfully");
                httpResponse.setData(sport);
                httpResponse.setCode("CREATED");
               return ResponseEntity.status(HttpStatus.CREATED).body(httpResponse);
           } catch (AlreadyExistsException e) {
               throw new HttpException(e.getMessage(), HttpStatus.CONFLICT);
           } catch (Exception e) {
               throw new HttpException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
           }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("{id}")
    public ResponseEntity<HttpResponse<Sport>> updateSportById(
            @PathVariable("id") @Min(1) int id,
            @Valid @RequestBody UpdateSportPayload updateSportPayload
            ) throws HttpException {
        try {
            Sport sport = sportService.updateSportById(id, updateSportPayload);
            HttpResponse<Sport> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("Sport updated successfully");
            httpResponse.setData(sport);
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (NotFoundException e) {
            throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AlreadyExistsException e) {
            throw new HttpException(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("{id}")
    public ResponseEntity<HttpResponse<Void>> deleteSportById(
            @PathVariable("id") int id
    ) throws HttpException {
        try {
            sportService.deleteSportById(id);
            HttpResponse<Void> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("Sport deleted successfully");
            httpResponse.setCode("OK");
            return ResponseEntity.ok(httpResponse);
        } catch (NotFoundException e) {
            throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new HttpException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
