package ru.nsu.fit.g15203.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.g15203.model.NodeEntity;

public interface NodeRepository extends JpaRepository<NodeEntity, Long> {
}
