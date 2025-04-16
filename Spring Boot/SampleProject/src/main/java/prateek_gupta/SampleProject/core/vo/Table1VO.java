package prateek_gupta.SampleProject.core.vo;

import lombok.Getter;
import lombok.Setter;
import prateek_gupta.SampleProject.core.entities.Table1Entity;

@Getter
@Setter
public class Table1VO {
    Integer primaryKey;
    String col1;
    boolean col2;

    public Table1Entity toEntity(){
        Table1Entity entity = new Table1Entity();
        entity.setPrimaryKey(primaryKey);
        entity.setCol1(col1);
        entity.setCol2(col2);
        return entity;
    }
}
