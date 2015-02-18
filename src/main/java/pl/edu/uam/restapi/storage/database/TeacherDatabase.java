package pl.edu.uam.restapi.storage.database;

import pl.edu.uam.restapi.storage.model.Teacher;

import java.util.Collection;

/**
 * Created by alan on 25.12.14.
 */
public interface TeacherDatabase {
    Teacher getTeacher(String id);
    Teacher createTeacher(Teacher teacher);
    Teacher removeTeacher(String id);
    Teacher updateTeacher(String id, Teacher teacher);
    Collection<Teacher> getTeachers();
}
