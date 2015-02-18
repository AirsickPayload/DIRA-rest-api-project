package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.storage.database.MysqlDB;
import pl.edu.uam.restapi.storage.database.SchoolClassDatabase;
import pl.edu.uam.restapi.storage.database.StudentClassAssignmentDatabase;

import javax.ws.rs.Path;

/**
 * Created by alan on 05.01.2015.
 */
@Path("/mysql/scassignments")
@Api(value = "/mysql/scassignments", description = "Operations about Student-Class Assignments using mysql")
public class MysqlStudentClassAssignmentResource extends AbstractStudentClassAssignmentResource{
    private static final StudentClassAssignmentDatabase database = new MysqlDB();

    @Override
    protected StudentClassAssignmentDatabase SCAdatabase() {
        return database;
    }
}
