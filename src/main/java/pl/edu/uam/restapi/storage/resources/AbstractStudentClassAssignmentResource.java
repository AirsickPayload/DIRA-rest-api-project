package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.StudentClassAssignmentException;
import pl.edu.uam.restapi.storage.database.StudentClassAssignmentDatabase;
import pl.edu.uam.restapi.storage.model.StudentClassAssignment;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

/**
 * Created by Alan Matuszczak on 05.01.2015.
 */
public abstract class AbstractStudentClassAssignmentResource {
    protected abstract StudentClassAssignmentDatabase SCAdatabase();

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get Student-Class Assignments.", response = StudentClassAssignment.class, responseContainer = "LIST")
    public Collection<StudentClassAssignment> getSCAssignments() { return SCAdatabase().getSCAssignments(); }

    @Path("/searches")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Search Student-Class Assignments by id's", response = StudentClassAssignment.class, responseContainer = "LIST")
    public Collection<StudentClassAssignment> searchSCAssignments(@QueryParam("pesel") String pesel, @QueryParam("classid") String classid){
        Collection<StudentClassAssignment> queryResult = SCAdatabase().searchSCAssignments(pesel, classid);

        if(queryResult == null){
            throw new StudentClassAssignmentException("No Student - Class Assignments found", "Nie znaleziono żadnych przyporządkowań Uczen - Klasa", "http://docu.pl/errors/scassignment-search-not-found");
        }
        return queryResult;
    }

    @POST
    @ApiOperation(value = "Create a new Student-Class Assignment", response = StudentClassAssignment.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSCAssignment(StudentClassAssignment studentClassAssignment){
        StudentClassAssignment dbSCAssignment = SCAdatabase().createSCAssignment(studentClassAssignment);
        return Response.created(URI.create(uriInfo.getPath() + "/" + dbSCAssignment.getId())).entity(dbSCAssignment).build();
    }

    @Path("/{id}")
    @GET
    @ApiOperation(value = "Get Student-Class Assignment by id.", response = StudentClassAssignment.class)
    @Produces(MediaType.APPLICATION_JSON)
    public StudentClassAssignment getSCAssignment(@PathParam("id") String id) throws Exception {
        StudentClassAssignment studentClassAssignment = SCAdatabase().getSCAssignment(id);

        if (id.equals("db")) {
            throw new Exception("Database error");
        }

        if (studentClassAssignment == null) {
            throw new StudentClassAssignmentException("Student-Class Assignment not found", "Przyporzadkowanie Uczen-Klasa nie zostało znalezione", "http://docu.pl/errors/sca-not-found");
        }
        return studentClassAssignment;
    }

    @Path("/{id}")
    @DELETE
    @ApiOperation(value = "Remove School-Class Asssignment by id", response = StudentClassAssignment.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSCAssignment(@PathParam("id") String id) throws Exception {
        StudentClassAssignment studentClassAssignment = SCAdatabase().removeSCAssignment(id);

        if (id.equals("db")) {
            throw new Exception("Database error");
        }

        if (studentClassAssignment == null) {
            throw new StudentClassAssignmentException("Student-Class Assignment not found", "Przyporzadkowanie Uczen-Klasa nie zostało znalezione", "http://docu.pl/errors/sca-not-found");
        }

        return Response.ok().entity(studentClassAssignment).build();
    }


    @Path("/{id}")
    @PUT
    @ApiOperation(value="Update Student-Class Assignment by id", response = StudentClassAssignment.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSCAssignment(@PathParam("id") String id,StudentClassAssignment studentClassAssignment){
        if(!id.equals(studentClassAssignment.getId())){
            throw new StudentClassAssignmentException("Student-Class Assignment ID inconsistency detected in retrieved data", "Niezgodnosc numeru ID przyporzadkowania Uczen-Klasa wykryta w otrzymanych danych", "http://docu.pl/errors/sca-PUT-error");
        }

        StudentClassAssignment updated = SCAdatabase().updateSCAssignment(id,studentClassAssignment);
        return Response.ok(URI.create(uriInfo.getPath())).entity(updated).build();
    }
}
