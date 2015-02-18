package pl.edu.uam.restapi.storage.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alan on 25.12.14.
 */

@Entity
@Table(name = "teachers")
@NamedQueries({
        @NamedQuery(name = "teachers.findAll", query = "SELECT u FROM TeacherEntity u")
})
public class TeacherEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeacherEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "employmentdate")
    @Temporal(TemporalType.DATE)
    private java.util.Date employmentdate;

    @Column(name = "qualifications")
    private String qualifications;

    public TeacherEntity() {}

    public TeacherEntity(String name, String lastname, String qualifications, Date employmentdate) {
        this.name = name;
        this.lastname = lastname;
        this.qualifications = qualifications;
        this.employmentdate = employmentdate;
    }

    public TeacherEntity(Long id, String name, String lastname, String qualifications, Date employmentdate) {
        this.id = id;
        this.name = name;
        this.employmentdate = employmentdate;
        this.qualifications = qualifications;
        this.lastname = lastname;
    }

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

    public String getLastname() {
        return lastname;
    }

    public Date getEmploymentdate() {
        return employmentdate;
    }

    public String getQualifications() {
        return qualifications;
    }

    public String toString(){
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("lastname", lastname)
                .add("employmentdate", employmentdate)
                .add("qualifications", qualifications)
                .toString();
    }
}
