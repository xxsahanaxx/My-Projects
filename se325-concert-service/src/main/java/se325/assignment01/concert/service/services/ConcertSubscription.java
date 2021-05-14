package se325.assignment01.concert.service.services;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import javax.ws.rs.container.AsyncResponse;

/**
 * CONCERTSUBSCRIPTIONSERVICE CLASS
 * This class is meant to hold the elements required to run the subscribe/publish tests.
 */
public class ConcertSubscription {

	// Asynchronous response helps in scalability
	public final AsyncResponse response;
	
	// This variable notifies the user when a specific set of seats are booked.
	public final double percentageTarget;
    
	public ConcertSubscription(AsyncResponse response, int percentageBooked) {
		this.response = response;
		this.percentageTarget = percentageBooked / 100.0;
	}   
}
