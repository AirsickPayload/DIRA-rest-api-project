package pl.edu.uam.restapi.storage.resources;

import com.wordnik.swagger.annotations.ApiOperation;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.StudentException;
import pl.edu.uam.restapi.dokumentacjaibledy.exceptions.SubjectException;
import pl.edu.uam.restapi.storage.database.SubjectDatabase;
import pl.edu.uam.restapi.storage.model.Subject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;

/**
 * Created by alan on 08.01.2015.
 */
public abstract class AbstractSubjectResource {
    protected abstract SubjectDatabase subjectDatabase();

    @Context
    private UriInfo uriInfo;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get subjects collection.", response = Subject.class, responseContainer = "LIST")
    public Collection<Subject> getSubjects() { return subjectDatabase().getSubjects(); }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Get subject by id", response = Subject.class)
    public Subject getStudent(@PathParam("id") String id) throws Exception {
        Subject subject = subjectDatabase().getSubject(id);

        if(id.equals("db")){
            throw new Exception("Database error");
        }

        if(subject == null){
            throw new SubjectException("Subject not found", "Przedmiot nie zostal znaleziony", "http://docu.pl/errors/subject-not-found");
        }

        return subject;
    }

    @Path("/{id}")
    @DELETE
    @ApiOperation(value = "Remove subject by id", response = Subject.class)
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSubject(@PathParam("id") String id) throws Exception {
        Subject subject = subjectDatabase().removeSubject(id);

        if(id.equals("db")){
            throw new Exception("Database error");
        }

        if(subject == null){
            throw new SubjectException("Subject not found", "Subject nie zostal znaleziony", "http://docu.pl/errors/subject-not-found");
        }

        return Response.ok().entity(subject).build();
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update subject by id", response = Subject.class)
    public Response updateSubject(@PathParam("id") String id, Subject subject){
        if(!id.equals(subject.getId())){
            throw new SubjectException("Subject's id inconsistency detected in retrieved data", "Niezgodnosc nr ID przedmiotu wykryta w otrzymanych danych", "http://docu.pl/errors/subject-put-error");
        }
        Subject dbUpdated = subjectDatabase().updateSubject(id, subject);
        return Response.ok(URI.create(uriInfo.getPath())).entity(dbUpdated).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Create a new subject.", response = Subject.class)
    public Response createSubject(Subject subject){
        Subject dbEntity = subjectDatabase().createSubject(subject);
        return Response.created(URI.create(uriInfo.getPath() + "/" + dbEntity.getId() )).entity(dbEntity).build();
    }
}
