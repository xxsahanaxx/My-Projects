package se325.assignment01.concert.service.mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import se325.assignment01.concert.common.dto.BookingDTO;
import se325.assignment01.concert.common.dto.SeatDTO;
import se325.assignment01.concert.service.domain.Reservation;

/**
 * RESERVATIONMAP
 * This class is used to map the DTO and domain classes together. This class' implementations
 * will be used by the JAX-RS service.
 */

public class ReservationMap {
	private ReservationMap() {
	}

	/**
	 * Connects the reservation domain model to its relative DTO class
	 */
	public static BookingDTO toDTOModel(Reservation b) {
		List<SeatDTO> seats = new ArrayList<>();
		b.getSeats().forEach(seat -> seats.add(SeatMap.toDTOModel(seat)));
		return new BookingDTO(b.getId(), b.getDate(), seats);
	}
}
