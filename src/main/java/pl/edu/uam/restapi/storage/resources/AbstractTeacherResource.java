package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.TeacherException;
import pl.edu.uam.restapi.storage.database.TeacherDatabase;
import pl.edu.uam.restapi.storage.model.Student;
import pl.edu.uam.restapi.storage.model.Teacher;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

/**
 * Created by Alan Matuszczak on 25.12.14.
 */
public abstract class AbstractTeacherResource {
    protected abstract TeacherDatabase teacherDatabase();

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get teachers collection.", response = Teacher.class, responseContainer = "LIST")
    public Collection<Teacher> getTeachers(){ return teacherDatabase().getTeachers(); }


    @Path("/{id}")
    @GET
    @ApiOperation(value = "Get teacher by id.", response = Teacher.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Teacher getTeacher(@PathParam("id") String id) throws Exception {
        Teacher teacher = teacherDatabase().getTeacher(id);

        if (id.equals("db")) {
            throw new Exception("Database error");
        }

        if (teacher == null) {
            throw new TeacherException("Teacher not found", "Nauczyciel nie został znaleziony", "http://docu.pl/errors/teacher-not-found");
        }

        return teacher;
    }

    @Path("/{id}")
    @DELETE
    @ApiOperation(value = "Delete teacher by id.", response =  Teacher.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTeacher(@PathParam("id") String id) throws Exception {
        Teacher teacher = teacherDatabase().removeTeacher(id);

        if (id.equals("db")) {
            throw new Exception("Database error");
        }

        if (teacher == null) {
            throw new TeacherException("Teacher not found", "Nauczyciel nie został znaleziony", "http://docu.pl/errors/teacher-not-found");
        }

        return Response.ok().entity(teacher).build();
    }

    @Path("/{id}")
    @PUT
    @ApiOperation(value="Update teacher by id", response = Teacher.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTeacher(@PathParam("id") String id, Teacher teacher){
        if(!id.equals(teacher.getId())){
            throw new TeacherException("Teacher's ID inconsistency detected in retrieved data", "Niezgodnosc numeru ID nauczyciela wykryta w otrzymanych danych", "http://docu.pl/errors/teacher-PUT-error");
        }

        Teacher updated = teacherDatabase().updateTeacher(id, teacher);

        return Response.created(URI.create(uriInfo.getPath())).entity(updated).build();
    }


    @POST
    @ApiOperation(value = "Create a new teacher.", response =  Teacher.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTeacher(Teacher teacher){
        Teacher createDbTeacher = teacherDatabase().createTeacher(teacher);
        return Response.ok(URI.create(uriInfo.getPath() + "/" + createDbTeacher.getId())).entity(createDbTeacher).build();
    }
}
