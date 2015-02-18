package pl.edu.uam.restapi.storage.entity;

import com.google.common.base.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;

/**
 * Created by alan on 09.01.2015.
 */
@Entity
@Table(name = "teacherclasssubjectassignment", uniqueConstraints = @UniqueConstraint(columnNames = {"teacherid", "classid", "subjectid"}))
@NamedQueries({
        @NamedQuery(name = "teacherclasssubjectassignment.findAll", query = "SELECT u FROM TCSAssignmentEntity u"),
        @NamedQuery(name = "teacherclasssubjectassignment.findTeacher", query = "SELECT u from TCSAssignmentEntity u where u.teacherEntity = :teacherid"),
        @NamedQuery(name = "teacherclasssubjectassignment.findClass", query = "SELECT u from TCSAssignmentEntity u where u.schoolClassEntity = :classid"),
        @NamedQuery(name = "teacherclasssubjectassignment.findSubject", query = "SELECT u from TCSAssignmentEntity u where u.subjectEntity = :subjectid"),
        @NamedQuery(name = "teacherclasssubjectassignment.findTeacherAndClass", query = "SELECT u from TCSAssignmentEntity u where u.teacherEntity = :teacherid AND u.schoolClassEntity = :classid"),
        @NamedQuery(name = "teacherclasssubjectassignment.findTeacherAndSubject", query = "SELECT u from TCSAssignmentEntity u where u.teacherEntity = :teacherid AND u.subjectEntity = :subjectid"),
        @NamedQuery(name = "teacherclasssubjectassignment.findClassAndSubject", query = "SELECT u from TCSAssignmentEntity u where u.schoolClassEntity = :classid AND u.subjectEntity = :subjectid"),
        @NamedQuery(name = "teacherclasssubjectassignment.findTCS", query = "SELECT u from TCSAssignmentEntity u where u.teacherEntity = :teacherid AND u.schoolClassEntity = :classid AND u.subjectEntity = :subjectid")
})
public class TCSAssignmentEntity {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubjectEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacherid", insertable=true, updatable=true,nullable=true)
    private TeacherEntity teacherEntity;

    @ManyToOne
    @JoinColumn(name = "classid", insertable=true, updatable=true,nullable=true)
    private SchoolClassEntity schoolClassEntity;

    @ManyToOne
    @JoinColumn(name = "subjectid", insertable=true, updatable=true,nullable=true)
    private SubjectEntity subjectEntity;

    public TCSAssignmentEntity(Long id, TeacherEntity teacherEntity, SchoolClassEntity schoolClassEntity, SubjectEntity subjectEntity) {
        this.id = id;
        this.teacherEntity = teacherEntity;
        this.schoolClassEntity = schoolClassEntity;
        this.subjectEntity = subjectEntity;
    }

    public TCSAssignmentEntity() {}

    @PostLoad
    private void postLoad() {
        LOGGER.info("postLoad: {}", toString());
    }

    public String toString(){
        return Objects.toStringHelper(this)
                .add("id", id)
                .add("teacherid", teacherEntity.getId())
                .add("classid", schoolClassEntity.getId())
                .add("subjectid", subjectEntity.getId())
                .toString();
    }


    public Long getId() {
        return id;
    }

    public TeacherEntity getTeacherEntity() {
        return teacherEntity;
    }

    public SchoolClassEntity getSchoolClassEntity() {
        return schoolClassEntity;
    }

    public SubjectEntity getSubjectEntity() {
        return subjectEntity;
    }
}
