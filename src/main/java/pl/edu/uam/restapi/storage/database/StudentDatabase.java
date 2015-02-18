package pl.edu.uam.restapi.storage.database;

import pl.edu.uam.restapi.storage.model.Student;

import java.util.Collection;

/**
 * Created by alan on 23.12.14.
 */
public interface StudentDatabase {
    Student getStudent(String pesel);
    Student createOrUpdateStudent(Student student);
    Student removeStudent(String pesel);
    Collection<Student> getStudents();
}
