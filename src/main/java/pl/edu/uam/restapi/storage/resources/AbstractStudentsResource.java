package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.StudentException;
import pl.edu.uam.restapi.storage.database.StudentDatabase;
import pl.edu.uam.restapi.storage.entity.StudentEntity;
import pl.edu.uam.restapi.storage.model.Student;
import pl.edu.uam.restapi.storage.model.StudentClassAssignment;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

/**
 * Created by alan on 23.12.14.
 */
public abstract class AbstractStudentsResource {
    protected abstract StudentDatabase studentDatabase();

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value  = "Get students collection", response = Student.class, responseContainer = "LIST")
    public Collection<Student> list() { return studentDatabase().getStudents();}

    @Path("/{pesel}")
    @GET
    @ApiOperation(value = "Get student by id", response = Student.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Student getStudent(@PathParam("pesel") String pesel) throws Exception {
        Student student = studentDatabase().getStudent(pesel);

        if(pesel.equals("db")){
            throw new Exception("Database error");
        }

        if(student == null){
            throw new StudentException("Student not found", "Student nie zostal znaleziony", "http://docu.pl/errors/student-not-found");
        }

        return student;
    }

    @Path("/{pesel}")
    @DELETE
    @ApiOperation(value = "Remove student by id", response = Student.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeStudent(@PathParam("pesel") String pesel) throws Exception {
        Student student = studentDatabase().removeStudent(pesel);

        if(pesel.equals("db")){
            throw new Exception("Database error");
        }

        if(student == null){
            throw new StudentException("Student not found", "Student nie zostal znaleziony", "http://docu.pl/errors/student-not-found");
        }

        return Response.ok().entity(student).build();
    }

    @Path("/{pesel}")
    @PUT
    @ApiOperation(value = "Create a new student", response = Student.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createOrUpdateStudent(@PathParam("pesel") String pesel, Student student){
        if(!pesel.equals(student.getPesel())){
            throw new StudentException("Student's PESEL inconsistency detected in retrieved data", "Niezgodnosc numeru PESEL studenta wykryta w otrzymanych danych", "http://docu.pl/errors/student-put-error");
        }
        Student createdDbStudent = studentDatabase().createOrUpdateStudent(student);
        return Response.ok(URI.create(uriInfo.getPath())).entity(createdDbStudent).build();
    }

    @POST
    @ApiOperation(value = "Create a new Student.", response = Student.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createStudent(Student student){
        Student dbEntity = studentDatabase().createOrUpdateStudent(student);

        return Response.created(URI.create(uriInfo.getPath() + "/" + dbEntity.getPesel())).entity(dbEntity).build();
    }
}
