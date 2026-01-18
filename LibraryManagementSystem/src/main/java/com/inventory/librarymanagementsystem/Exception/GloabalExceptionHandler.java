package com.inventory.librarymanagementsystem.Exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
@Provider
public class GloabalExceptionHandler implements ExceptionMapper<Throwable>{

        @Override
        public Response toResponse(Throwable exception) {
            if (exception.getMessage().contains("NumberFormatException") ||
                    exception.getMessage().contains("instantiated")) {

                return Response.status(Response.Status.BAD_REQUEST)
                        .entity(new ErrorMessage("Invalid ID provided. Please send a number."))
                        .build();
            }

            exception.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorMessage("Something went wrong: " + exception.getMessage()))
                    .build();
        }

        public static class ErrorMessage {
            public String error;
            public ErrorMessage(String error) { this.error = error; }
        }
    }