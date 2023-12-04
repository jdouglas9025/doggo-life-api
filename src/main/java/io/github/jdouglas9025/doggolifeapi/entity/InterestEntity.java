package io.github.jdouglas9025.doggolifeapi.entity;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node("Interest")
public class InterestEntity {
    //Attributes
    @Id
    private final String name;
    private final String category;

    public InterestEntity(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }
}
