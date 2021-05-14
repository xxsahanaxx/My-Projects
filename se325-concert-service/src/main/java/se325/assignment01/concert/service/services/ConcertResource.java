package se325.assignment01.concert.service.services;

import java.net.URI;
import java.time.LocalDateTime;

/**
 * Author: Sahana Srinivasan
 * UPI: ssri365
 * AUID: 677618824
 */

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.persistence.*;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Response.Status;

import se325.assignment01.concert.common.dto.*;
import se325.assignment01.concert.service.domain.*;
import se325.assignment01.concert.service.mapper.*;

import se325.assignment01.concert.common.types.BookingStatus;

import se325.assignment01.concert.service.jaxrs.LocalDateTimeParam;
import se325.assignment01.concert.service.util.TheatreLayout;

/**
 * CONCERTRESOURCE CLASS
 * This class deals with the testing methods of the concert reservation system.
 * There are a total of 24 (plus 5 publish & subscribe) tests that need to be passed from ConcertResourceIT
 * class.
 */

@Path("/concert-service")
/**
 * Since the entire class deals with Jackson objects, it is better to declare the Jackson annotations
 * (@Produces and @Consumes) for the entire class.
 */
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ConcertResource {

	// From the test file, it can be found that the authentication token is "auth"
	public static final String STRINGAUTH = "auth";
	private static final ExecutorService THREAD_POOL = Executors.newSingleThreadExecutor();
	private static final ConcurrentHashMap<LocalDateTime, LinkedList<ConcertSubscription>> subscriptions = new ConcurrentHashMap<>();

	/**
	 * Test 1, 2 & 3:
	 * PATH: /concerts/{id}
	 * REQUEST: To get a concert record from the stored table when an ID parameter is sent, and its multiple
	 * 			performers and dates if they exist.
	 * RESPONSE: The request should return a 200 response (OK), with the requested concert DTO.
	 * 			The concert DTO should contain all performers and dates for that concert.
	 *      	 If no concert exists with that ID, a 404 (Not found) message is returned.
	 */
	@GET
	@Path("/concerts/{id}")
	public Response retrieveConcert(@PathParam("id") long id) {
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {

			// Start a transaction for persisting the data.
			em.getTransaction().begin();

			// Load object by its id
			Concert concert = em.find(Concert.class, id);

			// Save the persisted object
			em.getTransaction().commit();

			if (concert == null) {
				// Return a HTTP 404 response if the specified Concert isn't found.
				return Response.status(Status.NOT_FOUND).build();
			}

			// Otherwise, create a ResponseBuilder object to return the DTO object of the selected Concert
			Response.ResponseBuilder builder = Response.ok(ConcertMap.toDTOModel(concert));
			return builder.build();

		}
		finally {
			em.close();
		}
	}

	/**
	 * Test 4:
	 * PATH: /concerts
	 * REQUEST: To get all concerts from the stored table
	 * RESPONSE: The request should return a 200 response (OK), when returned number of concerts is 8.
	 * 			Also, every concert must have at least one performer, and must run on at least one date.
	 *      	 If those conditions are not met, a 204 (No content) message is returned.
	 */
	@GET
	@Path("/concerts")
	public Response retrieveAllConcerts() {
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();

			// Run a query to obtain all Concert records (like Lab 04 query search method)
			List<Concert> concerts = em.createQuery("SELECT c FROM Concert c", Concert.class)
					.getResultList();

			if (concerts.isEmpty()) {
				// Return a No Content response
				return Response.noContent().build();
			}

			// Save the persisted query information
			em.getTransaction().commit();

			// Convert obtained concert records to DTO objects
			Set<ConcertDTO> dtoConcerts = new HashSet<>();
			concerts.forEach(concert -> dtoConcerts.add(ConcertMap.toDTOModel(concert)));

			return Response.ok(dtoConcerts).build();
		} finally {
			em.close();
		}
	}

	/**
	 * Test 5:
	 * PATH: /concerts/summaries
	 * REQUEST: all concert records' summaries are to be returned. Concert summaries contain
	 * 			only the id, title, and image name for each concert.
	 * RESPONSE: 200 if OK
	 * 			 204 if no concerts are found
	 */
	@GET
	@Path("/concerts/summaries")
	public Response retrieveConcSummaries() {
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();

			// Query to check from Concert objects, similar to previous test
			List<Concert> concerts = em.createQuery("SELECT c FROM Concert c", Concert.class)
					.getResultList();

			if (concerts.isEmpty()) {
				// Return no content response
				return Response.noContent().build();
			}

			// Otherwise, convert the objects to their respective DTO objects
			Set<ConcertSummaryDTO> dtoSummaries = new HashSet<>();
			concerts.forEach(concert -> dtoSummaries.add(ConcertMap.toConcSummaryDTO(concert)));

			// Save persisted query
			em.getTransaction().commit();

			return Response.ok(dtoSummaries).build();
		} finally {
			em.close();
		}
	}

	/**
	 * Test 6 & 7:
	 * PATH: /performers/{id}
	 * REQUEST: A single performer object must be returned given the ID as a parameter.
	 * RESPONSE: a 200 response is returned, if everything is returned as expected.
	 * 			 404 if object not found.
	 */
	@GET
	@Path("/performers/{id}")
	public Response retrievePerformer(@PathParam("id") long id) {
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {

			em.getTransaction().begin();
			Performer performer = em.find(Performer.class, id);

			if (performer == null) {
				return Response.status(Status.NOT_FOUND).build();
			}

			return Response.ok(PerformerMap.toDTOModel(performer)).build();

		} finally {

			em.close();
		}
	}

	/**
	 * Test 8:
	 * PATH: /performers
	 * REQUEST: A set of all performer objects in their DTO forms.
	 * RESPONSE: 200 when OK
	 * 			 204 if no content received
	 */
	@GET
	@Path("/performers")
	public Response retrieveAllPerformers() {
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();
			List<Performer> performers = em.createQuery("SELECT p FROM Performer p", Performer.class)
					.getResultList();

			if (performers.isEmpty()) {
				// Return no content response
				return Response.noContent().build();
			}

			// Convert the obtained Performer objects to DTO form
			Set<PerformerDTO> dtoPerformers = new HashSet<>();
			performers.forEach(performer -> dtoPerformers.add(PerformerMap.toDTOModel(performer)));

			// Save persisted objects
			em.getTransaction().commit();

			return Response.ok(dtoPerformers).build();
		}
		finally {
			em.close();
		}
	}


	/**
	 * Test 9, 10 & 11:
	 * PATH: /login
	 * REQUEST: Checks the authentication of a user using username and password entries. An
	 * 			authentication token is generated if the login is successful.
	 * RESPONSE: 200 when authentication successful (and hence a token is created)
	 * 			 401 when authentication fails (and hence no token is created)
	 */
	@POST
	@Path("/login")
	public Response checkLogin(UserDTO testUser) {
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();

			// Create a temporary User object that catches a record if it exists in the database
			User user;
			try {
				user = em.createQuery("SELECT u FROM User u where u.username = :username AND u.password = :password", User.class)
						.setParameter("username", testUser.getUsername())
						.setParameter("password", testUser.getPassword())
						.getSingleResult();
			}
			// if no record exists already, the user is not allowed to access the reservation service
			catch (NoResultException e) {
				// Return unauthorised response message
				return Response.status(Status.UNAUTHORIZED).build();
			} finally {
				// Save for this "catch exception" code block
				em.getTransaction().commit();
			}

			// TODO this implementation
			return Response.ok().cookie(createSession(user, em)).build();
		} finally {
			em.close();
		}
	}

	/**
	 * Test 12, 13, 14 & 15:
	 * PATH: /bookings
	 * REQUEST: Attempt to make a seat booking (must be a POST request) for a user that's already logged in.
	 * 			This reservation happens for one concert on a particular date.
	 * RESPONSE: 201 when a valid reservation has been made, or valid reservation has been returned
	 * 			 400 if a concert doesn't exist for that date
	 * 			 401 error when attempting to book while not logged in, and no booking is actually made.
	 * 			 403 when unable to view other users' seats, or book occupied seats
	 */
	@POST
	@Path("/bookings")
	public Response reservationResource(BookingRequestDTO reserveReq, @CookieParam(STRINGAUTH) Cookie cookie) {
		EntityManager em = PersistenceManager.instance().createEntityManager();

		// If there is no authentication cookie, then it is not possible to have a valid user
		if (cookie == null) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		try {
			User found = null;
			em.getTransaction().begin();

			try {
				// Check if the inputted details correspond to a valid user
				found = em.createQuery("SELECT u FROM User u where u.sessionId = :uuid", User.class)
						.setParameter("uuid", UUID.fromString(cookie.getValue()))
						.getSingleResult();
			} catch (NoResultException e) {
				// Login cannot happen, as no result exists
			}

			// Save persisted
			em.getTransaction().commit();

			// Check if the concert exists for that particular date or not
			em.getTransaction().begin();
			Concert concert = em.find(Concert.class, reserveReq.getConcertId());
			if (concert == null || !concert.getDates().contains(reserveReq.getDate())) {
				return Response.status(Status.BAD_REQUEST).build();
			}
			em.getTransaction().commit();

			// Check if selected seats are free
			em.getTransaction().begin();
			List<Seat> freeRequestedSeats = em.createQuery("SELECT s FROM Seat s WHERE s.label IN :label " +
					"AND s.date = :date AND s.isBooked = false", Seat.class)
					.setParameter("label", reserveReq.getSeatLabels())
					.setParameter("date", reserveReq.getDate())
					.getResultList();

			// If seats are already occupied, reservation cannot be made
			if (freeRequestedSeats.size() != reserveReq.getSeatLabels().size()) {
				return Response.status(Status.FORBIDDEN).build();
			}

			// Otherwise reservation can be made
			Reservation reservation = new Reservation(found, reserveReq.getConcertId(), reserveReq.getDate());
			reservation.getSeats().addAll(freeRequestedSeats);
			freeRequestedSeats.forEach(seat -> seat.setIsBooked(true));
			em.persist(reservation);

			// Get remaining seats for notifications (subscriber option)
			int remainingSeats = em.createQuery("SELECT COUNT(s) FROM Seat s WHERE s.date = :date AND s.isBooked = false", Long.class)
					.setParameter("date", reserveReq.getDate())
					.getSingleResult()
					.intValue();


			// Finds the subscriptions made for that concert date
			findSubscribers(reserveReq.getDate(), remainingSeats);

			em.getTransaction().commit();

			// If reservation can be made, it should correspond to a path like /bookings/{id}
			return Response.created(URI.create("/concert-service/bookings/" + reservation.getId())).build();
		} finally {
			if (em.getTransaction().isActive()) {
				em.getTransaction().commit();
			}
		}
	}

	/**
	 * Test 16:
	 * PATH: /bookings
	 * REQUEST: Getting all the reservations made by the user
	 * RESPONSE: 401 error when getting a reservation while not logged in.
	 */
	@GET
	@Path("/bookings")
	public Response getUsersReservations(@CookieParam(STRINGAUTH) Cookie cookie) {
		EntityManager em = PersistenceManager.instance().createEntityManager();

		// If there is no authentication cookie, then it is not possible to have a valid user
		if (cookie == null) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		try {
			User found = null;
			em.getTransaction().begin();

			try {
				// Check if the inputted details correspond to a valid user
				found = em.createQuery("SELECT u FROM User u where u.sessionId = :uuid", User.class)
						.setParameter("uuid", UUID.fromString(cookie.getValue()))
						.getSingleResult();
			} catch (NoResultException e) {
				// Login cannot happen, as no result exists
			}
			if(found == null){
				return Response.status(Status.UNAUTHORIZED).build();
			}
			em.getTransaction().commit();

			// Get reservations and convert to DTO models
			em.getTransaction().begin();

			Set<BookingDTO> dtoBookings = new HashSet<>();
			found.getBookings().forEach(reservation -> dtoBookings.add(ReservationMap.toDTOModel(reservation)));

			em.getTransaction().commit();

			return Response.ok(dtoBookings).build();
		} finally {
			em.close();
		}
	}

	/**
	 * PATH: /bookings/{id}
	 * REQUEST: A specific reservation entry
	 * RESPONSE: 401 if user isn't logged in
	 * 			 403 if logged in user is not the reservation's user
	 */
	@GET
	@Path("/bookings/{id}")
	public Response getReservation(@PathParam("id") Long id, @CookieParam(STRINGAUTH) Cookie cookie) {
		EntityManager em = PersistenceManager.instance().createEntityManager();
		// If there is no authentication cookie, then it is not possible to have a valid user
		if (cookie == null) {
			return Response.status(Status.UNAUTHORIZED).build();
		}

		try {
			User found = null;
			em.getTransaction().begin();

			try {
				// Check if the inputted details correspond to a valid user
				found = em.createQuery("SELECT u FROM User u where u.sessionId = :uuid", User.class)
						.setParameter("uuid", UUID.fromString(cookie.getValue()))
						.getSingleResult();
			} catch (NoResultException e) {
				// Login cannot happen, as no result exists
			}
			if (found == null) {
				return Response.status(Status.UNAUTHORIZED).build();
			}
			em.getTransaction().commit();

			em.getTransaction().begin();
			Reservation reservation = em.find(Reservation.class, id);

			if (!reservation.getUser().equals(found)) {
				return Response.status(Status.FORBIDDEN).build();
			}

			return Response.ok(ReservationMap.toDTOModel(reservation)).build();
		} finally {

			em.close();
		}
	}

	/**
	 * PATH: /seats/{date}
	 * OPTIONAL PATH: ?status=[BookingStatus]
	 * REQUEST: Return necessary labels for seats on a specific date
	 * RESPONSE: 400 for date that doesn't exist
	 */
	@GET
	@Path("/seats/{date}")
	public Response checkSeatBooked(@PathParam("date") String dateString, @DefaultValue("Any") @QueryParam("status") BookingStatus status) {

		// Convert the string of date inputted to a LocalDateTime object
		LocalDateTime date = new LocalDateTimeParam(dateString).getLocalDateTime();

		// Set persistence context
		EntityManager em = PersistenceManager.instance().createEntityManager();
		try {
			em.getTransaction().begin();

			// Check whether or not date is valid
			boolean isValid = em.createQuery("SELECT COUNT(s) FROM Seat s WHERE s.date = :date", Long.class)
					.setParameter("date", new LocalDateTimeParam(dateString).getLocalDateTime())
					.getSingleResult()
					.intValue() > 0;

			if(!isValid) {
				// Return a 400 response
				return Response.status(Status.BAD_REQUEST).build();
			}
			em.getTransaction().commit();

			// Get seat labels
			em.getTransaction().begin();
			TypedQuery<Seat> findQuery;
			if (status == BookingStatus.Any) {
				findQuery = em.createQuery("SELECT s FROM Seat s WHERE s.date = :date", Seat.class)
						.setParameter("date", date);
			} else {
				findQuery = em.createQuery("SELECT s FROM Seat s WHERE s.date = :date AND isBooked = :status", Seat.class)
						.setParameter("date", date)
						.setParameter("status", status == BookingStatus.Booked);
			}
			List<Seat> seats = findQuery
					.getResultList();

			// Convert the seats to DTO models
			Set<SeatDTO> dtoSeats = new HashSet<>();
			seats.forEach(seat -> dtoSeats.add(SeatMap.toDTOModel(seat)));

			em.getTransaction().commit();

			return Response.ok(dtoSeats).build();

		} finally {

			em.close();
		}
	}

	/**
	 * PATH: /subscribe/concertInfo
	 * REQUEST: To subscribe to a notification when a percentage of seats are reserved
	 * RESPONSE: 400 if concert doesn't exist
	 * 			 401 if user is not logged in.
	 */
	@POST
	@Path("/subscribe/concertInfo")
	public void subscribeConcert(@Suspended AsyncResponse asyncResponse, @CookieParam(STRINGAUTH) Cookie cookie, ConcertInfoSubscriptionDTO subscriptionDTO) {
		EntityManager em = PersistenceManager.instance().createEntityManager();
		if (cookie == null) {
			// not a valid user, cannot authenticate
			asyncResponse.resume(Response.status(Status.UNAUTHORIZED).build());
			return;
		}

		try {
			User found = null;
			em.getTransaction().begin();

			try {
				// Check if the inputted details correspond to a valid user
				found = em.createQuery("SELECT u FROM User u where u.sessionId = :uuid", User.class)
						.setParameter("uuid", UUID.fromString(cookie.getValue()))
						.getSingleResult();
			} catch (NoResultException e) {
				// Login cannot happen, as no result exists
			}
			if (found == null) {
				// User not logged, return 401 message
				asyncResponse.resume(Response.status(Status.UNAUTHORIZED).build());
				return;
			}
			em.getTransaction().commit();

			em.getTransaction().begin();

			// Check if the concert specified is valid
			Concert concert = em.find(Concert.class, subscriptionDTO.getConcertId());
			if (concert == null || !concert.getDates().contains(subscriptionDTO.getDate())) {
				// if invalid, return 400 message
				asyncResponse.resume(Response.status(Status.BAD_REQUEST).build());
				return;
			}
		} finally {
			em.close();
		}

		// Check if subscription has the date, otherwise add it
		synchronized (subscriptions) {
			if (!subscriptions.contains(subscriptionDTO.getDate())) {
				subscriptions.put(subscriptionDTO.getDate(), new LinkedList<>());
			}
		}
		subscriptions.get(subscriptionDTO.getDate())
				.add(new ConcertSubscription(asyncResponse, subscriptionDTO.getPercentageBooked()));
	}

	// Helper methods
	// --------------------------------------------------------------------

	/**
	 * Creates a new session id for the user that needs it.
	 * A cookie is also created
	 */
	private NewCookie createSession(User user, EntityManager em) {
		em.getTransaction().begin();
		user.setSessionId(UUID.randomUUID());
		em.getTransaction().commit();
		return new NewCookie(STRINGAUTH, user.getSessionId().toString());
	}

	/**
	 * Finds all notification subscriptions for a concert at a given DateTime.
	 */
	private void findSubscribers(LocalDateTime key, int seatsAvailable) {
		THREAD_POOL.submit(() -> {
			double percentageBooked = 1.0 - seatsAvailable / (double) TheatreLayout.NUM_SEATS_IN_THEATRE;
			for (Iterator<ConcertSubscription> iterator = subscriptions.get(key).iterator(); iterator.hasNext();) {
				ConcertSubscription sub = iterator.next();
				if (percentageBooked >= sub.percentageTarget) {
					iterator.remove();
					sub.response.resume(Response.ok(new ConcertInfoNotificationDTO(seatsAvailable)).build());
				}
			}
		});
	}
}

