package se325.assignment01.concert.service.mapper;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import se325.assignment01.concert.common.dto.PerformerDTO;
import se325.assignment01.concert.service.domain.Performer;

/**
 * PERFORMERMAP
 * This class is used to map the DTO and domain classes together. This class' implementations
 * will be used by the JAX-RS service.
 */

public class PerformerMap {

	private PerformerMap() {
	}

	/**
	 * Converting a DTO object to its domain object
	 * @param dtoPerformer
	 * @return
	 */
	public static Performer toDomainModel(PerformerDTO dtoPerformer) {
		Performer properPerformer = new Performer(
				dtoPerformer.getId(),
				dtoPerformer.getName(),
				dtoPerformer.getImageName(),
				dtoPerformer.getGenre(),
				dtoPerformer.getBlurb()
		);

		return properPerformer;
	}

	/**
	 * Converting a Performer object to an object of PerformerDTO type
	 * This function also deals with the list of performers (from Concert object) inputted one at a time
 	 */
	public static PerformerDTO toDTOModel(Performer perf) {
		return new PerformerDTO(perf.getId(), perf.getName(), perf.getImageName(),
				perf.getGenre(), perf.getBlurb());
	}
}
