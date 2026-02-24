package prateek_gupta.SampleProject.db.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "table_1_attachment_mapping")
public class Table1AttachmentMappingEntity {
    @Id
    @GenericGenerator(name = "autoincrement", strategy = "increment")
    @GeneratedValue(generator = "autoincrement")
    @Column(name = "primary_key")
    Integer primaryKey;

    @ManyToOne()
    @JoinColumn(name = "table_1_primary_key")
    Table1Entity table1Entity;

    @Column(name = "attachment_path")
    String attachmentPath;

}
