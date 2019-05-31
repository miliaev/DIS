package ru.nsu.fit.g15203.converter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import ru.nsu.fit.g15203.dto.NodeDto;
import ru.nsu.fit.g15203.model.NodeEntity;
import ru.nsu.fit.g15203.model.TagEntity;

public class NodeConverter {
    public static NodeDto toApi(NodeEntity node) {
        if (node == null) {
            return null;
        }

        Map<String, String> tags = CollectionUtils.emptyIfNull(node.getTags())
            .stream()
            .collect(Collectors.toMap(TagEntity::getKey, TagEntity::getValue));

        return new NodeDto(
            node.getId(),
            node.getLatitude(),
            node.getLongitude(),
            node.getName(),
            tags
        );
    }

    public static NodeEntity fromApi(NodeDto node) {
        if (node == null) {
            return null;
        }

        List<TagEntity> dbTags = Optional.ofNullable(node.getTags())
            .map(tags -> tags
                .entrySet()
                .stream()
                .map(e -> new TagEntity().setKey(e.getKey()).setValue(e.getValue()))
                .collect(Collectors.toList()))
            .orElse(Collections.emptyList());

        return new NodeEntity()
            .setLatitude(node.getLatitude())
            .setLongitude(node.getLongitude())
            .setName(node.getName())
            .addAllTags(dbTags);
    }
}
