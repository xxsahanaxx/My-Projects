package se325.assignment01.concert.service.domain;


/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.*;

/**
 * SEAT CLASS
 * This class allows us to create a seat in a concert.
 *
 * The variable labels are supposed to be the same as the ones in the DTO class.
 * label    the seat label
 * isBooked the boolean value that states if the seat is booked or not
 * bookDate		date of the booking
 * price    the price
 *
 */

@Entity
@Table(name = "SEATS")
@IdClass(SeatIndex.class)
public class Seat {

	@Id
	// label must point to the composite primary class ID -> SeatIndex.label
	private String label;
	private boolean isBooked;

	@Id
	private LocalDateTime date;
	private BigDecimal price;

	// Empty constructor
	public Seat() {
	}

	// Parameterised constructor
	public Seat(String tier, boolean isBooked, LocalDateTime date, BigDecimal price) {
		this.label = tier;
		this.isBooked = isBooked;
		this.date = date;
		this.price = price;
	}


	// Override methods
	@Override
	public int hashCode() {
		return Objects.hash(date, label, price);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Seat)) {
			return false;
		}
		Seat other = (Seat) obj;
		return Objects.equals(date, other.date) && Objects.equals(label, other.label) && Objects.equals(price, other.price);
	}


	// Getters and setter methods
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	public Boolean getIsBooked() {
		return isBooked;
	}
	public void setIsBooked(Boolean isBooked) {
		this.isBooked = isBooked;
	}

	public LocalDateTime getDate() {
		return date;
	}
	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
