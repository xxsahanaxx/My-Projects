package se325.assignment01.concert.service.domain;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * USER CLASS
 * This class represents a registered user.
 *
 * The variables are:
 * username  	the user's unique username.
 * password  	the user's password.
 *
 */
@Entity
@Table(name = "USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Version
	private Long version;

	@Column(unique = true)
	// Username must be unique so that duplicate users do not exist
	private String username;
	private String password;

	@Column(unique = true)
	private UUID sessionId;

	// One user can make many reservations
	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "user")
	private Set<Reservation> bookings = new HashSet<>();

	public User() {
	}

	// Override methods
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		User user = (User) o;
		return new EqualsBuilder().append(username, user.username)
				.append(password, user.password).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37)
				.append(username).append(password).toHashCode();
	}


	// Getter and setter methods
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public UUID getSessionId() {
		return sessionId;
	}
	public void setSessionId(UUID sessionId) {
		this.sessionId = sessionId;
	}


	public Set<Reservation> getBookings() {
		return bookings;
	}


	public void setBookings(Set<Reservation> bookings) {
		this.bookings = bookings;
	}
}
