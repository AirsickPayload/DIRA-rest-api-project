package pl.edu.uam.restapi.storage.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by alan on 23.12.14.
 */

@Entity
@Table(name = "students")
@NamedQueries({
        @NamedQuery(name = "students.findAll", query = "SELECT u FROM StudentEntity u")
})
public class StudentEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudentEntity.class);

    @Id
    private String pesel;

    @Column(name = "name")
    private String name;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "birthdate")
    @Temporal(TemporalType.DATE)
    private java.util.Date birthdate;

    @Column(name = "city")
    private String city;

    public StudentEntity(String pesel, String name, String lastname, Date birthdate, String city) {
        this.pesel = pesel;
        this.name = name;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.city = city;
    }

    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }

    public StudentEntity() {}

    public String getPesel() {
        return pesel;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getCity() {
        return city;
    }

    public String toString(){
        return Objects.toStringHelper(this)
                .add("pesel", pesel)
                .add("name", name)
                .add("lastname", lastname)
                .add("birthdate", birthdate)
                .add("city", city)
                .toString();
    }
}
