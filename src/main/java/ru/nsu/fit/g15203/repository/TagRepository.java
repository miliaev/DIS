package ru.nsu.fit.g15203.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.fit.g15203.model.TagEntity;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
