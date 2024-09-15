package com.etna.sportevents.payloads.response;

import javax.persistence.*;
import java.util.Date;
public class Comment {
  private Integer id;
  private String comment;
  private Float rating;
  private Boolean approved;
  private User user = new User();
  private Date creationDate;
  private Date updatedDate;

	private int eventId;

	public Comment() {
	}

	public Comment(Integer id, String comment, Float rating, Boolean approved, User user, Date creationDate, Date updatedDate) {
		this.id = id;
		this.comment = comment;
		this.rating = rating;
		this.approved = approved;
		this.user = user;
		this.creationDate = creationDate;
		this.updatedDate = updatedDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}
}
