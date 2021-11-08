package Monument.entity;

import javax.persistence.Id;

@javax.persistence.Entity
public class Entity {
    @Id
    private int id;
    private String name;
    private String location;
}
