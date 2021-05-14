package se325.assignment01.concert.service.domain;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * SEATINDEX CLASS
 * This class allows us to create a composite primary key for a seat in a concert, and hence only the
 * seatTier and booking date are of relevance in this class.
 * This is a value type class because it cannot exist without a reference to a Seat field
 * Booking date is checked so that two bookings at the same time by different users is
 * taken into consideration.
 *
 * label	label/tier of the seat
 * date		date of the booking
 *
 */

public class SeatIndex implements Serializable {
	private static final long serialVersionUID = -5504649016409889372L;
	private String label;
	private LocalDateTime date;

	public SeatIndex() {
	}

	// Parameterised constructor
	public SeatIndex(String label, LocalDateTime date) {
		this.label = label;
		this.date = date;
	}

	// Override methods
	@Override
	public int hashCode() {
		return Objects.hash(date, label);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SeatIndex)) {
			return false;
		}
		SeatIndex other = (SeatIndex) obj;
		return Objects.equals(date, other.date) && Objects.equals(label, other.label);
	}
}
