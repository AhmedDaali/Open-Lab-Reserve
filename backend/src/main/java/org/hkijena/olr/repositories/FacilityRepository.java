package org.hkijena.olr.repositories;

import org.hkijena.olr.model.entities.Facility;
import org.hkijena.olr.payloads.FacilityPayload;
import org.springframework.data.repository.CrudRepository;

public interface FacilityRepository extends CrudRepository<Facility, Long> {
}
