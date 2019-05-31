package ru.nsu.fit.g15203.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString(exclude = "tags", callSuper = true)
@Entity
@Table(name = "node")
public class NodeEntity extends BasePersistentEntity {
    @Column(name = "latitude")
    private Double latitude;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "node", orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private List<TagEntity> tags = new ArrayList<>();

    public NodeEntity addTag(TagEntity tag) {
        tag.setNode(this);
        this.tags.add(tag);
        return this;
    }

    public NodeEntity addAllTags(List<TagEntity> tags) {
        this.tags.clear();
        tags.forEach(this::addTag);
        return this;
    }
}
