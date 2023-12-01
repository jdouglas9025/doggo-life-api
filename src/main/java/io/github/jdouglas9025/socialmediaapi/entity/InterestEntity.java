package io.github.jdouglas9025.socialmediaapi.entity;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node("Interest")
public class InterestEntity {
    //Attributes
    @Id
    private final String value;

    public InterestEntity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
