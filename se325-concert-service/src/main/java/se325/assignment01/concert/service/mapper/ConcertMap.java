package se325.assignment01.concert.service.mapper;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import se325.assignment01.concert.common.dto.ConcertDTO;
import se325.assignment01.concert.common.dto.ConcertSummaryDTO;
import se325.assignment01.concert.service.domain.Concert;

/**
 * CONCERTMAP
 * This class is used to map the DTO and domain classes together. This class' implementations
 * will be used by the JAX-RS service.
 */
public class ConcertMap {

	private ConcertMap() {
	}

	/**
	 * Convert from DTO object to domain object
	 * @param dtoConcert
	 * @return Concert object
	 */
	public static Concert toDomainModel(ConcertDTO dtoConcert) {
		// Get the fields from the DTO object and save as a concert
		Concert properConc = new Concert(
				dtoConcert.getId(),
				dtoConcert.getTitle(),
				dtoConcert.getImageName(),
				dtoConcert.getBlurb()
		);

		return properConc;
	}

	/**
	 * Convert from domain model to DTO
	 *
	 * @param conc (Concert object)
	 * @return ConcertDTO object
	 */
	public static ConcertDTO toDTOModel(Concert conc) {
		// Constructor implementation for ConcertDTO
		ConcertDTO dtoObj = new ConcertDTO(conc.getId(), conc.getTitle(), conc.getImageName(), conc.getBlurb());

		// To fill performer set and date set, where performer => default iterator of forEach() method
		conc.getPerformers().forEach(performer -> dtoObj.getPerformers().add(PerformerMap.toDTOModel(performer)));
		dtoObj.getDates().addAll(conc.getDates());

		// Return the complete object to dtoObject
		return dtoObj;
	}


	/**
	 * Create a summary object for a Concert object
	 */
	public static ConcertSummaryDTO toConcSummaryDTO(Concert conc) {
		return new ConcertSummaryDTO(conc.getId(), conc.getTitle(), conc.getImageName());
	}
}
