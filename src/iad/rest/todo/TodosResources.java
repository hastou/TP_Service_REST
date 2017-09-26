package iad.rest.todo;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;
import java.net.URI;
import java.util.*;


@Path("/todos")
@Produces(MediaType.APPLICATION_XML)
@Consumes(MediaType.APPLICATION_XML)
public class TodosResources {

    @Context
    UriInfo uriInfo;

    @GET
    public List<Todo> getTodos() {
        List<Todo> todos = new LinkedList<>();
        // todos.add(new Todo("1", "Faire les courses", new Date().toString(), Collections.singletonList("Important")));
        todos.addAll(Todo.valuesStore());
        return todos;
    }

    @POST
    public Response post(JAXBElement<Todo> todo) {
        Todo t = todo.getValue();
        Response resp;
        String id = t.getId();
        if(Todo.containStore(id)) {
            resp = Response.noContent().build();
        }else {
            Todo.putStore(id, t);
            String uriStr = uriInfo.getAbsolutePath().toString();
            System.out.println(uriStr + "::");
            URI uri = URI.create(uriStr + "/" + id);
            resp = Response.created(uri).build();
        }
        return resp;
    }

    @DELETE
    @Path("/{id}")
    public Response deleteTodo(@PathParam("id") String id) {
        Response resp;
        if(Todo.containStore(id)) {
            Todo.removeStore(id);
            resp = Response.noContent().build();
        }else {
            resp = Response.status(Response.Status.NOT_FOUND).build();
        }
        return resp;
    }

    @PUT
    @Path("/{id}")
    public Response modifyTodo(@PathParam("id") String id, JAXBElement<Todo> todo) {
        Response resp;
        Todo t = todo.getValue();
        if(Todo.containStore(id)) {
            Todo.removeStore(id);
            Todo.putStore(id, t);
            String uriStr = uriInfo.getAbsolutePath().toString();
            URI uri = URI.create(uriStr + "/" + id);
            resp = Response.status(Response.Status.NO_CONTENT).build();
        }else {
            resp = Response.status(Response.Status.NOT_FOUND).build();
        }
        return resp;
    }

}
