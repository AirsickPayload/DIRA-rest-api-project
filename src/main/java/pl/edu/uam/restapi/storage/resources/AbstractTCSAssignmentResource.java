package pl.edu.uam.restapi.storage.resources;
import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.StudentClassAssignmentException;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.TCSAssignmentException;
import pl.edu.uam.restapi.storage.database.TeacherClassSubjectAssignmentDatabase;
import pl.edu.uam.restapi.storage.model.StudentClassAssignment;
import pl.edu.uam.restapi.storage.model.TCSAssignment;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

/**
 * Created by alan on 14.01.2015.
 */
public abstract class AbstractTCSAssignmentResource {
    protected abstract TeacherClassSubjectAssignmentDatabase tcsAssignmentDatabase();

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get Teacher-Class-Subject Assignments.", response = TCSAssignment.class, responseContainer = "LIST")
    public Collection<TCSAssignment> getTSCAssignments() { return tcsAssignmentDatabase().getTCSAssignments(); }

    @POST
    @ApiOperation(value = "Create a new Teacher-Class-Subject Assignment", response = TCSAssignment.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTSCAssignment(TCSAssignment tcsAssignment){
        TCSAssignment dbCreated = tcsAssignmentDatabase().createTCSAssignment(tcsAssignment);
        return Response.created(URI.create(uriInfo.getPath() + "/" + dbCreated.getId())).entity(dbCreated).build();
    }

    @Path("/searches")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Search [Teacher - Class - Subject] Assignments by id's", response = TCSAssignment.class, responseContainer = "LIST")
    public Collection<TCSAssignment> searchTCSAssignments(@QueryParam("teacherid") String teacherid, @QueryParam("classid") String classid,
                                                          @QueryParam("subjectid") String subjectid){

        Collection<TCSAssignment> queryResult = tcsAssignmentDatabase().searchTCSAssignments(teacherid, classid, subjectid);

        if(queryResult == null){
            throw new TCSAssignmentException("No Teacher-Class-Subject Assignments found", "Nie znaleziono żadnych przyporządkowań Nauczyciel - Klasa - Przedmiot", "http://docu.pl/errors/tcsa-search-empty");
        }

        return queryResult;
    }


    @Path("/{id}")
    @GET
    @ApiOperation(value = "Get Teacher-Class-Subject Assignment by id.", response = TCSAssignment.class)
    @Produces(MediaType.APPLICATION_JSON)
    public TCSAssignment getSCAssignment(@PathParam("id") String id) throws Exception {
        TCSAssignment tcsAssignment = tcsAssignmentDatabase().getTCSAssignment(id);

        if (id.equals("db")) {
            throw new Exception("Database error");
        }

        if (tcsAssignment == null) {
            throw new TCSAssignmentException("Teacher-Class-Subject Assignment not found", "Przyporzadkowanie Nauczyciel-Klasa-Przedmiot nie zostało znalezione", "http://docu.pl/errors/tsca-not-found");
        }
        return tcsAssignment;
    }

    @Path("/{id}")
    @DELETE
    @ApiOperation(value = "Remove Teacher-Class-Subject Asssignment by id", response = TCSAssignment.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSCAssignment(@PathParam("id") String id) throws Exception {
        TCSAssignment tcsAssignment = tcsAssignmentDatabase().removeTCSAssignment(id);

        if (id.equals("db")) {
            throw new Exception("Database error");
        }

        if (tcsAssignment == null) {
            throw new TCSAssignmentException("Teacher-Class-Subject Assignment not found", "Przyporzadkowanie Nauczyciel-Klasa-Przedmiot nie zostało znalezione", "http://docu.pl/errors/tsca-not-found");
        }

        return Response.ok().entity(tcsAssignment).build();
    }

    @Path("/{id}")
    @PUT
    @ApiOperation(value="Update Teacher-Class-Subject Assignment by id", response = TCSAssignment.class)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSCAssignment(@PathParam("id") String id, TCSAssignment tcsAssignment){
        if(!id.equals(tcsAssignment.getId())){
            throw new StudentClassAssignmentException("Teacher-Class-Subject Assignment ID inconsistency detected in retrieved data", "Niezgodnosc numeru ID przyporzadkowania Nauczyciel-Klasa-Przedmiot wykryta w otrzymanych danych", "http://docu.pl/errors/tsca-PUT-error");
        }

        TCSAssignment updated = tcsAssignmentDatabase().updateTCSAssignment(id, tcsAssignment);
        return Response.ok(URI.create(uriInfo.getPath())).entity(updated).build();
    }

}
