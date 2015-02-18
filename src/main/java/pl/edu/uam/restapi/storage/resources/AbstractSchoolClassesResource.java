package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.SchoolClassException;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.TeacherException;
import pl.edu.uam.restapi.storage.database.SchoolClassDatabase;
import pl.edu.uam.restapi.storage.model.SchoolClass;
import pl.edu.uam.restapi.storage.model.Teacher;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

/**
 * Created by alan on 29.12.14.
 */
public abstract class AbstractSchoolClassesResource {
    protected abstract SchoolClassDatabase classesDatabase();

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get classes collection.", response = SchoolClass.class, responseContainer = "LIST")
    public Collection<SchoolClass> getClasses(){ return classesDatabase().getClasses(); }


    @Path("/{id}")
    @GET
    @ApiOperation(value = "Get class by id.", response = SchoolClass.class)
    @Produces(MediaType.APPLICATION_JSON)
    public SchoolClass getClass(@PathParam("id") String id) throws Exception {
        SchoolClass rclass = classesDatabase().getClass(id);

        if (rclass == null) {
            throw new SchoolClassException("SchoolClass not found", "Klasa nie została znaleziona", "http://docu.pl/errors/schoolclass-not-found");
        }

        if (id.equals("db")) {
            throw new Exception("Database error");
        }


        return rclass;
    }

    @Path("/{id}")
    @DELETE
    @ApiOperation(value = "Delete class by id.", response =  SchoolClass.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeClass(@PathParam("id") String id) throws Exception {
        SchoolClass rclass = classesDatabase().removeClass(id);

        if (id.equals("db")) {
            throw new Exception("Database error");
        }

        if (rclass == null) {
            throw new SchoolClassException("Class not found", "Klasa nie została znaleziona", "http://docu.pl/errors/schoolclass-not-found");
        }

        return Response.ok().entity(rclass).build();
    }

    @Path("/{id}")
    @PUT
    @ApiOperation(value="Update class by id", response = SchoolClass.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateClass(@PathParam("id") String id, SchoolClass rclass){
        if(!id.equals(rclass.getId())){
            throw new SchoolClassException("Class' ID inconsistency detected in retrieved data", "Niezgodnosc numeru ID klasy wykryta w otrzymanych danych", "http://docu.pl/errors/teacher-PUT-error");
        }

        SchoolClass updated = classesDatabase().updateClass(id, rclass);

        return Response.ok(URI.create(uriInfo.getPath())).entity(updated).build();
    }


    @POST
    @ApiOperation(value = "Create a new class.", response =  SchoolClass.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createClass(SchoolClass rclass){
        SchoolClass createDbClass = classesDatabase().createClass(rclass);
        return Response.created(URI.create(uriInfo.getPath() + "/" + createDbClass.getId())).entity(createDbClass).build();
    }
}
