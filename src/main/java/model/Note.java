package main.java.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import main.java.model.base.BaseModel;

public class Note extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue
	@Column
	private Long id;
	@NotNull
	@Size(min=1,max=50)
	@Column
	private String title;
	@Size(max = 1000)
	@Column
	private String note;
	@Column
	private Date createdTime;
	@Column
	private Date lastUpdatedTime;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinTable(
			name="user",
			joinColumns=
			@JoinColumn(name="user_id",referencedColumnName="id"),
			inverseJoinColumns = 
			@JoinColumn(name="group_id",referencedColumnName="id")
	)
	private User user;
	
	public Note(){
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	@Size(min=1,max=50)
	public void setTitle(String title) {
		this.title = title;
	}

	public String getNote() {
		return note;
	}
	@Size(max=1000)
	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getLastUpdatedTime() {
		return lastUpdatedTime;
	}

	public void setLastUpdatedTime(Date lastUpdatedTime) {
		this.lastUpdatedTime = lastUpdatedTime;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
}
