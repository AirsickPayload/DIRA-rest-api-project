package pl.edu.uam.restapi.storage.database;

import pl.edu.uam.restapi.storage.model.TCSAssignment;

import java.util.Collection;

/**
 * Created by alan on 09.01.2015.
 */
public interface TeacherClassSubjectAssignmentDatabase {
    Collection<TCSAssignment> getTCSAssignments();
    Collection<TCSAssignment> searchTCSAssignments(String teacherid, String classid, String subjectid);
    TCSAssignment createTCSAssignment(TCSAssignment tcsAssignment);
    TCSAssignment removeTCSAssignment(String id);
    TCSAssignment getTCSAssignment(String id);
    TCSAssignment updateTCSAssignment(String id, TCSAssignment tcsAssignment);
}
