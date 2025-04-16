package org.hkijena.olr.repositories;

import org.hkijena.olr.model.entities.Group;
import org.springframework.data.repository.CrudRepository;

public interface GroupRepository extends CrudRepository<Group, Long> {
}
