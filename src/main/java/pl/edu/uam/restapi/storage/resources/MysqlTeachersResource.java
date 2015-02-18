package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.storage.database.MysqlDB;
import pl.edu.uam.restapi.storage.database.TeacherDatabase;

import javax.ws.rs.Path;

/**
 * Created by alan on 25.12.14.
 */
@Path("/mysql/teachers")
@Api(value="/mysql/teachers", description = "Teachers resource operations")
public class MysqlTeachersResource extends AbstractTeacherResource {
    private static final TeacherDatabase database = new MysqlDB();
    @Override
    protected TeacherDatabase teacherDatabase() {
        return database;
    }
}
