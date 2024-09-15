package com.etna.sportevents.payloads.request;

import javax.validation.constraints.*;

public class UpdateCommentPayload {

    @Size(min = 1, max = 255, message = "Comment must be between 1 and 255 characters")
    private String comment;

    @Min(0)
    @Max(5)
    private Float rating;

    private Boolean approved;

    public UpdateCommentPayload(String comment, Float rating, Boolean approved) {
        this.comment = comment;
        this.rating = rating;
        this.approved = approved;
    }

    public UpdateCommentPayload() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }
}
