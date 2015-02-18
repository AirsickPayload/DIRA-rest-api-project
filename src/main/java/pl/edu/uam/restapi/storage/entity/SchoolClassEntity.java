package pl.edu.uam.restapi.storage.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Created by alan on 26.12.14.
 */
@Entity
@Table(name = "classes")
@NamedQueries({
        @NamedQuery(name = "classes.findAll", query = "SELECT u FROM SchoolClassEntity u")
})
public class SchoolClassEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(SchoolClassEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "profile")
    private String profile;

    @Column(name = "shortcut")
    private String shortcut;

    @Column(name = "year")
    private int year;

    public SchoolClassEntity() {
    }

    public SchoolClassEntity(String profile, String shortcut, int year) {
        this.profile = profile;
        this.shortcut = shortcut;
        this.year = year;
    }

    public SchoolClassEntity(Long id, String profile, String shortcut, int year) {
        this.id = id;
        this.profile = profile;
        this.shortcut = shortcut;
        this.year = year;
    }

    public Long getId() {
        return id;
    }

    public String getProfile() {
        return profile;
    }

    public String getShortcut() {
        return shortcut;
    }

    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }


    public String toString(){
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("profile", profile)
                .add("shortcut", shortcut)
                .add("year", year)
                .toString();
    }

    public int getYear() {
        return year;
    }
}
