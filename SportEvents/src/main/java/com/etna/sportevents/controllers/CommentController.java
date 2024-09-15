package com.etna.sportevents.controllers;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.HttpException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.exceptions.ForbiddenException;
import com.etna.sportevents.payloads.request.CreateCommentPayload;
import com.etna.sportevents.payloads.request.UpdateCommentPayload;
import com.etna.sportevents.payloads.response.Comment;
import com.etna.sportevents.payloads.response.CursorMeta;
import com.etna.sportevents.payloads.response.HttpResponse;
import com.etna.sportevents.security.JwtUserDetails;
import com.etna.sportevents.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/api/comment")
@Validated
public class CommentController {
    @Autowired
    private CommentService commentService;


    @GetMapping("/event/{eventId}")
    public ResponseEntity<HttpResponse<List<Comment>>> getAllCommentByEventId(
            @PathVariable int eventId,
            @RequestParam(defaultValue = "0") int cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Comment> comments = commentService.getAllCommentByEventId(eventId, cursor, size);
        HttpResponse<List<Comment>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Comments retrieved successfully");
        httpResponse.setCode("OK");
        httpResponse.setData(comments.getContent());
        httpResponse.setMeta(new CursorMeta(comments, cursor, size));
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @GetMapping("/event/{eventId}/unapproved")
    public ResponseEntity<HttpResponse<List<Comment>>> getAllApprovedCommentByEventId(
            @PathVariable int eventId,
            @RequestParam(defaultValue = "0") int cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Comment> comments = commentService.getAllUnapprovedCommentsByEventId(eventId, cursor, size);
        HttpResponse<List<Comment>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Comments retrieved successfully");
        httpResponse.setCode("OK");
        httpResponse.setData(comments.getContent());
        httpResponse.setMeta(new CursorMeta(comments, cursor, size));
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @PutMapping("/{commentId}/approve")
    public ResponseEntity<HttpResponse<Void>> getAllApprovedCommentByEventId(
            @PathVariable int commentId
    ) throws HttpException {
        try {
            commentService.approveById(commentId);
            HttpResponse<Void> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("Comment approved successfully");
            httpResponse.setCode("OK");
            return new ResponseEntity<>(httpResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_MODERATOR"})
    @GetMapping("/unapproved")
    public ResponseEntity<HttpResponse<List<Comment>>> getAllUnapprovedComments(
            @RequestParam(defaultValue = "0") int cursor,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Comment> comments = commentService.getAllUnapprovedComments(cursor, size);
        HttpResponse<List<Comment>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Comments retrieved successfully");
        httpResponse.setCode("OK");
        httpResponse.setData(comments.getContent());
        httpResponse.setMeta(new CursorMeta(comments, cursor, size));
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}/rating")
    public ResponseEntity<HttpResponse<Float>> getAverageRatingByEventId(
            @PathVariable int eventId
    ) {
        Float averageRating = commentService.getAvgRatingByEventId(eventId);
        HttpResponse<Float> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Average rating retrieved successfully");
        httpResponse.setCode("OK");
        httpResponse.setData(averageRating);
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    @GetMapping("/event/{eventId}/user/{userId}")
    public ResponseEntity<HttpResponse<Boolean>> getCommentByEventIdAndUserId(
            @PathVariable int eventId,
            @PathVariable int userId
    ) throws HttpException {
        boolean comment = commentService.existsByEventIdAndUserId(eventId, userId);
        HttpResponse<Boolean> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("Comment retrieved successfully");
        httpResponse.setCode("OK");
        httpResponse.setData(comment);
        return new ResponseEntity<>(httpResponse, HttpStatus.OK);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<HttpResponse<Comment>> getCommentById(
            @PathVariable int commentId
    ) throws HttpException {
        try {
            Comment comment = commentService.getCommentById(commentId);
            HttpResponse<Comment> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("Comment retrieved successfully");
            httpResponse.setCode("OK");
            httpResponse.setData(comment);
            return new ResponseEntity<>(httpResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/event/{eventId}")
    public ResponseEntity<HttpResponse<Comment>> createComment(
        @PathVariable int eventId,
        @Valid @RequestBody CreateCommentPayload comment,
        Authentication authentication
        ) throws HttpException {
        try {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
            Comment createdComment = commentService.createComment(eventId, jwtUserDetails, comment);
            HttpResponse<Comment> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("Comment created successfully");
            httpResponse.setCode("CREATED");
            httpResponse.setData(createdComment);
            return new ResponseEntity<>(httpResponse, HttpStatus.CREATED);
        } catch (NotFoundException e) {
            throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AlreadyExistsException e) {
            throw new HttpException(e.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<HttpResponse<Comment>> updateComment(
            @PathVariable int commentId,
            @Valid @RequestBody UpdateCommentPayload comment,
            Authentication authentication
    ) throws HttpException {
        try {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
            Comment updatedComment = commentService.updateCommentById(commentId, jwtUserDetails, comment);
            HttpResponse<Comment> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("Comment updated successfully");
            httpResponse.setCode("OK");
            httpResponse.setData(updatedComment);
            return new ResponseEntity<>(httpResponse, HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new HttpException(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ForbiddenException e) {
            throw new HttpException(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<HttpResponse<Void>> deleteComment(
            @PathVariable int commentId,
            Authentication authentication
    ) throws HttpException {
        try {
            JwtUserDetails jwtUserDetails = (JwtUserDetails) authentication.getPrincipal();
            commentService.deleteCommentById(commentId, jwtUserDetails);
            HttpResponse<Void> httpResponse = new HttpResponse<>();
            httpResponse.setMessage("Comment deleted successfully");
            httpResponse.setCode("OK");
            return new ResponseEntity<>(httpResponse, HttpStatus.OK);
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
