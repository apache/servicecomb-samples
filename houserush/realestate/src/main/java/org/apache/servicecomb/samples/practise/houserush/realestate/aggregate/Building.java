package org.apache.servicecomb.samples.practise.houserush.realestate.aggregate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name="buildings")
@SQLDelete(sql = "update buildings set deleted_at = now() where id = ?")
@Where(clause = "deleted_at is null")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "realestate_id")
    private Realestate realestate;

    @JsonIgnore
    @OneToMany(mappedBy = "building")
    private List<House> houses = new ArrayList<>();

    private String name;

    private Integer sequenceInRealestate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

}
