package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.storage.database.MysqlDB;
import pl.edu.uam.restapi.storage.database.SchoolClassDatabase;

import javax.ws.rs.Path;

/**
 * Created by alan on 05.01.2015.
 */
@Path("/mysql/classes")
@Api(value = "/mysql/classes", description = "Operations about classes using mysql")
public class MysqlSchoolClassesResource extends AbstractSchoolClassesResource {
    private static final SchoolClassDatabase database = new MysqlDB();

    @Override
    protected SchoolClassDatabase classesDatabase() {
        return database;
    }
}
