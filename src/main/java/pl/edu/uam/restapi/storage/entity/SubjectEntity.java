package pl.edu.uam.restapi.storage.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Created by alan on 07.01.2015.
 */

@Entity
@Table(name = "subjects")
@NamedQueries({
        @NamedQuery(name = "subjects.findAll", query = "SELECT u FROM SubjectEntity u")
})
public class SubjectEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "shortname")
    private String shortname;


    public SubjectEntity(Long id, String name, String shortname) {
        this.id = id;
        this.name = name;
        this.shortname = shortname;
    }

    public SubjectEntity(String name, String shortname) {
        this.name = name;
        this.shortname = shortname;
    }

    public SubjectEntity() {}

    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortname() {
        return shortname;
    }

    public String toString(){
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("shortname", shortname)
                .toString();
    }
}
