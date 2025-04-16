package org.hkijena.olr.repositories;

import org.hkijena.olr.model.entities.Resource;
import org.springframework.data.repository.CrudRepository;

public interface ResourceRepository extends CrudRepository<Resource, Long> {
}
