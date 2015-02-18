package pl.edu.uam.restapi.dokumentacjaibledy.exceptions;

import pl.edu.uam.restapi.dokumentacjaibledy.model.ExceptionMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by alan on 08.01.2015.
 */
public class SubjectException extends WebApplicationException {
    public SubjectException(String message, String userMessage, String info) {
        //super(Responses.notFound().type(MediaType.APPLICATION_JSON).entity(message).build());
        super(Response.status(Response.Status.NOT_FOUND).
                entity(createExceptionMessage(message, userMessage, info))
                .type(MediaType.APPLICATION_JSON)
                .build());
    }

    private static ExceptionMessage createExceptionMessage(String message, String userMessage, String info) {
        return new ExceptionMessage(message, userMessage, info);
    }
}