package Monument.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@javax.persistence.Entity
public class Entity {
    @Id
    @GenericGenerator(name = "autoincrement", strategy = "increment")
    @GeneratedValue(generator="autoincrement")
    private int id;
    private String name;
    private String location;
}
