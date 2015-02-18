package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.storage.database.MysqlDB;
import pl.edu.uam.restapi.storage.database.StudentDatabase;

import javax.ws.rs.Path;

/**
 * Created by alan on 23.12.14.
 */
@Path("/mysql/students")
@Api(value = "/mysql/students", description = "Operations about students using mysql")
public class MysqlStudentsResource extends AbstractStudentsResource {
    private static final StudentDatabase database = new MysqlDB();

    @Override
    protected StudentDatabase studentDatabase() {
        return database;
    }
}
