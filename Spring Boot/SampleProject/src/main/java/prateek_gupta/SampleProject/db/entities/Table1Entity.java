package prateek_gupta.SampleProject.db.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import prateek_gupta.SampleProject.db.vo.Table1VO;

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

    public Table1VO toVO() {
        Table1VO vo = new Table1VO();
        vo.setPrimaryKey(primaryKey);
        vo.setCol1(col1 != null ? col1 : "");
        vo.setCol2(col2 != null ? col2 : false);
        return vo;
    }
}
