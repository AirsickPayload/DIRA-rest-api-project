package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.Api;
import pl.edu.uam.restapi.storage.database.MysqlDB;
import pl.edu.uam.restapi.storage.database.SubjectDatabase;

import javax.ws.rs.Path;

/**
 * Created by alan on 08.01.2015.
 */

@Path("/mysql/subjects")
@Api(value = "/mysql/subjects", description = "Operations about subjects using mysql")
public class MysqlSubjectResource extends AbstractSubjectResource {
    private static final SubjectDatabase database = new MysqlDB();

    @Override
    protected SubjectDatabase subjectDatabase() {
        return database;
    }
}
