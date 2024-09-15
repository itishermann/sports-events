package com.etna.sportevents.payloads.request;

import javax.validation.constraints.*;
public class CreateCommentPayload {

    @Size(min = 1, max = 255, message = "Comment must be between 1 and 255 characters")
    private String comment;

    @Min(0)
    @Max(5)
    @NotNull(message = "Rating is mandatory")
    private Float rating;

    public CreateCommentPayload(String comment, Float rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public CreateCommentPayload() {
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
}
