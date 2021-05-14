package se325.assignment01.concert.service.domain;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;

/**
 * RESERVATION CLASS
 * This class allows us to create a reservation for a concert.
 * user		   the user object associated with the reservation
 * concertId   the id of the concert which was booked
 * date        the date on which that concert was booked
 * seats       the seats which were booked for that concert on that date
 */

@Entity
@Table(name = "RESERVATIONS")
public class Reservation {
	
	@Id
	// The primary key is supposed to be a unique field
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	// More than one reservation can be made by one user
	@ManyToOne
	private User user;

	private long concertId;
	private LocalDateTime date;

	// Seats (an ordered list) must be eager-fetched to check their availability before booking
	@OneToMany(fetch = FetchType.EAGER)
	private List<Seat> seats = new ArrayList<>();

	public Reservation() {
	}

	// Sets the reservation class parameters
	public Reservation(User user, long concertId, LocalDateTime date) {
		this.user = user;
		this.concertId = concertId;
		this.date = date;
	}


	// Override methods
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Reservation)) {
			return false;
		}
		Reservation opp = (Reservation) obj;
		return concertId == opp.concertId && Objects.equals(date, opp.date) &&
				Objects.equals(concertId, opp.id) && Objects.equals(user, opp.user);
	}

	@Override
	public int hashCode() {
		return Objects.hash(concertId, date, id, user);
	}


	// Getter and setter methods
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public long getConcertId() {
		return concertId;
	}
	public void setConcertId(long concertId) {
		this.concertId = concertId;
	}

	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public List<Seat> getSeats() {
		return seats;
	}
	public void setSeats(List<Seat> seats) {
		this.seats = seats;
	}

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
