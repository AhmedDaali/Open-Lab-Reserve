package org.hkijena.olr.controller;

import org.hkijena.olr.model.entities.PhysicalLab;
import org.hkijena.olr.model.entities.Reservation;
import org.hkijena.olr.model.entities.User;
import org.hkijena.olr.model.UserPrincipal;
import org.hkijena.olr.payloads.ReservationPayload;
import org.hkijena.olr.repositories.PhysicalLabRepository;
import org.hkijena.olr.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final PhysicalLabRepository physicalLabRepository;

    @Autowired
    public ReservationController(ReservationService reservationService, PhysicalLabRepository physicalLabRepository) {
        this.reservationService = reservationService;
        this.physicalLabRepository = physicalLabRepository;
    }

    /**
     * Get all reservations for a specific lab in a time range.
     */
    @GetMapping("/physical-lab/{id}")
    public ResponseEntity<List<Reservation>> getReservationsForLab(
            @PathVariable("id") Long labId,
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {

        return physicalLabRepository.findById(labId)
                .map(lab -> {
                    List<Reservation> reservations = reservationService.getReservationsForLabBetween(lab, from, to);
                    return ResponseEntity.ok(reservations);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
}


    /**
     * Create a new reservation.
     */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody ReservationPayload payload,
                                                         Authentication authentication) {
        if (!(authentication.getPrincipal() instanceof UserPrincipal userPrincipal)) {
            return ResponseEntity.status(403).build();
        }

        User user = userPrincipal.getUser();

        // Convert the payload to Reservation
        PhysicalLab lab = physicalLabRepository.findById(payload.getPhysicalLabId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid lab ID"));

        Reservation reservation = new Reservation();
        reservation.setPhysicalLab(lab);
        reservation.setStartTime(payload.getStartTime());
        reservation.setEndTime(payload.getEndTime());
        reservation.setUser(user);

        Reservation saved = reservationService.createReservation(reservation);
        return ResponseEntity.ok(saved);
    }


    /**
     * Delete a reservation by ID (only if the user is the owner or an admin).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id,
                                                    Authentication authentication) {
        if (authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
            User user = userPrincipal.getUser();
            reservationService.deleteReservation(id, user);
            return ResponseEntity.ok("Reservation deleted.");
        }

        return ResponseEntity.status(403).body("Forbidden");
    }
}
