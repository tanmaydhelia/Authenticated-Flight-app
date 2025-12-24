package com.flightbookingservice.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.flightbookingservice.entity.Passenger;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {


	@Query("SELECT p.seatNumber FROM Passenger p WHERE p.booking.flightId = :flightId AND p.booking.status = 'BOOKED' AND p.seatNumber IN :seatNumbers ")
	List<String> findTakenSeatNumbers(int flightId, Collection<String> seatNumbers);

	@Query("SELECT p.seatNumber FROM Passenger p WHERE p.booking.flightId = :flightId and p.booking.status = 'BOOKED'")
	List<String> findAllOccupiedSeatsByFlightId(int flightId);

	List<Passenger> findByBookingId(int bookingId);
}
