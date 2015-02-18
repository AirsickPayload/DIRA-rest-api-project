package pl.edu.uam.restapi.storage.database;

import pl.edu.uam.restapi.storage.model.Subject;

import java.util.Collection;

/**
 * Created by alan on 08.01.2015.
 */
public interface SubjectDatabase {
    Subject getSubject(String id);
    Subject createSubject(Subject subject);
    Subject updateSubject(String id, Subject subject);
    Subject removeSubject(String id);
    Collection<Subject> getSubjects();
}
