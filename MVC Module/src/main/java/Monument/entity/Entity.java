package Monument.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@javax.persistence.Entity
@Table
@Getter
@Setter
public class Entity {
    @Id
    @GenericGenerator(name = "autoincrement", strategy = "increment")
    @GeneratedValue(generator="autoincrement")
    private int id;
    private String name;
    private String location;
    private short year;
    private int entryFee;
}
