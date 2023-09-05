package com.javainuse.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Value;

@Entity
@Table(name = "order")
public class Order {

	@EmbeddedId
	private OrderId id = new OrderId();

	@ManyToOne
	@MapsId("userId")
	private User user;

	@ManyToOne
	@MapsId("bookId")
	private Book book;

	@CreationTimestamp
	@Column(name = "added_at", nullable = false)
	private Date addedAt;

	@UpdateTimestamp
	@Column(name = "last_update", nullable = false)
	private Date lastUpdate;

	@Column(name = "editable")
	@Value("true")
	private Boolean editable;

	@Column(name = "lasteditor")
	private String lasteditor;

	@Column(name = "deleted")
	@Value("false")
	private Boolean deleted;

	@Column(name = "deletedsecription")
	private String deletedsecription;

	public Order() {
	}

	public Order(OrderId id) {
		this.id = id;

	}

	public OrderId getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Book getBook() {
		return book;
	}

	public Date getAddedAt() {
		return addedAt;
	}

	public Boolean getEditable() {
		return editable;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public String getLasteditor() {
		return lasteditor;
	}

	public String getDeletedsecription() {
		return deletedsecription;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	@Embeddable
	public static class OrderId implements Serializable {

		private static final long serialVersionUID = 1L;

		private Integer userId;
		private Integer bookId;

		public OrderId() {
		}

		public OrderId(Integer userId, Integer bookId) {
			super();
			this.userId = userId;
			this.bookId = bookId;
		}

		public Integer getUserId() {
			return userId;
		}

		public Integer getBookId() {
			return bookId;
		}

		public void setUserId(Integer userId) {
			this.userId = userId;
		}

		public void setBookId(Integer bookId) {
			this.bookId = bookId;
		}
	}
}
