package org.hkijena.olr.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.hkijena.olr.model.entities.PhysicalLab;
import org.hkijena.olr.model.entities.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByPhysicalLabAndStartTimeBetween(PhysicalLab lab, LocalDateTime from, LocalDateTime to);

    List<Reservation> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);
}
