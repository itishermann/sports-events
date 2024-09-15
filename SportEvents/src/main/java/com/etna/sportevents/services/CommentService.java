package com.etna.sportevents.services;

import com.etna.sportevents.exceptions.AlreadyExistsException;
import com.etna.sportevents.exceptions.NotFoundException;
import com.etna.sportevents.exceptions.ForbiddenException;
import com.etna.sportevents.models.CommentEntity;
import com.etna.sportevents.models.EventEntity;
import com.etna.sportevents.payloads.request.CreateCommentPayload;
import com.etna.sportevents.payloads.request.UpdateCommentPayload;
import com.etna.sportevents.payloads.response.Comment;
import com.etna.sportevents.payloads.response.User;
import com.etna.sportevents.repositories.CommentRepository;
import com.etna.sportevents.repositories.EventRepository;
import com.etna.sportevents.security.JwtUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private EventRepository eventRepository;


    private Comment mapCommentEntityToComment(CommentEntity commentEntity){
        Comment comment = new Comment();
        comment.setId(commentEntity.getId());
        comment.setComment(commentEntity.getComment());
        comment.setRating(commentEntity.getRating());
        comment.setApproved(commentEntity.getApproved());
        User user = new User();
        user.setId(commentEntity.getUser().getId());
        user.setUsername(commentEntity.getUser().getUsername());
        user.setEmail(commentEntity.getUser().getEmail());
        user.setRole(commentEntity.getUser().getRole());
        user.setUpdatedDate(commentEntity.getUser().getUpdatedDate());
        user.setCreationDate(commentEntity.getUser().getCreationDate());
        comment.setUser(user);
        comment.setCreationDate(commentEntity.getCreationDate());
        comment.setUpdatedDate(commentEntity.getUpdatedDate());
        comment.setEventId(commentEntity.getEvent().getId());
        return comment;
    }

    public Page<Comment> getAllCommentByEventId(
            int eventId,
            int cursor,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                0,
                size,
                Sort.by("id").ascending()
        );
        Page<CommentEntity> comments = commentRepository.findAllByIdGreaterThanAndApprovedIsTrueAndEventId(cursor, eventId, pageable);
        return comments.map(this::mapCommentEntityToComment);
    }

    public Page<Comment> getAllUnapprovedCommentsByEventId(
            int eventId,
            int cursor,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                0,
                size,
                Sort.by("id").ascending()
        );
        Page<CommentEntity> comments = commentRepository.findAllByIdGreaterThanAndApprovedIsFalseAndEventId(cursor, eventId, pageable);
        return comments.map(this::mapCommentEntityToComment);
    }

    public Page<Comment> getAllUnapprovedComments(
            int cursor,
            int size
    ) {
        Pageable pageable = PageRequest.of(
                0,
                size,
                Sort.by("id").ascending()
        );
        Page<CommentEntity> comments = commentRepository.findAllByIdGreaterThanAndApprovedIsFalse(cursor, pageable);
        return comments.map(this::mapCommentEntityToComment);
    }

    public float getAvgRatingByEventId(int eventId) {
        return commentRepository.avgRatingByEventId(eventId);
    }

    public Comment getCommentById(int id) throws NotFoundException {
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        if(comment == null) {
            throw new NotFoundException("Comment not found");
        }
        return mapCommentEntityToComment(comment);
    }

    public boolean existsByEventIdAndUserId(int eventId, int userId) {
        return commentRepository.existsByUserIdAndEventId(userId, eventId);
    }

    public void approveById(int id) throws NotFoundException {
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        if(comment == null) {
            throw new NotFoundException("Comment not found");
        }
        comment.setApproved(true);
        commentRepository.save(comment);
    }

    public Comment createComment(
        int eventId,
        JwtUserDetails commenter,
        CreateCommentPayload createCommentPayload
    ) throws AlreadyExistsException, NotFoundException {
        if(commentRepository.existsByUserIdAndEventId(commenter.getId(), eventId)) {
            throw new AlreadyExistsException("A comment already exists for this user and this event");
        }
        EventEntity event = eventRepository.findById(eventId).orElse(null);
        if(event == null) {
            throw new NotFoundException("Event not found");
        }
        CommentEntity comment = new CommentEntity();
        comment.setComment(createCommentPayload.getComment());
        comment.setRating(createCommentPayload.getRating());
        comment.setApproved(false);
        comment.setUser(commenter.getUser());
        comment.setEvent(event);
        CommentEntity created = commentRepository.save(comment);
        return mapCommentEntityToComment(created);
    }

    public Comment updateCommentById(
        int id,
        JwtUserDetails updater,
        UpdateCommentPayload updateCommentPayload
    ) throws NotFoundException, ForbiddenException {
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        if(comment == null) {
            throw new NotFoundException("Comment not found");
        }
        if(!Objects.equals(comment.getUser().getId(), updater.getId()) && !updater.isModerator()) {
            throw new ForbiddenException("You are not allowed to update this comment");
        }
        if(updateCommentPayload.getComment() != null){
            comment.setComment(updateCommentPayload.getComment());
        }
        if(updateCommentPayload.getRating() != null){
            comment.setRating(updateCommentPayload.getRating());
        }
        if(updateCommentPayload.getApproved() != null){
            if(!updater.isModerator()) {
                throw new ForbiddenException("You are not allowed to approve or disapprove this comment");
            }
            comment.setApproved(updateCommentPayload.getApproved());
        }
        CommentEntity updated = commentRepository.save(comment);
        return mapCommentEntityToComment(updated);
    }

    public void deleteCommentById(
        int id,
        JwtUserDetails deleter
    ) throws NotFoundException, ForbiddenException {
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        if(comment == null) {
            throw new NotFoundException("Comment not found");
        }
        if(!Objects.equals(comment.getUser().getId(), deleter.getId()) && !deleter.isModerator()) {
            throw new ForbiddenException("You are not allowed to delete this comment");
        }
        commentRepository.deleteById(id);
    }
}
