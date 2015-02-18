package pl.edu.uam.restapi.storage.entity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.Objects;


import javax.persistence.*;



/**
 * Created by alan on 05.01.2015.
 */
@Entity
@Table(name = "studentclassassignment")
@NamedQueries({
        @NamedQuery(name = "studentclassassignment.findAll", query = "SELECT u FROM StudentClassAssignmentEntity u"),
        @NamedQuery(name = "studentclassassignment.findStudent", query = "SELECT u FROM StudentClassAssignmentEntity u where u.studentEntity = :pesel"),
        @NamedQuery(name = "studentclassassignment.findClass", query = "SELECT u FROM StudentClassAssignmentEntity u where u.schoolClassEntity = :classid"),
        @NamedQuery(name = "studentclassassignment.findSC", query = "SELECT u FROM StudentClassAssignmentEntity u where u.studentEntity = :pesel AND u.schoolClassEntity = :classid")

})
public class StudentClassAssignmentEntity {
        private static final Logger LOGGER = LoggerFactory.getLogger(StudentClassAssignmentEntity.class);

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        Long id;

        @ManyToOne
        @JoinColumn(name = "pesel", insertable=true, updatable=true,nullable=true, unique = true)
        private StudentEntity studentEntity;


        @ManyToOne
        @JoinColumn(name = "classid",insertable=true, updatable=true,nullable=true)
        private SchoolClassEntity schoolClassEntity;

        public StudentClassAssignmentEntity() {}

        public StudentClassAssignmentEntity(Long id, StudentEntity studentEntity, SchoolClassEntity schoolClassEntity) {
                this.id = id;
                this.studentEntity = studentEntity;
                this.schoolClassEntity = schoolClassEntity;
        }

        @PostLoad
        private void postLoad() {
                LOGGER.info("postLoad: {}", toString());
        }

        public String toString(){
                return Objects.toStringHelper(this)
                        .add("id", id)
                        .add("pesel", studentEntity.getPesel())
                        .add("classid", schoolClassEntity.getId())
                        .toString();
        }

        public Long getId() {
                return id;
        }

        public StudentEntity getStudentEntity() {
                return studentEntity;
        }

        public SchoolClassEntity getSchoolClassEntity() {
                return schoolClassEntity;
        }
}
