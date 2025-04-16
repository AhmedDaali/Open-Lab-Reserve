package org.hkijena.olr.repositories;

import org.hkijena.olr.model.entities.ResourceType;
import org.springframework.data.repository.CrudRepository;

public interface ResourceTypeRepository extends CrudRepository<ResourceType, Long> {
}
