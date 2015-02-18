package pl.edu.uam.restapi.dokumentacjaibledy.exceptions.mappers;


import pl.edu.uam.restapi.dokumentacjaibledy.model.ExceptionMessage;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InternalErrorsMapper implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {
        if(WebApplicationException.class.isAssignableFrom(throwable.getClass())) {
            WebApplicationException exc = (WebApplicationException) throwable;

            return Response.status(exc.getResponse().getStatus())
                    .entity(createExceptionMessage(exc))
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(createExceptionMessage(throwable))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    private ExceptionMessage createExceptionMessage(Throwable throwable) {
        return new ExceptionMessage("Internal server error. " + throwable.getMessage(),
                "Wewnętrzny błąd serwera. Spróbuj za kilka minut.",
                "http://aplikacja.pl/erorrs/internal");
    }

    private ExceptionMessage createExceptionMessage(WebApplicationException exc) {
        return new ExceptionMessage(exc.getMessage(),
                "Wewnętrzny błąd serwera. Spróbuj za kilka minut.",
                "http://aplikacja.pl/erorrs/internal");
    }
}
