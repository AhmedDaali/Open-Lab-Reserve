package org.hkijena.olr.services;

import org.hkijena.olr.model.entities.PhysicalLab;
import org.hkijena.olr.model.entities.Reservation;
import org.hkijena.olr.model.entities.User;
import org.hkijena.olr.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;

    @Autowired
    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    /**
     * Returns all reservations for a specific lab between two dates.
     */
    public List<Reservation> getReservationsForLabBetween(PhysicalLab lab, LocalDateTime from, LocalDateTime to) {
        return reservationRepository.findByPhysicalLabAndStartTimeBetween(lab, from, to);
    }

    /**
     * Returns all reservations in the system within a specific time window.
     */
    public List<Reservation> getReservationsBetween(LocalDateTime from, LocalDateTime to) {
        return reservationRepository.findByStartTimeBetween(from, to);
    }

    /**
     * Creates a new reservation after checking for conflicts.
     */
    public Reservation createReservation(Reservation reservation) {
        // Check for overlapping reservations
        List<Reservation> overlapping = reservationRepository.findByPhysicalLabAndStartTimeBetween(
                reservation.getPhysicalLab(),
                reservation.getStartTime(),
                reservation.getEndTime()
        );

        for (Reservation r : overlapping) {
            if (r.getEndTime().isAfter(reservation.getStartTime())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Time slot is already reserved");
            }
        }

        // Additional validation logic could go here:
        // - Only authenticated users can reserve
        // - Limit reservation duration
        // - Enforce time slots within working hours

        return reservationRepository.save(reservation);
    }

    /**
     * Deletes a reservation if the user is the owner or an admin.
     */
    public void deleteReservation(Long id, User user) {
        Reservation r = reservationRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Reservation not found"));

        // Only the owner of the reservation or an admin can delete it
        if (!r.getUser().getId().equals(user.getId()) && user.getRole() != User.Role.Admin) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You cannot delete this reservation");
        }

        reservationRepository.delete(r);
    }
}
