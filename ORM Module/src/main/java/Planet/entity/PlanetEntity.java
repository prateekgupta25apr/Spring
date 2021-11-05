package Planet.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Getter
@Setter
@ToString
@Entity
@Table(name = "planet_details")
public class PlanetEntity {
    @Id
    @Column
    private int id;
    @Column
    private String name;
}
