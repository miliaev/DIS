package ru.nsu.fit.g15203.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.nsu.fit.g15203.model.NodeEntity;

public interface NodeRepository extends JpaRepository<NodeEntity, Long> {
    @Query(value = "SELECT * FROM node " +
        "WHERE earth_distance(ll_to_earth(?1, ?2), ll_to_earth(node.latitude, node.longitude)) < ?3",
        nativeQuery = true)
    List<NodeEntity> findByCoordinatesAndRadius(double latitude, double longitude, double radius);
}
