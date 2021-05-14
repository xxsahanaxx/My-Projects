package se325.assignment01.concert.service.mapper;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import se325.assignment01.concert.common.dto.SeatDTO;
import se325.assignment01.concert.service.domain.Seat;

import java.time.LocalDateTime;

/**
 * SEATMAP
 * This class is used to map the DTO and domain classes together. This class' implementations
 * will be used by the JAX-RS service.
 */

public class SeatMap {

	private SeatMap() {
	}

	/**
	 * The function maps a DTO class model to its domain class
	 */
	public static Seat toDomainModel(SeatDTO dtoSeat) {
		Seat properSeat = new Seat(
				dtoSeat.getLabel(),
				false,
				LocalDateTime.now(),
				dtoSeat.getPrice()
		);

		return properSeat;
	}

	/**
	 * The function maps a domain class model to its DTO class
	 */
	public static SeatDTO toDTOModel(Seat s) {
		return new SeatDTO(s.getLabel(), s.getPrice());
	}
}
