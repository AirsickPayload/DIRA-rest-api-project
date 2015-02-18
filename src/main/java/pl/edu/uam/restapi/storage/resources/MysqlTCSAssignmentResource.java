package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.storage.database.MysqlDB;
import pl.edu.uam.restapi.storage.database.TeacherClassSubjectAssignmentDatabase;

import javax.ws.rs.Path;

/**
 * Created by alan on 14.01.2015.
 */
@Path("/mysql/tcsassignments")
@Api(value = "/mysql/tcsassignments", description = "Operations about Teacher-Class-Subject Assignments using mysql")
public class MysqlTCSAssignmentResource extends AbstractTCSAssignmentResource {
    private static final TeacherClassSubjectAssignmentDatabase database = new MysqlDB();

    @Override
    protected TeacherClassSubjectAssignmentDatabase tcsAssignmentDatabase() {
        return database;
    }
}
