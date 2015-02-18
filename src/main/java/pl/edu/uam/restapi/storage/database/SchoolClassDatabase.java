package pl.edu.uam.restapi.storage.database;

import pl.edu.uam.restapi.storage.model.*;

import java.util.Collection;

/**
 * Created by alan on 29.12.14.
 */
public interface SchoolClassDatabase {
    SchoolClass getClass(String id);
    SchoolClass createClass(SchoolClass rclass);
    SchoolClass removeClass(String id);
    SchoolClass updateClass(String id, SchoolClass rclass);
    Collection<SchoolClass> getClasses();
}
