package pl.edu.uam.restapi.storage.database;

import pl.edu.uam.restapi.storage.model.StudentClassAssignment;

import java.util.Collection;

/**
 * Created by Alan Matuszczak on 05.01.2015.
 */
public interface StudentClassAssignmentDatabase {
    Collection<StudentClassAssignment> getSCAssignments();
    Collection<StudentClassAssignment> searchSCAssignments(String pesel, String classid);
    StudentClassAssignment createSCAssignment(StudentClassAssignment studentClassAssignment);
    StudentClassAssignment getSCAssignment(String id);
    StudentClassAssignment removeSCAssignment(String id);
    StudentClassAssignment updateSCAssignment(String id, StudentClassAssignment studentClassAssignment);
}
