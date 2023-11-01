package prateek_gupta.spring;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "table_1")
public class Table1Entity {
    @Id
    @GenericGenerator(name = "autoincrement", strategy = "increment")
    @GeneratedValue(generator = "autoincrement")
    @Column(name = "primary_key")
    Integer primaryKey;

    @Column(name = "col_1")
    String col1;

    @Column(name = "col_2")
    Boolean col2;
}
